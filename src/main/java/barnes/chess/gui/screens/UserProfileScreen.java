package barnes.chess.gui.screens;

import barnes.chess.db.entity.Game;
import barnes.chess.db.entity.UserProfile;
import barnes.chess.db.entity.WinnerType;
import barnes.chess.db.stats.CollectionInterval;
import barnes.chess.db.stats.PlayedGame;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class UserProfileScreen extends AbstractScreen {
  private TextField opponent, startTime, endTime;
  private DashboardScreen parent;
  private TableView<PlayedGame> playedGames;
  private DatePicker start, end;
  private int userId;
  private ComboBox<WinnerType> winnerType;

  public UserProfileScreen(Stage stage, Object... args) {
    super(stage, args);
  }

  @Override
  protected void addComponentsToGrid() {
    start = createDatePicker();
    end = createDatePicker();
    grid.add(createBoldLabel("Opponent", 16), 1, 2);
    grid.add(createBoldLabel("Start Date", 16), 2, 2);
    grid.add(createBoldLabel("Start Time", 16), 2, 2);
    grid.add(createBoldLabel("End Date", 16), 3, 2);
    grid.add(createBoldLabel("End Time", 16), 3, 2);
    grid.add(createBoldLabel("Winner", 16), 4, 2);
    grid.add(createButton("Add game", this::addGame), 5, 2);
  }

  @Override
  protected int getHeight() {
    return 480;
  }

  @Override
  protected int getWidth() {
    return 640;
  }

  @Override
  protected void initComponents() {
    Game.getGames(userId, CollectionInterval.OVERALL, new Date(System.currentTimeMillis()), (games) -> {
      playedGames = createTableView(games);
      playedGames.setOnMouseClicked((e) -> {
        PlayedGame g = playedGames.getSelectionModel().getSelectedItem();
        if (g != null && e.isSecondaryButtonDown()) {
          g.delete();
          updatePlayedGames();
        }
      });
      grid.add(playedGames, 1, 1, 5, 1);
    });

  }

  @Override
  protected void initGrid() {
    grid.getRowConstraints().addAll(
            row(10),
            row(70),
            row(7),
            row(8),
            row(10)
    );
    grid.getColumnConstraints().addAll(
            col(10),
            col(11),
            col(11),
            col(12),
            col(11),
            col(12),
            col(11),
            col(12),
            col(10)
    );
  }

  @Override
  protected void initScreen(Object... args) {
    this.userId = (int) args[0];
    this.parent = (DashboardScreen) args[1];
  }

  @Override
  protected void onClose(WindowEvent e) {
    parent.show(stage);
  }

  @Override
  protected void populateComponents() {

  }

  @Override
  protected void registerEvents() {

  }

  private void addGame(ActionEvent e) {
    long startTime, endTime;
    try {
      startTime = getStartTime();
    } catch (Throwable err) {
      showAlert(Alert.AlertType.ERROR, "Invalid start time", "The entered start time is invalid");
      return;
    }
    try {
      endTime = getEndTime();
    } catch (Throwable err) {
      showAlert(Alert.AlertType.ERROR, "Invalid start time", "The entered end time is invalid");
      return;
    }
    if (start.getValue().compareTo(end.getValue()) > 0) {
      LocalDate date = start.getValue();
      start.setValue(end.getValue());
      end.setValue(date);
    }
    if (start.getValue().equals(end.getValue()) && endTime < startTime) {
      long time = startTime;
      startTime = endTime;
      endTime = time;
      String timeText = this.startTime.getText();
      this.startTime.setText(this.endTime.getText());
      this.endTime.setText(timeText);
    }
    startTime += localDateToMillis(start.getValue());
    endTime += localDateToMillis(end.getValue());
    long finalStartTime = startTime;
    long finalEndTime = endTime;
    UserProfile.with(opponent.getText(), (u) -> {
      if (u == null) {
        showAlert(Alert.AlertType.ERROR, "Player not found", "Player " + opponent.getText() + " was not found.");
        return;
      }
      new Game(userId, u.getId(), winnerType.getValue(), finalStartTime, finalEndTime,
              () -> showAlert(Alert.AlertType.INFORMATION, "Added game", "Added game to the database"));

    });
  }

  private long getEndTime() {
    return toTime(endTime.getText());
  }

  private long getStartTime() {
    return toTime(startTime.getText());
  }

  public long localDateToMillis(LocalDate date) {
    Calendar cal = GregorianCalendar.getInstance();
    cal.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfWeek().ordinal());
    return cal.getTimeInMillis();
  }

  private void updatePlayedGames() {
    Game.getPlayedGames(userId, (games) -> {
      List<PlayedGame> gameList = playedGames.getItems();
      gameList.clear();
      gameList.addAll(games);
    });
  }
}
