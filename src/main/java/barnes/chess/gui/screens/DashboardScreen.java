package barnes.chess.gui.screens;

import barnes.chess.db.entity.Game;
import barnes.chess.db.entity.UserProfile;
import barnes.chess.db.stats.CollectionInterval;
import barnes.chess.db.stats.StatElement;
import barnes.chess.db.stats.StatType;
import barnes.chess.utils.ErrorAcceptedConsumer;
import barnes.chess.utils.ThreadUtil;
import javafx.geometry.HPos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static barnes.chess.db.stats.CollectionInterval.*;

public class DashboardScreen extends AbstractScreen {
  private Label DailyStatsLabel;
  private TableRow DrawsRow;
  private TableColumn FirstUserColumn;
  private TableRow LosesRow;
  private TableRow MSDrawsRow;
  private TableRow MSLossesRow;
  private TableRow MSPlayedGamesRow;
  private TableRow MSWinGamesRatio;
  private TableRow MSWinLoseRatioRow;
  private TableRow MSWinsRow;
  private Label MonthlyStatsLabel;
  private TableRow OSDrawsRow;
  private TableRow OSLossesRow;
  private TableRow OSPlayedGamesRow;
  private TableRow OSWinGamesRatio;
  private TableRow OSWinLoseRatioRow;
  private TableRow OSWinsRow;
  private Label OverallStatsLabel;
  private TableRow PlayedGamesRow;
  private TableView UserTable;
  private TableRow WSDrawsRow;
  private TableRow WSLossesRow;
  private TableRow WSPlayedGamesRow;
  private TableRow WSWinGamesRatio;
  private TableRow WSWinLoseRatioRow;
  private TableRow WSWinsRow;
  private Label WeeklyStatsLabel;
  private TableRow WinGameRatio;
  private TableRow WinLoseRatio;
  private TableRow WinsRow;
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
    withStatsTable(DAILY, (t) -> grid.add(t, 1, 1));
    withStatsTable(WEEKLY, (t) -> grid.add(t, 2, 1));
    withStatsTable(MONTHLY, (t) -> grid.add(t, 1, 2));
    withStatsTable(OVERALL, (t) -> grid.add(t, 2, 2));
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

    WSPlayedGamesRow = addRow();
    WSWinsRow = addRow();
    WSLossesRow = addRow();
    WSDrawsRow = addRow();
    WSWinLoseRatioRow = addRow();
    WSWinGamesRatio = addRow();

    MSPlayedGamesRow = addRow();
    MSWinsRow = addRow();
    MSLossesRow = addRow();
    MSDrawsRow = addRow();
    MSWinLoseRatioRow = addRow();
    MSWinGamesRatio = addRow();

    OSPlayedGamesRow = addRow();
    OSWinsRow = addRow();
    OSLossesRow = addRow();
    OSDrawsRow = addRow();
    OSWinLoseRatioRow = addRow();
    OSWinGamesRatio = addRow();
  }

  @Override
  protected int getHeight() {
    return 600;
  }

  @Override
  protected int getWidth() {
    return 800;
  }

  @Override
  protected void initGrid() {
    super.initGrid();
    grid.setVgap(30);
    grid.getColumnConstraints().addAll(col(11),
            col(26, HPos.LEFT),
            col(26, HPos.CENTER),
            col(26, HPos.CENTER),
            col(11));
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
    Game.getGames(user, interval, new Date(System.currentTimeMillis()), (games) ->
            ThreadUtil.ui(() -> {
              List<Object> stats = new ArrayList<>();
              for (StatType t : StatType.values())
                stats.add(new StatElement(t, userId, games));
              consumer.accept(createTableView(stats));
            }));
  }

  protected void registerEvents() {
  }
}
