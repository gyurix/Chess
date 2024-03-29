package barnes.chess.gui.screens;

import barnes.chess.ChessLauncher;
import barnes.chess.db.entity.UserProfile;
import barnes.chess.utils.HashUtils;
import javafx.geometry.HPos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import static barnes.chess.utils.HashUtils.HashType.SHA_256;

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

  private void enterPressed(KeyEvent e) {
    if (e.getCode() == KeyCode.ENTER)
      loginButtonClicked(e);
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
  protected void initComponents() {
    usernameLabel = createLabel("Username", 16);
    usernameField = new TextField();

    passwordLabel = createLabel("Password", 16);
    passwordField = new PasswordField();

    loginButton = createButton("Login", this::loginButtonClicked);

    noAccountYetLabel = new Label("Don't have an account yet?\n" +
            "Click here to register");
    noAccountYetLabel.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 14));
    noAccountYetLabel.setTextAlignment(TextAlignment.CENTER);
  }

  @Override
  protected void registerEvents() {
    scene.setOnKeyPressed(this::enterPressed);
    noAccountYetLabel.setOnMouseClicked(this::noAccountYetClicked);
  }

  private void loginButtonClicked(Object o) {
    System.out.println("Logging in...");
    if (usernameField.getText().isEmpty()) {
      showError("No Username", "You did not enter your username");
      return;
    }
    if (passwordField.getText().isEmpty()) {
      showError("No Password", "You did not enter your password");
      return;
    }
    Alert info = showInfo("Logging in...", "Logging in, please wait...");
    UserProfile.with(usernameField.getText(), (p) -> {
      info.close();
      if (p == null) {
        showError("User not found", "User " + usernameField.getText() + " was not found");
        return;
      }
      if (!p.getPwd_hash().equals(HashUtils.hash(passwordField.getText(), SHA_256))) {
        showError("Wrong Password", "The entered password is incorrect.");
        return;
      }
      new DashboardScreen(stage, p);
    });
  }

  private void noAccountYetClicked(MouseEvent e) {
    System.out.println("Opening register screen...");
    new RegisterScreen(stage);
  }
}
