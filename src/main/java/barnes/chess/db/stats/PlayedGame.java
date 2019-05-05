package barnes.chess.db.stats;

import barnes.chess.db.entity.Game;
import barnes.chess.db.entity.SkipField;
import barnes.chess.db.entity.WinnerType;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayedGame {
  private String endTime;
  @SkipField
  private Game game;
  private String opponent;
  private String startTime;
  private WinnerType winner;

  public PlayedGame(ResultSet rs, boolean inverseWinnerType) throws SQLException {
    this(new Game(rs.getInt(1)),
            rs.getString(2),
            rs.getTimestamp(3).toString(),
            rs.getTimestamp(4).toString(),
            WinnerType.valueOf(rs.getString(5)),
            inverseWinnerType);
  }

  public PlayedGame(Game game, String opponent, String startTime, String endTime, WinnerType winner, boolean inverseWinnerType) {
    this.game = game;
    this.startTime = startTime;
    this.endTime = endTime;
    this.opponent = opponent;
    this.winner = inverseWinnerType ? winner.inverse() : winner;
  }

  public void delete() {
    game.delete();
  }
}