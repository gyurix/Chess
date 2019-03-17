package barnes.chess.gui.screens;

import barnes.chess.db.entity.UserProfile;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.lang.reflect.Field;

public class RegisterScreen extends AbstractScreen {
  private TextField firstNameField, lastNameField, nicknameField;
  private Label firstNameLabel, lastNameLabel, nicknameLabel, passwordLabel, confirmPasswordLabel;
  private PasswordField passwordField, confirmPasswordField;
  private Button registerButton;


  public RegisterScreen(Stage stage) {
    super(stage);
  }

  @Override
  protected void addComponentsToGrid() {
    Control[] elements = new Control[]{firstNameLabel, firstNameField, lastNameLabel, lastNameField,
            nicknameLabel, nicknameField, passwordLabel, passwordField, confirmPasswordLabel, confirmPasswordField, registerButton};
    for (int i = 0; i < elements.length; ++i)
      grid.add(elements[i], 1, i);
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
    confirmPasswordField = createPasswordField(14);
    confirmPasswordLabel = createLabel("Confirm Password", 14);
    firstNameField = createTextField(14);
    firstNameLabel = createLabel("First Name", 14);
    lastNameField = createTextField(14);
    lastNameLabel = createLabel("Last Name", 14);
    nicknameField = createTextField(14);
    nicknameLabel = createLabel("Nickname", 14);
    passwordField = createPasswordField(14);
    passwordLabel = createLabel("Password", 14);
    registerButton = createButton("Register", this::registerButtonClicked);
  }

  @Override
  protected void initGrid() {
    grid.getColumnConstraints().addAll(col(15), col(70), col(15));
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
    scene.setOnKeyPressed(this::enterPressed);
  }

  private void enterPressed(KeyEvent e) {
    if (e.getCode() == KeyCode.ENTER)
      registerButtonClicked(e);
  }

  private void registerButtonClicked(Object o) {
    System.out.println("Trying to register...");
    try {
      for (Field f : getClass().getDeclaredFields()) {
        if (TextField.class.isAssignableFrom(f.getType())) {
          f.setAccessible(true);
          TextField field = (TextField) f.get(this);
          String text = field.getText();
          String fn = f.getName().replace("Field", "");
          fn = Character.toUpperCase(fn.charAt(0)) + fn.substring(1);
          if (text.isEmpty()) {
            showError("No " + fn, "You did not fill the " + fn + " field.");
            return;
          }
          if (!(field instanceof PasswordField) && !text.matches("[a-zA-Z0-9]{3,16}")) {
            showError("Incorrect " + fn,
                    "The " + fn + " should be at least 3, at most 16 characters longs\n" +
                            "It can only contain:\n" +
                            "- the lowercase characters of the English alphabet (a-z)\n" +
                            "- the uppercase characters of the English alphabet (A-Z)\n" +
                            "- numbers (0-9)");
            return;
          }
        }
      }
    } catch (Throwable e) {
      e.printStackTrace();
    }
    if (!passwordField.getText().equals(confirmPasswordField.getText())) {
      showError("Passwords mismatch", "The entered two passwords do not match.");
      return;
    }
    String username = nicknameField.getText();
    String pwd = passwordField.getText();
    if (username.toLowerCase().contains(pwd.toLowerCase()) || pwd.toLowerCase().contains(username.toLowerCase())) {
      showError("Unsafe password", "The password should not contain your username");
      return;
    }
    if (pwd.length() < 8 || pwd.length() > 32) {
      showError("Wrong password length", "The password should be between 8 and 32 characters");
      return;
    }
    if (!pwd.matches(".*[a-z]+.*") || !pwd.matches(".*[A-Z]+.*") || !pwd.matches(".*[0-9]+.*")) {
      showError("Unsafe password",
              "The password should contain at least\n" +
                      "- 1 uppercase character\n" +
                      "- 1 lowercase character\n" +
                      "- 1 number");
      return;
    }
    Alert info = showInfo("Registring...", "Registring, please wait...");
    UserProfile.with(username, (p) -> {
      info.close();
      if (p != null) {
        showError("Incorrect username", "Another user with this username exists already");
        return;
      }
      UserProfile.register(nicknameField.getText(), passwordField.getText(),
              firstNameField.getText(), lastNameField.getText(),
              (newP) -> new DashboardScreen(stage, newP));
    });
  }
}
