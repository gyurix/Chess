package barnes.chess.gui.screens;

import barnes.chess.db.entity.Game;
import barnes.chess.db.entity.UserProfile;
import barnes.chess.db.stats.CollectionInterval;
import barnes.chess.db.stats.StatElement;
import barnes.chess.db.stats.StatType;
import barnes.chess.utils.ErrorAcceptedConsumer;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static barnes.chess.db.stats.CollectionInterval.*;

public class DashboardScreen extends AbstractScreen {
  private TableView UserTable;
  private Label currentUserStatsLabel;
  private UserProfile user;
  private ScrollPane userSelectorBar;
  private Label userSelectorLabel;


  public DashboardScreen(Stage stage, UserProfile user) {
    super(stage, user);
  }

  @Override
  protected void addComponentsToGrid() {
    /*grid.add(userSelectorLabel, 1, 1);
    grid.add(currentUserStatsLabel, 1, 1);
    grid.add(userSelectorBar, 1, 2);*/
    grid.add(createLabel("Daily Stats", 16), 2, 2);
    grid.add(createLabel("Weekly Stats", 16), 3, 2);
    grid.add(createLabel("Monthly Stats", 16), 2, 4);
    grid.add(createLabel("Overall Stats", 16), 3, 4);

    withStatsTable(DAILY, (t) -> grid.add(t, 2, 3));
    withStatsTable(WEEKLY, (t) -> grid.add(t, 3, 3));
    withStatsTable(MONTHLY, (t) -> grid.add(t, 2, 5));
    withStatsTable(OVERALL, (t) -> grid.add(t, 3, 5));
  }

  @Override
  protected void initScreen(Object... args) {
    this.user = (UserProfile) args[0];
  }

  protected void initComponents() {
    GridPane grid = new GridPane();
    userSelectorLabel = createLabel("User selector", 10);
    userSelectorBar = createScrollPane(grid);
    currentUserStatsLabel = createLabel("Current user stats", 20);
    //UserTable = createTableView("Nick", "Role", "ID");
    //UserTable.setEditable(false);
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

  protected void registerEvents() {
  }
}
