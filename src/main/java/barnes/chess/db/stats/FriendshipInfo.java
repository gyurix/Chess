package barnes.chess.db.stats;

import barnes.chess.db.entity.Friendship;
import barnes.chess.db.entity.SkipField;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
public class FriendshipInfo {
  @SkipField
  private Friendship friendship;
  private String name;
  private String since;
  private String until;

  public FriendshipInfo(ResultSet rs) throws SQLException {
    friendship = new Friendship();
    friendship.load(rs);
    name = rs.getString(5);
    since = rs.getTimestamp(6).toString();
    until = rs.getTimestamp(7).toString();
  }

  public void delete() {
    friendship.delete();
  }
}
