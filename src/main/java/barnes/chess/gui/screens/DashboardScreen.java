package barnes.chess.gui.screens;

import barnes.chess.db.entity.Game;
import barnes.chess.db.entity.UserProfile;
import barnes.chess.db.stats.CollectionInterval;
import barnes.chess.db.stats.StatElement;
import barnes.chess.db.stats.StatType;
import barnes.chess.utils.ErrorAcceptedConsumer;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static barnes.chess.db.stats.CollectionInterval.*;

public class DashboardScreen extends AbstractScreen {
  private TableView UserTable;
  private Label currentUserStatsLabel;
  private EnumMap<CollectionInterval, TableView<Object>> statTables = new EnumMap<>(CollectionInterval.class);
  private UserProfile user;
  private ScrollPane userSelectorBar;
  private Label userSelectorLabel;
  private DatePicker statViewDatePicker;


  public DashboardScreen(Stage stage, UserProfile user) {
    super(stage, user);
  }

  @Override
  protected void addComponentsToGrid() {
    /*grid.add(userSelectorLabel, 1, 1);
    grid.add(currentUserStatsLabel, 1, 1);
    grid.add(userSelectorBar, 1, 2);*/
    initStats(DAILY, 2, 3);
    initStats(WEEKLY, 3, 3);
    initStats(MONTHLY, 2, 5);
    initStats(OVERALL, 3, 5);
    grid.add(statViewDatePicker, 2, 1, 2, 1);
  }

  protected void initComponents() {
    GridPane grid = new GridPane();
    userSelectorLabel = createLabel("User selector", 10);
    userSelectorBar = createScrollPane(grid);
    currentUserStatsLabel = createLabel("Current user stats", 20);
    statViewDatePicker = new DatePicker();
    //UserTable = createTableView("Nick", "Role", "ID");
    //UserTable.setEditable(false);
  }

  @Override
  protected void initScreen(Object... args) {
    this.user = (UserProfile) args[0];
  }

  protected void registerEvents() {
    statViewDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
      System.out.println("Date picker value changed - " + newValue);
      for (CollectionInterval interval : CollectionInterval.values())
        updateStatsTable(interval, newValue);
    });
  }

  @Override
  protected int getHeight() {
    return 630;
  }

  @Override
  protected int getWidth() {
    return 800;
  }

  @Override
  protected void initGrid() {
    super.initGrid();
    grid.setVgap(5);
    grid.getColumnConstraints().addAll(col(11),
            col(26, HPos.LEFT),
            col(26, HPos.CENTER),
            col(26, HPos.CENTER),
            col(11));

    grid.getRowConstraints().addAll(row(11),
            row(5, VPos.CENTER),
            row(6, VPos.BOTTOM),
            row(30, VPos.CENTER),
            row(6, VPos.BOTTOM),
            row(30, VPos.CENTER),
            row(11));
  }

  @Override
  protected void onClose(WindowEvent e) {
    new LoginScreen(new Stage());
  }

  @Override
  protected void populateComponents() {

  }

  public void withStatsTable(CollectionInterval interval, ErrorAcceptedConsumer<TableView> consumer) {
    int userId = user.getId();
    Game.getGames(user, interval, new Date(System.currentTimeMillis()), (games) -> {
      List<Object> stats = new ArrayList<>();
      for (StatType t : StatType.values())
        stats.add(new StatElement(t, userId, games));
      consumer.accept(createTableView(stats));
    });
  }

  public void initStats(CollectionInterval interval, int col, int row) {
    withStatsTable(interval, (t) -> {
      statTables.put(interval, t);
      grid.add(createBoldLabel(interval + " Stats", 16), col, row - 1);
      grid.add(t, col, row);
    });
  }

  public void updateStatsTable(CollectionInterval interval, LocalDate date) {
    Calendar cal = GregorianCalendar.getInstance();
    //noinspection MagicConstant
    cal.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfWeek().ordinal());
    int userId = user.getId();
    Game.getGames(user, interval, new Date(cal.getTime().getTime()), (games) -> {
      List<Object> stats = new ArrayList<>();
      for (StatType t : StatType.values())
        stats.add(new StatElement(t, userId, games));
      statTables.get(interval).setItems(new ObservableListWrapper<>(stats));
    });
  }
}
