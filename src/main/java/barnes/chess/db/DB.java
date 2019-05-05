package barnes.chess.db;

import barnes.chess.utils.ErrorAcceptedConsumer;
import lombok.Getter;

import java.sql.*;
import java.util.concurrent.ConcurrentHashMap;

import static barnes.chess.utils.ThreadUtil.async;

public class DB {
  @Getter
  private static DB instance;
  private final DatabaseConfig config;

  static {
    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  private ConcurrentHashMap<Thread, Connection> connections = new ConcurrentHashMap<>();

  public DB(DatabaseConfig config) {
    if (instance != null)
      throw new RuntimeException("DB can only be created once.");
    instance = this;
    this.config = config;
  }

  public Connection checkConnection() {
    Thread thread = Thread.currentThread();
    Connection connection = connections.get(Thread.currentThread());
    try {
      if (connection != null && connection.isValid(config.getTimeout()))
        return connection;
      String check = "jdbc:postgresql://" + config.getHost() + ":" + config.getPort() + "/" + config.getDatabase();
      System.out.println(check);
      connection = DriverManager.getConnection("jdbc:postgresql://" +
                      config.getHost() + ":" + config.getPort() + "/" + config.getDatabase(),
              config.getUsername(), config.getPassword());
      connections.put(thread, connection);
    } catch (Throwable e) {
      e.printStackTrace();
    }
    return connection;
  }

  public void command(ErrorAcceptedConsumer<Boolean> resultHandler, String query, Object... data) {
    async(() -> resultHandler.accept(prepare(query, data).execute()));
  }

  public void command(String query, Object... data) {
    async(() -> prepare(query, data).execute());
  }

  private PreparedStatement prepare(String query, Object... data) throws SQLException {
    PreparedStatement ps = checkConnection().prepareStatement(query);
    for (int i = 0; i < data.length; ++i)
      ps.setObject(i + 1, data[i]);
    return ps;
  }

  public void query(ErrorAcceptedConsumer<ResultSet> resultHandler, String query, Object... data) {
    async(() -> resultHandler.accept(prepare(query, data).executeQuery()));
  }

  public void query2(ErrorAcceptedConsumer<ResultSet> resultHandler, String query, Object... data) {
    async(() -> resultHandler.accept(prepare(query, data).executeQuery()));
  }

  public void update(String query, Object... data) {
    async(() -> prepare(query, data).executeUpdate());
  }

  public void update(ErrorAcceptedConsumer<Integer> resultHandler, String query, Object... data) {
    async(() -> resultHandler.accept(prepare(query, data).executeUpdate()));
  }

  public void shutdown() {
    connections.values().forEach(c -> {
      try {
        c.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    });
  }


}
