package barnes.chess.gui.screens;

import barnes.chess.db.entity.UserProfile;
import javafx.geometry.HPos;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class DashboardScreen extends AbstractScreen {

  private Label dailyStatsLabel;
  private TableView userTable;
  private TableView dailyStatsTable;
  private TableColumn DSFirstColumn;
  private TableColumn DSSecondColumn;
  private TableView weeklyStatsTable;
  private TableColumn WSFirstColumn;
  private TableColumn WSSecondColumn;
  private TableView monthlyStatsTable;
  private TableColumn MSFirstColumn;
  private TableColumn MSSecondColumn;
  private TableView overallStatsTable;
  private TableColumn OSFirstColumn;
  private TableColumn OSSecondColumn;
  private Label currentuserStatsLabel;
  private UserProfile user;
  private ScrollBar userSelectorBar;
  private Label userSelectorLabel;
  private TableColumn First = new TableColumn("First");
  private TableColumn Second = new TableColumn("Second");

  public DashboardScreen(Stage stage, UserProfile user) {
    super(stage);
    this.user = user;
  }

  @Override
  protected void addComponentsToGrid() {
    grid.add(userSelectorLabel, 0, 1);
    grid.add(currentuserStatsLabel, 1, 1);
    grid.add(userSelectorBar, 1, 2);
    grid.add(dailyStatsLabel, 1, 3);
    grid.add(dailyStatsTable, 1, 4);
    //grid.add(First, 1, 1, 0, 0);
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
    dailyStatsLabel = createLabel("Statistics", 10);
    userSelectorBar = createScrollBar();
    currentuserStatsLabel = createLabel("Current user stats", 20);
    userTable = createTableView();
    userTable.setEditable(false);

    dailyStatsTable = createTableView();
    dailyStatsTable.setEditable(false);
    //dailyStatsTable.getColumns().addAll(First, Second);

    weeklyStatsTable = createTableView();
    weeklyStatsTable.setEditable(false);

    monthlyStatsTable = createTableView();
    monthlyStatsTable.setEditable(false);

    overallStatsTable = createTableView();
    overallStatsTable.setEditable(false);
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
