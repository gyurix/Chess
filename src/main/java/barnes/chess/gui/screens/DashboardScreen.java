package barnes.chess.gui.screens;

import barnes.chess.db.DB;
import barnes.chess.db.entity.Game;
import barnes.chess.db.entity.Rank;
import barnes.chess.db.entity.UserProfile;
import barnes.chess.db.stats.CollectionInterval;
import barnes.chess.db.stats.StatElement;
import barnes.chess.db.stats.StatType;
import barnes.chess.db.stats.UserElement;
import barnes.chess.utils.ErrorAcceptedConsumer;
import barnes.chess.utils.ThreadUtil;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static barnes.chess.db.stats.CollectionInterval.*;

public class DashboardScreen extends AbstractScreen {
  private boolean canEditRanks;
  private Label currentUserStatsLabel;
  private int rankCount;
  private EnumMap<CollectionInterval, TableView<Object>> statTables = new EnumMap<>(CollectionInterval.class);
  private DatePicker statViewDatePicker;
  private UserProfile user;
  private int userPage;
  private TextField userSearchField;
  private Label userSelectorLabel;
  private TableView userTable;
  private Button usersNextPage;
  private Button usersPrevPage;

  public DashboardScreen(Stage stage, UserProfile user) {
    super(stage, user);
  }

  @Override
  protected void addComponentsToGrid() {
    initStats(DAILY, 3, 3);
    initStats(WEEKLY, 4, 3);
    initStats(MONTHLY, 3, 5);
    initStats(OVERALL, 4, 5);
    initUsers();

    grid.add(userSelectorLabel, 1, 1);
    grid.add(userSearchField, 1, 2, 2, 1);
    grid.add(usersPrevPage, 1, 5);
    grid.add(usersNextPage, 2, 5);

    grid.add(statViewDatePicker, 3, 1, 2, 1);
  }

  @Override
  protected int getHeight() {
    return 630;
  }

  @Override
  protected int getWidth() {
    return 800;
  }

  protected void initComponents() {
    Rank.withRanks(l -> rankCount = l.size());
    userPage = 1;
    canEditRanks = user.getRank() == 1;
    usersPrevPage = createButton("prev", this::previousButtonClick);
    usersNextPage = createButton("next", this::nextButtonClick);
    userSelectorLabel = createLabel("User selector", 10);
    currentUserStatsLabel = createLabel("Current user stats", 20);
    userSearchField = createTextField(16);
    userSearchField.setOnAction((e) -> {
      userPage = 1;
      updateUsers();
    });
    statViewDatePicker = new DatePicker();
  }

  @Override
  protected void initGrid() {
    super.initGrid();
    grid.setVgap(5);
    grid.getColumnConstraints().addAll(col(11),
            col(13, HPos.LEFT),
            col(13, HPos.RIGHT),
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
  protected void initScreen(Object... args) {
    this.user = (UserProfile) args[0];
  }

  @Override
  protected void onClose(WindowEvent e) {
    new LoginScreen(new Stage());
  }

  @Override
  protected void populateComponents() {

  }

  protected void registerEvents() {
    statViewDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
      System.out.println("Date picker value changed - " + newValue);
      for (CollectionInterval interval : CollectionInterval.values())
        updateStatsTable(interval, newValue);
    });
  }

  public void initStats(CollectionInterval interval, int col, int row) {
    withStatsTable(interval, (t) -> {
      statTables.put(interval, t);
      grid.add(createBoldLabel(interval + " Stats", 16), col, row - 1);
      grid.add(t, col, row);
    });
  }

  public void initUsers() {
    UserProfile.getAll((users) -> {
      userTable = createTableView(users);
      if (!canEditRanks) {
        userTable.setOnMouseClicked(e -> {
          UserElement ue = (UserElement) userTable.getSelectionModel().getSelectedItem();
          DB.getInstance().command((r) ->
                          ThreadUtil.ui(() -> updateUsers()), "UPDATE UserProfile WHERE id = ? SET rank = ?",
                  ue.getId(), (ue.getRoleId() + 1) % rankCount);
        });
      }
      grid.add(userTable, 1, 3, 2, 2);
    }, "", 0, 10);
  }

  private void nextButtonClick(Object o) {
    if (userTable == null)
      return;
    if (userTable.getItems().size() == 10) {
      ++userPage;
      updateUsers();
    }
  }

  private void previousButtonClick(Object o) {
    if (userTable == null)
      return;
    if (userPage > 1) {
      --userPage;
      updateUsers();
    }
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

  public void updateUsers() {
    System.out.println("Update users");
    UserProfile.getAll((users) -> {
      userTable.setItems(new ObservableListWrapper(users));
    }, userSearchField.getText(), (userPage - 1) * 10, 10);
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
}
