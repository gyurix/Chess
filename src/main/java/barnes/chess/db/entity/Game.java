package barnes.chess.db.entity;

import barnes.chess.db.DB;
import barnes.chess.db.data.GameInfoCollectionInterval;
import barnes.chess.utils.ErrorAcceptedConsumer;
import barnes.chess.utils.ThreadUtil;
import lombok.Getter;

import java.sql.Date;

@Getter
public class Game extends DurationHolder {
  private int player1, player2;
  private WinnerType winner;

  public static void getGames(UserProfile user, GameInfoCollectionInterval interval, Date date, int page, ErrorAcceptedConsumer<Game> handler) {
    int id = user.getId();
    DB.getInstance().query((rs) -> {
      while (rs.next()) {
        Game game = new Game();
        game.load(rs);
        ThreadUtil.ui(() -> handler.accept(game));
      }
      rs.close();
    }, "SELECT * FROM Game INNER JOIN Duration ON Duration.id=Game.duration " +
            "WHERE (Game.player1 = ? OR Game.player2 = ?) AND" +
            " Duration.start_time > AND Duration.start_time < ORDER BY Duration.start_time LIMIT 100", id, id);
  }
}
