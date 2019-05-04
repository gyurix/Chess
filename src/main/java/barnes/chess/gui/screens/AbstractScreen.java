package barnes.chess.gui.screens;

import barnes.chess.db.entity.SkipField;
import barnes.chess.gui.ResourceManager;
import barnes.chess.utils.ErrorAcceptedFunction;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.Alert.AlertType.INFORMATION;

public abstract class AbstractScreen {
  protected GridPane grid = new GridPane();
  protected Scene scene;
  protected Stage stage;
  protected String title = "Chess Stats - " + getClass().getSimpleName().replace("Screen", "");

  public AbstractScreen(Stage stage, Object... args) {
    this.stage = stage;
    initScreen(args);
    initGrid();
    initComponents();
    populateComponents();
    addComponentsToGrid();
    createScene();
    registerEvents();
    show(stage);
  }

  protected AbstractScreen() {
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

  public Label createBoldLabel(String text, double fontSize) {
    Label out = new Label(text);
    out.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, fontSize));
    return out;
  }

  public PasswordField createPasswordField(double fontSize) {
    PasswordField out = new PasswordField();
    out.setFont(Font.font(fontSize));
    return out;
  }

  private void createScene() {
    scene = new Scene(grid, getWidth(), getHeight());
  }

  public ScrollPane createScrollPane(GridPane grid) {
    ScrollPane out = new ScrollPane(grid);
    return out;
  }

  public TableColumn<Object, Object> createTableCol(String name, ErrorAcceptedFunction<Object, Object> dataGenerator) {
    TableColumn<Object, Object> col = new TableColumn<>(name);
    col.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(dataGenerator.toFunction().apply(c.getValue())));
    //noinspection unchecked
    col.setComparator((o1, o2) -> o1 instanceof Comparable ? ((Comparable) o1).compareTo(o2) : String.valueOf(o1).compareTo(String.valueOf(o2)));
    col.setMaxWidth(10000);
    return col;
  }

  public TableView createTableView(List data) {
    TableView<Object> out = new TableView<>();
    out.setEditable(false);
    if (data != null && !data.isEmpty()) {
      ObservableList<TableColumn<Object, ?>> columns = out.getColumns();
      out.setItems(new ObservableListWrapper(data));
      Object o = data.get(0);
      Class cl = o.getClass();
      for (Field f : cl.getDeclaredFields()) {
        SkipField skipField = f.getAnnotation(SkipField.class);
        if (skipField != null)
          continue;
        Class fieldType = f.getType();
        String fieldName = f.getName();
        fieldName = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        String getterName = ((fieldType == Boolean.class || fieldType == boolean.class) ? "is" : "get") + fieldName;
        try {
          Method getterMethod = cl.getDeclaredMethod(getterName);
          getterMethod.setAccessible(true);
          columns.add(createTableCol(fieldName, getterMethod::invoke));
        } catch (Throwable e) {
          f.setAccessible(true);
          columns.add(createTableCol(fieldName, f::get));
        }
      }
      out.refresh();
    }
    return out;
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

  protected void initScreen(Object... args) {
  }

  protected abstract void onClose(WindowEvent e);

  protected abstract void populateComponents();

  protected abstract void registerEvents();

  public RowConstraints row(double pct) {
    RowConstraints row = new RowConstraints();
    row.setPercentHeight(pct);
    return row;
  }

  public RowConstraints row(double pct, VPos alignment) {
    RowConstraints row = new RowConstraints();
    row.setPercentHeight(pct);
    row.setValignment(alignment);
    return row;
  }

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
