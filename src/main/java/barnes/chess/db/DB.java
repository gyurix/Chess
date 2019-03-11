package barnes.chess.db;

import barnes.chess.utils.ErrorAcceptedConsumer;
import lombok.Getter;

import java.sql.*;

import static barnes.chess.utils.ThreadUtil.async;

public class DB {
  @Getter
  private static DB instance;
  private final DatabaseConfig config;
  private Connection connection;

  public DB(DatabaseConfig config) {
    if (instance != null)
      throw new RuntimeException("DB can only be created once.");
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
    async(() -> resultHandler.accept(prepare(query, data).execute()));
  }

  public void command(String query, Object... data) {
    async(() -> prepare(query, data).execute());
  }

  private PreparedStatement prepare(String query, Object... data) throws SQLException {
    checkConnection();
    PreparedStatement ps = connection.prepareStatement(query);
    for (int i = 0; i < data.length; ++i)
      ps.setObject(i + 1, data[i]);
    return ps;
  }

  public void query(ErrorAcceptedConsumer<ResultSet> resultHandler, String query, Object... data) {
    async(() -> resultHandler.accept(prepare(query, data).executeQuery()));
  }

  public void update(String query, Object... data) {
    async(() -> prepare(query, data).executeUpdate());
  }

  public void update(ErrorAcceptedConsumer<Integer> resultHandler, String query, Object... data) {
    async(() -> resultHandler.accept(prepare(query, data).executeUpdate()));
  }
}
