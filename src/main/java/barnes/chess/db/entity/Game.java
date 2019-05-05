package barnes.chess.db.entity;

import barnes.chess.db.DB;
import barnes.chess.db.stats.CollectionInterval;
import barnes.chess.db.stats.PlayedGame;
import barnes.chess.utils.ErrorAcceptedConsumer;
import barnes.chess.utils.ErrorAcceptedRunnable;
import barnes.chess.utils.ThreadUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class Game extends DurationHolder {
  private int player1, player2;
  private WinnerType winner;

  public Game(int id) {
    this.id = id;
  }

  public Game(int player1, int player2, WinnerType winner, long from, long to, ErrorAcceptedRunnable onInsert) {
    this.player1 = player1;
    this.player2 = player2;
    this.winner = winner;
    Duration dur = new Duration(new Timestamp(from), new Timestamp(to));
    dur.insert(() -> {
      duration = dur.getId();
      ThreadUtil.async(() -> insert(onInsert));
    });
  }

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
    }, "SELECT * FROM Game" +
            " INNER JOIN Duration ON Duration.id=Game.duration " +
            "WHERE (Game.player1 = ? OR Game.player2 = ?) AND" +
            " Duration.end_time >= ? AND Duration.end_time < ? ORDER BY Duration.end_time", userId, userId, start, end);
  }

  public static void getPlayedGames(int userId, ErrorAcceptedConsumer<List<PlayedGame>> handler) {
    DB.getInstance().query((rs) -> {
      List<PlayedGame> games = new ArrayList<>();
      while (rs.next()) {
        PlayedGame game = new PlayedGame(rs, false);
        games.add(game);
      }
      rs.close();
      DB.getInstance().query((rs2) -> {
        while (rs2.next()) {
          PlayedGame game = new PlayedGame(rs2, true);
          games.add(game);
        }
        rs2.close();
        ThreadUtil.ui(() -> handler.accept(games));
      }, "SELECT Game.id,UserProfile.nick,Duration.start_time,Duration.end_time,Game.winner" +
              " FROM Game INNER JOIN Duration ON Duration.id=Game.duration" +
              " INNER JOIN UserProfile ON Game.player2 = UserProfile.id" +
              " WHERE (Game.player1 = ?) " + " ORDER BY Duration.end_time", userId);
    }, "SELECT Game.id,UserProfile.nick,Duration.start_time,Duration.end_time,Game.winner" +
            " FROM Game INNER JOIN Duration ON Duration.id=Game.duration" +
            " INNER JOIN UserProfile ON Game.player2 = UserProfile.id" +
            " WHERE (Game.player1 = ?) " + " ORDER BY Duration.end_time", userId);
  }
}
