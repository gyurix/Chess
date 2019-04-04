package barnes.chess.gui.screens;

import barnes.chess.db.data.CollectionInterval;
import barnes.chess.db.data.StatType;
import barnes.chess.db.entity.Game;
import barnes.chess.db.entity.UserProfile;
import barnes.chess.utils.ErrorAcceptedConsumer;
import barnes.chess.utils.ThreadUtil;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

import java.sql.Date;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
  private TableView dailyStatsTable;
  private TableView monthlyStatsTable;
  private TableView overallStatsTable;
  private UserProfile user;
  private ScrollPane userSelectorBar;
  private Label userSelectorLabel;
  private TableView weeklyStatsTable;


  public DashboardScreen(Stage stage, UserProfile user) {
    super(stage, user);
  }

  @Override
  protected void addComponentsToGrid() {
    grid.add(userSelectorLabel, 0, 1);
    grid.add(currentUserStatsLabel, 1, 1);
    grid.add(userSelectorBar, 1, 2);
    grid.add(dailyStatsTable, 2, 0);
    grid.add(weeklyStatsTable, 3, 0);
    grid.add(monthlyStatsTable, 2, 1);
    grid.add(overallStatsTable, 3, 1);
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
    UserTable = createTableView("Nick", "Role", "ID");
    UserTable.setEditable(false);

    dailyStatsTable = createStatsTable();
    weeklyStatsTable = createStatsTable();
    monthlyStatsTable = createStatsTable();
    overallStatsTable = createStatsTable();

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

  protected void populateComponents() {
    new StatsVisitor(dailyStatsTable, CollectionInterval.DAILY);
    new StatsVisitor(weeklyStatsTable, CollectionInterval.WEEKLY);
    new StatsVisitor(monthlyStatsTable, CollectionInterval.MONTHLY);
    new StatsVisitor(overallStatsTable, CollectionInterval.OVERALL);
  }

  @Override
  protected void onClose(WindowEvent e) {
    new LoginScreen(new Stage());
  }

  public TableView createStatsTable() {
    return createTableView("Stat", "Value");
  }

  protected void registerEvents() {
  }

  private static class UserVisitor implements Callback<TableView, TableRow> {

    @Override
    public TableRow call(TableView param) {
      return null;
    }
  }

  private class StatsVisitor implements ErrorAcceptedConsumer<List<Game>> {
    private TableView view;

    public StatsVisitor(TableView view, CollectionInterval interval) {
      this.view = view;
      Game.getGames(user, interval, new Date(System.currentTimeMillis()), this);
    }

    @Override
    public void accept(List<Game> games) {
      ThreadUtil.ui(() -> {
        int userId = user.getId();
        List<Map.Entry<StatType, Double>> data = new ArrayList<>();
        for (StatType st : StatType.values())
          data.add(new AbstractMap.SimpleEntry<>(st, st.get(userId, games)));
        ObservableList<TableColumn> cols = view.getColumns();
        cols.get(0).setCellValueFactory((c) -> {
          System.out.println("Get value for col 0");
          return new ReadOnlyStringWrapper("Test");
        });
        cols.get(1).setCellValueFactory((c) -> {
          System.out.println("Get value for col 1");
          return new ReadOnlyStringWrapper("Test 2");
        });
        System.out.println("Done");
      });
    }
  }
}
