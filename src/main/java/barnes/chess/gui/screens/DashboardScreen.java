package barnes.chess.gui.screens;

import barnes.chess.ChessLauncher;
import barnes.chess.db.entity.UserProfile;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class DashboardScreen extends AbstractScreen {
  private UserProfile user;

  public class StatsScreen extends AbstractScreen {

    private Label UserSelectorLabel = new Label();
    private Label CurrentUserStatsLabel;
    private ScrollBar UserSellectorBar;
    private TableView UserTable;

    /*private TableColumn FirstUserColumn;
    private TableRow PlayedGamesRow;
    private TableRow WinsRow;
    private TableRow LosesRow;
    private TableRow DrawsRow;
    private TableRow WinLoseRatio;
    private TableRow WinGameRatio;*/

    private Label DailyStatsLabel;
    private Label WeeklyStatsLabel;
    private Label OverallStatsLabel;
    private Label MonthlyStatsLabel;

    private TableView DailyStatsTable;
    private TableView WeeklyStatsTable;
    private TableView MonthlyStatsTable;
    private TableView OverallStatsTable;

    private TableRow DSPlayedGamesRow;
    private TableRow DSWinsRow;
    private TableRow DSLossesRow;
    private TableRow DSDrawsRow;
    private TableRow DSWinLoseRatioRow;
    private TableRow DSWinGamesRatio;


    private TableRow WSPlayedGamesRow;
    private TableRow WSWinsRow;
    private TableRow WSLossesRow;
    private TableRow WSDrawsRow;
    private TableRow WSWinLoseRatioRow;
    private TableRow WSWinGamesRatio;


    private TableRow MSPlayedGamesRow;
    private TableRow MSWinsRow;
    private TableRow MSLossesRow;
    private TableRow MSDrawsRow;
    private TableRow MSWinLoseRatioRow;
    private TableRow MSWinGamesRatio;


    private TableRow OSPlayedGamesRow;
    private TableRow OSWinsRow;
    private TableRow OSLossesRow;
    private TableRow OSDrawsRow;
    private TableRow OSWinLoseRatioRow;
    private TableRow OSWinGamesRatio;



    public StatsScreen(Stage stage) { super(stage); }

    @Override
    protected void addComponentsToGrid(){

      grid.add(UserSelectorLabel, 0, 1);
      grid.add(CurrentUserStatsLabel, 1, 1);
      grid.add(UserSellectorBar, 1, 2);
    }

    protected int getHeight() { return 600; }
    protected int getWidth() { return 600; }

    @Override
    protected void initGrid() {
      super.initGrid();
      grid.setVgap(30);
      grid.getColumnConstraints().addAll(col(30, HPos.RIGHT), col(40, HPos.LEFT), col(30));
    }

    protected void registerEvents(){}

    protected void onClose(WindowEvent e) {
      ChessLauncher.shutdown();
    }

    protected void populateComponents() {
      // No need for any population in the login screen :)
    }

    protected void initComponents() {

      UserSelectorLabel = createLabel("User selector", 10);
      CurrentUserStatsLabel = createLabel("Current user stats", 20);
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



  }



  public DashboardScreen(Stage stage, UserProfile user) {
    super(stage);
    this.user = user;
  }
  //TODO Implement Dashboard Screen functionality

  @Override
  protected void addComponentsToGrid() {

  }

  @Override
  protected int getHeight() {
    return 511;
  }

  @Override
  protected int getWidth() {
    return 832;
  }

  @Override
  protected void initComponents() {

  }

  @Override
  protected void onClose(WindowEvent e) {
    new LoginScreen(new Stage());
  }

  @Override
  protected void populateComponents() {

  }

  @Override
  protected void registerEvents() {

  }
}
