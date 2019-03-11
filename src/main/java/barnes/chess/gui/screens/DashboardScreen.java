package barnes.chess.gui.screens;

import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class DashboardScreen extends AbstractScreen {
  public DashboardScreen(Stage stage) {
    super(stage);
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
