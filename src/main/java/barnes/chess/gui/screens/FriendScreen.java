package barnes.chess.gui.screens;

import barnes.chess.db.entity.Friendship;
import barnes.chess.db.stats.FriendshipInfo;
import barnes.chess.db.stats.UserElement;
import barnes.chess.utils.ThreadUtil;
import javafx.scene.control.TableView;
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
      if (removePerm) {
        friendsTable.setOnMouseClicked((e) -> {
          if (e.isSecondaryButtonDown()) {
            FriendshipInfo info = friendsTable.getSelectionModel().getSelectedItem();
            if (info != null) {
              info.delete();
              ThreadUtil.async(() ->
                      ThreadUtil.ui(
                              () -> Friendship.getAll(Integer.valueOf(ue.getId().trim()), (friends2) -> {
                                List<FriendshipInfo> list = friendsTable.getItems();
                                list.clear();
                                list.addAll(friends2);
                              })));
            }
          }
        });
      }
      grid.add(friendsTable, 1, 1);
    });
  }

  @Override
  protected void initGrid() {
    super.initGrid();
    grid.setVgap(5);
    grid.setHgap(5);
    grid.getColumnConstraints().addAll(col(5), col(90), col(5));
    grid.getRowConstraints().addAll(row(5), row(90), row(5));
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
