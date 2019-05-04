package barnes.chess.gui.screens;

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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static barnes.chess.db.stats.CollectionInterval.*;

public class DashboardScreen extends AbstractScreen {
  private List<Rank> ranks;
  private UserElement selectedUser;
  private EnumMap<CollectionInterval, TableView<Object>> statTables = new EnumMap<>(CollectionInterval.class);
  private DatePicker statViewDatePicker;
  private Label statsLabel;
  private UserProfile user;
  private boolean canEditRanks;
  private int userPage;
  private TextField userSearchField;
  private Label userSelectorLabel;
  private TableView userTable;
  private Button usersNextPage;
  private Button usersPrevPage;
  private Button friendListShow;

  public DashboardScreen(Stage stage, UserProfile user) {
    super(stage, user);
  }

  @Override
  protected void addComponentsToGrid() {
    grid.add(statsLabel, 3, 0, 2, 1);
    initStats(DAILY, 3, 3);
    initStats(WEEKLY, 4, 3);
    initStats(MONTHLY, 3, 5);
    initStats(OVERALL, 4, 5);
    initUsers();

    grid.add(userSelectorLabel, 1, 0, 2, 1);
    grid.add(userSearchField, 1, 1, 2, 1);
    grid.add(friendListShow, 3, 6);
    grid.add(usersPrevPage, 1, 6);
    grid.add(usersNextPage, 2, 6);

    grid.add(statViewDatePicker, 3, 1, 2, 1);
  }

  @Override
  protected int getHeight() {
    return 715;
  }

  @Override
  protected int getWidth() {
    return 920;
  }

  protected void initComponents() {
    Rank.withRanks(r -> ranks = r);
    userPage = 1;
    canEditRanks = user.getRank() == 1;
    friendListShow = createButton("friends", this::friendShowButtonClick);
    usersPrevPage = createButton("prev", this::previousButtonClick);
    usersNextPage = createButton("next", this::nextButtonClick);
    userSelectorLabel = createBoldLabel("User selector", 16);
    statsLabel = createBoldLabel("Stats of user " + user.getNick() + ":", 20);
    userSearchField = createTextField(16);
    userSearchField.setOnAction((e) -> {
      userPage = 1;
      updateUsers();
    });
    statViewDatePicker = createDatePicker();
  }

  @Override
  protected void initGrid() {
    super.initGrid();
    grid.setVgap(5);
    grid.getColumnConstraints().addAll(col(5),
            col(23, HPos.LEFT),
            col(23, HPos.RIGHT),
            col(22, HPos.CENTER),
            col(22, HPos.CENTER),
            col(5));

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
      if (selectedUser == null)
        updateStatsTable(user.getId(), user.getNick(), newValue);
      else
        updateStatsTable(Integer.valueOf(selectedUser.getId().trim()), selectedUser.getName(), newValue);
    });
  }

  public void initStats(CollectionInterval interval, int col, int row) {
    withStatsTable(interval, (t) -> {
      statTables.put(interval, t);
      grid.add(createBoldLabel(interval + " Stats", 16), col, row - 1);
      grid.add(t, col, row);
    });
  }

  private MenuItem createMenuItem(String text, Runnable onClick) {
    MenuItem menuItem = new MenuItem(text);
    menuItem.setOnAction((e) -> onClick.run());
    return menuItem;
  }

  private void friendShowButtonClick(Object o) {
    System.out.println("Waiting for opening Friends Screen...");
    new FriendScreen(this, stage, user);
  }

  public void initUsers() {
    UserProfile.getAll((users) -> {
      userTable = createTableView(users);
      userTable.setOnMouseClicked(e -> {
        UserElement ue = (UserElement) userTable.getSelectionModel().getSelectedItem();
        if (ue == null) {
          return;
        }
        updateStatsTable(Integer.valueOf(ue.getId().trim()), ue.getName(), statViewDatePicker.getValue());
        if (e.getButton() == MouseButton.SECONDARY) {
          showContextMenu(e, ue);
        }
      });
      grid.add(userTable, 1, 2, 2, 4);
    }, "", 0, 20);
  }

  private void showContextMenu(MouseEvent event, UserElement ue) {
    ContextMenu contextMenu = new ContextMenu();
    List<MenuItem> items = contextMenu.getItems();
    if (canEditRanks && Integer.valueOf(ue.getId().trim()) != user.getId()) {
      items.add(createMenuItem("Change rank", () -> {
        ue.updateRank(ue.getRankId() % ranks.size() + 1, (c) -> {
          if (c > 0)
            ThreadUtil.ui(this::updateUsers);
        });
      }));
    }
    contextMenu.show(userTable, event.getScreenX(), event.getScreenY());
  }

  private void nextButtonClick(Object o) {
    if (userTable == null)
      return;
    if (userTable.getItems().size() == 20) {
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

  public void updateStatsTable(int userId, String userName, LocalDate date) {
    Calendar cal = GregorianCalendar.getInstance();
    //noinspection MagicConstant
    cal.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfWeek().ordinal());
    for (CollectionInterval interval : CollectionInterval.values()) {
      Game.getGames(userId, interval, new Date(cal.getTime().getTime()), (games) -> {
        statsLabel.setText("Stats of user " + userName + ":");
        List<Object> stats = new ArrayList<>();
        for (StatType t : StatType.values())
          stats.add(new StatElement(t, userId, games));
        statTables.get(interval).setItems(new ObservableListWrapper<>(stats));
      });
    }
  }

  public void updateUsers() {
    System.out.println("Update users");
    UserProfile.getAll((users) -> {
      userTable.setItems(new ObservableListWrapper(users));
    }, userSearchField.getText(), (userPage - 1) * 20, 20);
  }

  public void withStatsTable(CollectionInterval interval, ErrorAcceptedConsumer<TableView> consumer) {
    int userId = user.getId();
    Game.getGames(userId, interval, new Date(System.currentTimeMillis()), (games) -> {
      List<Object> stats = new ArrayList<>();
      for (StatType t : StatType.values())
        stats.add(new StatElement(t, userId, games));
      consumer.accept(createTableView(stats));
      updateStatsTable(userId, user.getNick(), statViewDatePicker.getValue());
    });
  }
}
