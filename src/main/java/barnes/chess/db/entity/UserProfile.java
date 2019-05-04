package barnes.chess.db.entity;

import barnes.chess.db.DB;
import barnes.chess.db.stats.UserElement;
import barnes.chess.utils.ErrorAcceptedConsumer;
import barnes.chess.utils.ThreadUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static barnes.chess.utils.HashUtils.HashType.SHA_256;
import static barnes.chess.utils.HashUtils.hash;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile extends AbstractEntity {
  private String first_name;
  private String last_name;
  private String nick;
  private String pwd_hash;
  private int rank;
  private Timestamp registered;

  public static void register(String nick, String pwd, String first_name, String last_name, ErrorAcceptedConsumer<UserProfile> handler) {
    UserProfile p = new UserProfile(first_name, last_name, nick, hash(pwd, SHA_256),
            1, new Timestamp(System.currentTimeMillis()));
    p.insert(() -> handler.accept(p));
  }

  public static void with(String nick, ErrorAcceptedConsumer<UserProfile> handler) {
    DB.getInstance().query((rs) -> {
      if (rs.next()) {
        UserProfile profile = new UserProfile();
        profile.load(rs);
        ThreadUtil.ui(() -> handler.accept(profile));
      } else
        ThreadUtil.ui(() -> handler.accept(null));
      rs.close();
    }, "SELECT * FROM UserProfile WHERE nick=? LIMIT 1", nick);
  }

  public static void getAll(ErrorAcceptedConsumer<List<UserElement>> handler, String query, int from, int count) {
    query = "%" + query + "%";
    String cmd = "SELECT s1.userId,s1.name,s1.rankId,s1.rankName,s1.games+s2.games,s1.wins+s2.wins,s1.loses+s2.loses,s1.draws+s2.draws FROM" +
            " (SELECT DISTINCT UserProfile.id AS userId, UserProfile.nick AS name," +
            "          count(*) OVER (PARTITION BY UserProfile.id) AS games, " +
            "          count(case when Game.winner = 'P1' then 1 else null end) OVER (PARTITION BY Game.player1) AS wins," +
            "          count(case when Game.winner = 'P2' then 1 else null end) OVER (PARTITION BY Game.player1) AS loses," +
            "          count(case when Game.winner = 'DRAW' then 1 else null end) OVER (PARTITION BY Game.player1) AS draws," +
            "          Rank.name AS rankName, Rank.id AS rankId" +
            "       FROM UserProfile" +
            "       LEFT JOIN Rank ON UserProfile.rank=Rank.id" +
            "       INNER JOIN Game ON Game.player1=UserProfile.id" +
            "       WHERE UserProfile.nick LIKE ? OFFSET " + from + " LIMIT " + count + ") AS s1 INNER JOIN " +
            " (SELECT DISTINCT UserProfile.id AS userId, UserProfile.nick AS name," +
            "          count(*) OVER (PARTITION BY UserProfile.id) AS games, " +
            "          count(case when Game.winner = 'P2' then 1 else null end) OVER (PARTITION BY Game.player2) AS wins," +
            "          count(case when Game.winner = 'P1' then 1 else null end) OVER (PARTITION BY Game.player2) AS loses," +
            "          count(case when Game.winner = 'DRAW' then 1 else null end) OVER (PARTITION BY Game.player2) AS draws" +
            "       FROM UserProfile" +
            "       LEFT JOIN Game ON Game.player2=UserProfile.id" +
            "       WHERE UserProfile.nick LIKE ? OFFSET " + from + " LIMIT " + count + ") AS s2 ON s1.userId = s2.userId" +
            " ORDER BY s1.userId";
    System.out.println("Command: " + cmd);
    DB.getInstance().query((rs) -> {
      List<UserElement> users = new ArrayList<>();
      while (rs.next())
        users.add(UserElement.builder()
                .id(StringUtils.leftPad(String.valueOf(rs.getInt(1)), 2, ' '))
                .name(rs.getString(2))
                .rankId(rs.getInt(3))
                .rank(rs.getString(4))
                .tGames(rs.getInt(5))
                .tWins(rs.getInt(6))
                .tLoses(rs.getInt(7))
                .tDraws(rs.getInt(8))
                .build());
      ThreadUtil.ui(() -> handler.accept(users));
      rs.close();
    }, cmd, query, query);
  }
}
