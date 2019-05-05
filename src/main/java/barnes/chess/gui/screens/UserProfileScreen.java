package barnes.chess.gui.screens;

import barnes.chess.db.entity.Game;
import barnes.chess.db.entity.UserProfile;
import barnes.chess.db.entity.WinnerType;
import barnes.chess.db.stats.PlayedGame;
import barnes.chess.db.stats.UserElement;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.Alert.AlertType.INFORMATION;

public class UserProfileScreen extends AbstractScreen {
  private boolean editPerm;
  private TextField opponent, startTime, endTime;
  private DashboardScreen parent;
  private TableView<PlayedGame> playedGames;
  private DatePicker start, end;
  private UserElement user;
  private ComboBox<WinnerType> winnerType;

  public UserProfileScreen(Object... args) {
    super(new Stage(), args);
  }

  @Override
  protected void addComponentsToGrid() {
    start = createDatePicker();
    end = createDatePicker();
    grid.add(createLabel("Opponent", 12), 1, 2);
    grid.add(createLabel("Start Date", 12), 2, 2);
    grid.add(createLabel("Start Time", 12), 3, 2);
    grid.add(createLabel("End Date", 12), 4, 2);
    grid.add(createLabel("End Time", 12), 5, 2);
    grid.add(createLabel("Winner", 12), 6, 2);
    grid.add(opponent = createTextField(14), 1, 3);
    grid.add(start = createDatePicker(), 2, 3);
    grid.add(startTime = createTextField(14), 3, 3);
    grid.add(end = createDatePicker(), 4, 3);
    grid.add(endTime = createTextField(14), 5, 3);
    grid.add(winnerType = createComboBox(WinnerType.class), 6, 3);
    grid.add(createButton("Add game", this::addGame), 7, 3);
  }

  @Override
  protected int getHeight() {
    return 715;
  }

  @Override
  protected int getWidth() {
    return 920;
  }

  private void addGame(ActionEvent e) {
    long startTime, endTime;
    try {
      startTime = getStartTime();
    } catch (Throwable err) {
      showAlert(ERROR, "Invalid start time", "The entered start time is invalid");
      return;
    }
    try {
      endTime = getEndTime();
    } catch (Throwable err) {
      showAlert(ERROR, "Invalid start time", "The entered end time is invalid");
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
        showAlert(ERROR, "Player not found", "Player " + opponent.getText() + " was not found.");
        return;
      }
      new Game(Integer.valueOf(user.getId().trim()), u.getId(), winnerType.getValue(), finalStartTime, finalEndTime,
              () -> showAlert(INFORMATION, "Added game", "Added game to the database"));

    });
  }

  @Override
  protected void initGrid() {
    grid.setHgap(5);
    grid.getRowConstraints().addAll(
            row(5),
            row(79),
            row(3),
            row(8),
            row(5)
    );
    grid.getColumnConstraints().addAll(
            col(4.5),
            col(13),
            col(13),
            col(13),
            col(13),
            col(13),
            col(13),
            col(13),
            col(4.5)
    );
  }

  @Override
  protected void initComponents() {
    title = "Chess Stats - Profile of user " + user.getName();
    Game.getPlayedGames(Integer.valueOf(user.getId().trim()), (games) -> {
      playedGames = createTableView(games);
      playedGames.setOnMouseClicked((e) -> {
        PlayedGame g = playedGames.getSelectionModel().getSelectedItem();
        if (g != null && editPerm && e.getButton() == MouseButton.SECONDARY) {
          g.delete();
          updatePlayedGames();
        }
      });
      grid.add(playedGames, 1, 1, 7, 1);
    });

  }

  @Override
  protected void onClose(WindowEvent e) {
  }

  @Override
  protected void populateComponents() {
  }

  @Override
  protected void registerEvents() {

  }

  @Override
  protected void initScreen(Object... args) {
    this.user = (UserElement) args[0];
    this.editPerm = (boolean) args[1];
  }

  private <T extends Enum<T>> ComboBox<T> createComboBox(Class<T> cl) {
    ComboBox<T> out = new ComboBox<>();
    out.getItems().addAll(cl.getEnumConstants());
    return out;
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
    Game.getPlayedGames(Integer.valueOf(user.getId().trim()), (games) -> {
      List<PlayedGame> gameList = playedGames.getItems();
      gameList.clear();
      gameList.addAll(games);
    });
  }
}
