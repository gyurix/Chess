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
    DB.getInstance().query((rs) -> {
      List<UserElement> users = new ArrayList<>();
      while (rs.next())
        users.add(new UserElement(StringUtils.leftPad(String.valueOf(rs.getInt(1)), 7, ' '), rs.getString(2), rs.getString(3), rs.getInt(4)));
      ThreadUtil.ui(() -> handler.accept(users));
      rs.close();
    }, "SELECT UserProfile.id,UserProfile.nick,Rank.name,Rank.id FROM UserProfile LEFT JOIN Rank ON UserProfile.rank=Rank.id" +
            " WHERE UserProfile.nick LIKE ? OR Rank.name LIKE ?" +
            " ORDER BY UserProfile.id OFFSET " + from + " LIMIT " + count, query, query);
  }
}
