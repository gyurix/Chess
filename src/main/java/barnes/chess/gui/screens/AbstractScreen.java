package barnes.chess.gui.screens;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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

  private void createScene() {
    scene = new Scene(grid, getWidth(), getHeight());
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
    stage.setOnCloseRequest(this::onClose);
    stage.show();
  }
}
