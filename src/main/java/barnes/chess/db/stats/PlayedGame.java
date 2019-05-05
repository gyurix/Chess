package barnes.chess.db.stats;

import barnes.chess.db.entity.Game;
import barnes.chess.db.entity.SkipField;
import barnes.chess.db.entity.WinnerType;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayedGame {
  @SkipField
  private Game game;
  private String opponent;
  private String timeBegin;
  private String timeEnded;
  private WinnerType winner;

  public PlayedGame(ResultSet rs, boolean inverseWinnerType) throws SQLException {
    this(new Game(rs.getInt(1)),
            rs.getString(2),
            rs.getTimestamp(3).toString(),
            rs.getTimestamp(4).toString(),
            WinnerType.valueOf(rs.getString(5)),
            inverseWinnerType);
  }

  public PlayedGame(Game game, String opponent, String timeBegin, String timeEnded, WinnerType winner, boolean inverseWinnerType) {
    this.game = game;
    this.timeBegin = timeBegin;
    this.timeEnded = timeEnded;
    this.opponent = opponent;
    this.winner = inverseWinnerType ? winner.inverse() : winner;
  }

  public void delete() {
    game.delete();
  }
}
