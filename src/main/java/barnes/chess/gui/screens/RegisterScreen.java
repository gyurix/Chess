package barnes.chess.gui.screens;

import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class RegisterScreen extends AbstractScreen {
  public RegisterScreen(Stage stage) {
    super(stage);
  }
  //TODO Implement Register Screen functionality

  @Override
  protected void addComponentsToGrid() {

  }

  @Override
  protected int getHeight() {
    return 360;
  }

  @Override
  protected int getWidth() {
    return 320;
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
