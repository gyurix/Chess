package barnes.chess.utils;

import javafx.application.Platform;
import lombok.Getter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadUtil {
  @Getter
  private static final ExecutorService executor = Executors.newCachedThreadPool();

  public static void async(ErrorAcceptedRunnable runnable) {
    executor.execute(runnable.toRunnable());
  }

  public static void ui(ErrorAcceptedRunnable runnable) {
    Platform.runLater(runnable.toRunnable());
  }
}
