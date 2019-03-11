package barnes.chess.gui.screens;

import barnes.chess.ChessLauncher;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class LoginScreen extends AbstractScreen {
  private Button loginButton;
  private Label noAccountYetLabel;
  private PasswordField passwordField;
  private Label passwordLabel;
  private TextField usernameField;
  private Label usernameLabel;

  public LoginScreen(Stage stage) {
    super(stage);
  }

  @Override
  protected void addComponentsToGrid() {
    grid.add(usernameLabel, 0, 1);
    grid.add(usernameField, 1, 1);
    grid.add(passwordLabel, 0, 2);
    grid.add(passwordField, 1, 2);
    grid.add(loginButton, 1, 3);
    grid.add(noAccountYetLabel, 1, 4);
  }

  @Override
  protected int getHeight() {
    return 328;
  }

  @Override
  protected int getWidth() {
    return 480;
  }

  @Override
  protected void initComponents() {
    usernameLabel = new Label("Username");
    usernameLabel.setFont(Font.font(16));
    usernameField = new TextField();

    passwordLabel = new Label("Password");
    passwordLabel.setFont(Font.font(16));
    passwordField = new PasswordField();

    loginButton = new Button("Login");
    loginButton.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 16));
    loginButton.setMaxWidth(10000);
    loginButton.setTextAlignment(TextAlignment.CENTER);

    noAccountYetLabel = new Label("Don't have an account yet?\n" +
            "Click here to register");
    noAccountYetLabel.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 14));
    noAccountYetLabel.setTextAlignment(TextAlignment.CENTER);
  }

  @Override
  protected void initGrid() {
    super.initGrid();
    grid.setVgap(30);
    grid.getColumnConstraints().addAll(col(30, HPos.RIGHT), col(40, HPos.LEFT), col(30));
  }

  @Override
  protected void onClose(WindowEvent e) {
    ChessLauncher.shutdown();
  }

  @Override
  protected void populateComponents() {
    // No need for any population in the login screen :)
  }

  @Override
  protected void registerEvents() {
    loginButton.setOnAction(this::loginButtonClicked);
    scene.setOnKeyPressed(this::loginEnterPressed);
    noAccountYetLabel.setOnMouseClicked(this::noAccountYetClicked);
  }

  private void loginButtonClicked(Object o) {
    System.out.println("Logging in...");
    new DashboardScreen(stage);
  }

  private void loginEnterPressed(KeyEvent e) {
    if (e.getCode() == KeyCode.ENTER)
      loginButtonClicked(e);
  }

  private void noAccountYetClicked(MouseEvent e) {
    System.out.println("Opening register screen...");
    new RegisterScreen(stage);
  }
}
