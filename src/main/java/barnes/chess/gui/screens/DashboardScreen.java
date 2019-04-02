package barnes.chess.gui.screens;

import barnes.chess.db.entity.UserProfile;
import javafx.geometry.HPos;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class DashboardScreen extends AbstractScreen {
  private TableRow DSDrawsRow;
  private TableRow DSLossesRow;
  private TableRow DSPlayedGamesRow;
  private TableRow DSWinGamesRatio;
  private TableRow DSWinLoseRatioRow;
  private TableRow DSWinsRow;
  private Label DailyStatsLabel;
  private TableView DailyStatsTable;
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
  private TableView MonthlyStatsTable;
  private TableRow OSDrawsRow;
  private TableRow OSLossesRow;
  private TableRow OSPlayedGamesRow;
  private TableRow OSWinGamesRatio;
  private TableRow OSWinLoseRatioRow;
  private TableRow OSWinsRow;
  private Label OverallStatsLabel;
  private TableView OverallStatsTable;
  private TableRow PlayedGamesRow;
  private TableView UserTable;
  private TableRow WSDrawsRow;
  private TableRow WSLossesRow;
  private TableRow WSPlayedGamesRow;
  private TableRow WSWinGamesRatio;
  private TableRow WSWinLoseRatioRow;
  private TableRow WSWinsRow;
  private Label WeeklyStatsLabel;
  private TableView WeeklyStatsTable;
  private TableRow WinGameRatio;
  private TableRow WinLoseRatio;
  private TableRow WinsRow;
  private Label currentUserStatsLabel;
  private UserProfile user;
  private ScrollBar userSelectorBar;
  private Label userSelectorLabel;


  public DashboardScreen(Stage stage, UserProfile user) {
    super(stage);
    this.user = user;
  }

  @Override
  protected void addComponentsToGrid() {
    grid.add(userSelectorLabel, 0, 1);
    grid.add(currentUserStatsLabel, 1, 1);
    grid.add(userSelectorBar, 1, 2);
  }

  @Override
  protected int getHeight() {
    return 600;
  }

  @Override
  protected int getWidth() {
    return 800;
  }

  protected void initComponents() {
    userSelectorLabel = createLabel("User selector", 10);
    userSelectorBar = createScrollBar();
    currentUserStatsLabel = createLabel("Current user stats", 20);
    UserTable = createTableView();
    UserTable.setEditable(false);

    DailyStatsTable = createTableView();
    DailyStatsTable.setEditable(false);

    WeeklyStatsTable = createTableView();
    WeeklyStatsTable.setEditable(false);

    MonthlyStatsTable = createTableView();
    MonthlyStatsTable.setEditable(false);

    OverallStatsTable = createTableView();
    OverallStatsTable.setEditable(false);

    DSPlayedGamesRow = addRow();
    DSWinsRow = addRow();
    DSLossesRow = addRow();
    DSDrawsRow = addRow();
    DSWinLoseRatioRow = addRow();
    DSWinGamesRatio = addRow();

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
  protected void initGrid() {
    super.initGrid();
    grid.setVgap(30);
    grid.getColumnConstraints().addAll(col(30, HPos.RIGHT), col(40, HPos.LEFT), col(30));
  }

  @Override
  protected void onClose(WindowEvent e) {
    new LoginScreen(new Stage());
  }

  protected void populateComponents() {
    // No need for any population in the login screen :)
  }

  protected void registerEvents() {
  }

}
