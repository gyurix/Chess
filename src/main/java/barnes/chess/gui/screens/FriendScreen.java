package barnes.chess.gui.screens;

import barnes.chess.db.entity.UserProfile;
import barnes.chess.utils.ThreadUtil;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class FriendScreen extends AbstractScreen{

    private DashboardScreen dash;
    private UserProfile user;

    public FriendScreen(DashboardScreen dash, Stage stage, UserProfile user){
        super(stage, user,dash);
    }

    @Override
    protected void initScreen(Object... args) {
        this.user = (UserProfile) args[0];
        this.dash = (DashboardScreen) args[1];
    }

    @Override
    protected void onClose(WindowEvent e) {
        System.out.println("on close");
        ThreadUtil.ui(()->dash.show(stage));
    }

    @Override
    protected void populateComponents() {

    }

    @Override
    protected void registerEvents() {

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

    }

    @Override
    protected void initGrid() {
        super.initGrid();
        grid.setVgap(5);
    }
}
