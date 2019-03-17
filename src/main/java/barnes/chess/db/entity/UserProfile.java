package barnes.chess.db.entity;

import barnes.chess.db.DB;
import barnes.chess.utils.ErrorAcceptedConsumer;
import barnes.chess.utils.ThreadUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

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
}
