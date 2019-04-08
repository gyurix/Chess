package barnes.chess.db.entity;

import barnes.chess.db.DB;
import barnes.chess.db.stats.CollectionInterval;
import barnes.chess.utils.ErrorAcceptedConsumer;
import barnes.chess.utils.ThreadUtil;
import lombok.Getter;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Game extends DurationHolder {
  private int player1, player2;
  private WinnerType winner;

  public static void getGames(int userId, CollectionInterval interval, Date date,
                              ErrorAcceptedConsumer<List<Game>> handler) {
    Timestamp start = interval.getStart(date);
    Timestamp end = interval.getEnd(date);
    DB.getInstance().query((rs) -> {
      List<Game> games = new ArrayList<>();
      while (rs.next()) {
        Game game = new Game();
        game.load(rs);
        games.add(game);
      }
      rs.close();
      ThreadUtil.ui(() -> handler.accept(games));
    }, "SELECT * FROM Game INNER JOIN Duration ON Duration.id=Game.duration " +
            "WHERE (Game.player1 = ? OR Game.player2 = ?) AND" +
            " Duration.end_time >= ? AND Duration.end_time < ? ORDER BY Duration.end_time", userId, userId, start, end);
  }
}
