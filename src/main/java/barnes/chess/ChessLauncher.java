package barnes.chess;

import barnes.chess.db.DB;
import barnes.chess.db.DatabaseConfig;
import barnes.chess.gui.screens.LoginScreen;
import barnes.chess.utils.ThreadUtil;
import javafx.application.Application;
import javafx.stage.Stage;

public class ChessLauncher extends Application {
  public static void main(String[] args) {
    Application.launch();
  }

  public static void shutdown() {
    System.out.println("Shutting down...");
    DB.getInstance().shutdown();
    ThreadUtil.getExecutor().shutdown();
    System.out.println("Shutdown complete.");
  }

  @Override
  public void start(Stage stage) {
    System.out.println("Started");
    DB db = new DB(DatabaseConfig.builder()
            .host("142.93.101.191")
            .port(5432)
            .username("dbs")
            .password("BkpKuHCEN9myIn3Q")
            .database("dbs").build());
    db.query((r) -> {
      System.out.println("Available ranks:");
      while (r.next()) {
        System.out.println(r.getInt(1) + ": " + r.getString(2));
      }
    }, "SELECT * FROM rank");
    new LoginScreen(stage);
  }
}
