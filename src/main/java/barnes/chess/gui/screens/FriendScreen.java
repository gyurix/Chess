package barnes.chess.gui.screens;

import barnes.chess.db.entity.Friendship;
import barnes.chess.db.stats.FriendshipInfo;
import barnes.chess.db.stats.UserElement;
import barnes.chess.utils.ThreadUtil;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.List;

public class FriendScreen extends AbstractScreen {

  private TableView<FriendshipInfo> friendsTable;
  private boolean removePerm;
  private UserElement ue;

  public FriendScreen(UserElement ue, boolean removePerm) {
    super(new Stage(), ue, removePerm);
  }

  @Override
  protected void addComponentsToGrid() {
    grid.add(createLabel("Right click to remove friendship", 12), 0, 2, 3, 1);
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
  protected void initComponents() {
    this.title = "ChessStats - " + ue.getName() + "'s friends";
    Friendship.getAll(Integer.valueOf(ue.getId().trim()), (friends) -> {
      friendsTable = createTableView(friends);
      grid.add(friendsTable, 1, 1);
      System.out.println("Loaded friends table.");
      friendsTable.setOnMouseClicked((e) -> {
        if (e.getButton() != MouseButton.SECONDARY)
          return;

        FriendshipInfo info = friendsTable.getSelectionModel().getSelectedItem();
        if (info == null)
          return;

        if (!removePerm)
          showAlert(Alert.AlertType.ERROR, "No perm", "You don't have permission for removing other players friends");

        info.getFriendship().delete((succeed) -> ThreadUtil.ui(() -> {
          Friendship.getAll(Integer.valueOf(ue.getId().trim()), (friends2) -> {
            List<FriendshipInfo> list = friendsTable.getItems();
            list.clear();
            list.addAll(friends2);
          });
          showAlert(Alert.AlertType.INFORMATION, "Deleted friend", "Delete friend " + info.getName());
        }));
      });
    });
  }

  @Override
  protected void initGrid() {
    super.initGrid();
    grid.setVgap(5);
    grid.setHgap(5);
    grid.getColumnConstraints().addAll(col(5), col(90), col(5));
    grid.getRowConstraints().addAll(row(5), row(86, VPos.CENTER), row(4), row(5));
  }

  @Override
  protected void initScreen(Object... args) {
    this.ue = (UserElement) args[0];
    this.removePerm = (boolean) args[1];
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
}
