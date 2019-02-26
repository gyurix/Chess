package barnes.chess.db;

import barnes.chess.utils.ErrorAcceptedConsumer;
import barnes.chess.utils.ErrorAcceptedRunnable;

import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DB {
  private final DatabaseConfig config;
  private final ExecutorService executor = Executors.newCachedThreadPool();
  private Connection connection;

  public DB(DatabaseConfig config) {
    this.config = config;
  }

  public void checkConnection() {
    try {
      if (connection != null && connection.isValid(config.getTimeout()))
        return;
      Class.forName("org.postgresql.Driver");
      connection = DriverManager.getConnection("jdbc:postgresql://" +
                      config.getHost() + ":" + config.getPort() + "/" + config.getDatabase(),
              config.getUsername(), config.getPassword());
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }

  public void command(ErrorAcceptedConsumer<Boolean> resultHandler, String query, Object... data) {
    execute(() -> resultHandler.accept(prepare(query, data).execute()));
  }

  public void command(String query, Object... data) {
    execute(() -> prepare(query, data).execute());
  }

  private void execute(ErrorAcceptedRunnable runnable) {
    executor.execute(runnable.toRunnable());
  }

  private PreparedStatement prepare(String query, Object... data) throws SQLException {
    checkConnection();
    PreparedStatement ps = connection.prepareStatement(query);
    for (int i = 0; i < data.length; ++i)
      ps.setObject(i + 1, data[i]);
    return ps;
  }

  public void query(ErrorAcceptedConsumer<ResultSet> resultHandler, String query, Object... data) {
    execute(() -> resultHandler.accept(prepare(query, data).executeQuery()));
  }

  public void update(String query, Object... data) {
    execute(() -> prepare(query, data).executeUpdate());
  }

  public void update(ErrorAcceptedConsumer<Integer> resultHandler, String query, Object... data) {
    execute(() -> resultHandler.accept(prepare(query, data).executeUpdate()));
  }
}
