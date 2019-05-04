package barnes.chess;

import barnes.chess.db.DB;
import barnes.chess.db.DatabaseConfig;
import barnes.chess.gui.screens.LoginScreen;
import barnes.chess.utils.ThreadUtil;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Calendar;

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

    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(System.currentTimeMillis() - 259200000);
    System.out.println((cal.get(Calendar.DAY_OF_WEEK) + 5) % 7);

    System.out.println("Started");
    DB db = new DB(DatabaseConfig.builder()
            .host("142.93.101.191")
            .port(5432)
            .username("dbs")
            .password("BkpKuHCEN9myIn3Q")
            .database("dbs").build());
    new LoginScreen(stage);
  }
}
