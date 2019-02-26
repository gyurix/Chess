package barnes.chess.db;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DatabaseConfig {
  private final String database;
  private final String host;
  private final String password;
  private final int port;
  private final int timeout;
  private final String username;
}
