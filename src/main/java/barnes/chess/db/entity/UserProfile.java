package barnes.chess.db.entity;

import barnes.chess.db.DB;
import barnes.chess.utils.ErrorAcceptedConsumer;
import barnes.chess.utils.ThreadUtil;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class UserProfile extends AbstractEntity {
  private String first_name;
  private String last_name;
  private String nick;
  private String pwd_hash;
  private int rank;
  private Timestamp registered;

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
