package barnes.chess.gui.screens;

import barnes.chess.gui.ResourceManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.table.TableColumn;

import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.Alert.AlertType.INFORMATION;

public abstract class AbstractScreen {
  protected GridPane grid = new GridPane();
  protected Scene scene;
  protected Stage stage;
  protected String title = "Chess Stats - " + getClass().getSimpleName().replace("Screen", "");

  public AbstractScreen(Stage stage) {
    this.stage = stage;
    initGrid();
    initComponents();
    populateComponents();
    addComponentsToGrid();
    createScene();
    registerEvents();
    show(stage);
  }

  protected abstract void addComponentsToGrid();

  public ColumnConstraints col(double pct) {
    ColumnConstraints col = new ColumnConstraints();
    col.setPercentWidth(pct);
    return col;
  }

  public ColumnConstraints col(double pct, HPos alignment) {
    ColumnConstraints col = new ColumnConstraints();
    col.setPercentWidth(pct);
    col.setHalignment(alignment);
    return col;
  }

  public Button createButton(String text, EventHandler<ActionEvent> onClick) {
    Button out = new Button(text);
    out.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 16));
    out.setMaxWidth(10000);
    out.setTextAlignment(TextAlignment.CENTER);
    out.setOnAction(onClick);
    return out;
  }

  public Label createLabel(String text, double fontSize) {
    Label out = new Label(text);
    out.setFont(Font.font(fontSize));
    return out;
  }

  public PasswordField createPasswordField(double fontSize) {
    PasswordField out = new PasswordField();
    out.setFont(Font.font(fontSize));
    return out;
  }

  //-------
  public ScrollBar createScrollBar(double fontSize) {
    ScrollBar out = new ScrollBar();
    return out;
  }

  public TableView createTableView(){
      TableView out = new TableView();
      return out;
  }
  public TableRow addRow(){
      TableRow out = new TableRow();
      return out;
  }

  public TableColumn addTable(){
    TableColumn out = new TableColumn();

  }
  //-------

  private void createScene() {
    scene = new Scene(grid, getWidth(), getHeight());
  }

  public TextField createTextField(double fontSize) {
    TextField out = new TextField();
    out.setFont(Font.font(fontSize));
    return out;
  }

  protected abstract int getHeight();

  protected abstract int getWidth();

  protected abstract void initComponents();

  protected void initGrid() {
    grid.setHgap(15);
    grid.setVgap(15);
    grid.setAlignment(Pos.CENTER);
  }

  protected abstract void onClose(WindowEvent e);

  protected abstract void populateComponents();

  protected abstract void registerEvents();

  public void show(Stage stage) {
    stage.setTitle(title);
    stage.setScene(scene);
    if (stage.getIcons().isEmpty())
      stage.getIcons().add(ResourceManager.getInstance().getIcon());
    stage.setOnCloseRequest(this::onClose);
    stage.show();
  }

  public Alert showAlert(Alert.AlertType type, String title, String description) {
    Alert alert = new Alert(type, description);
    alert.setTitle(title);
    ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(ResourceManager.getInstance().getIcon());
    alert.show();
    return alert;
  }

  public Alert showError(String title, String description) {
    return showAlert(ERROR, title, description);
  }

  public Alert showInfo(String title, String description) {
    return showAlert(INFORMATION, title, description);
  }
}
