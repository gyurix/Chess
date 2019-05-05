package barnes.chess.db.entity;

import barnes.chess.db.DB;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class Friendship extends DurationHolder {
  private int user1, user2;
  private String friendName;
  //private int duration;

  public Friendship(int id){ this.id = id; }
  public Friendship(String friendName){ this.friendName = friendName; }

  public Friendship(int user1, int user2, String friendName){
    this.user1 = user1;
    this.user2 = user2;
    this.friendName = friendName;
  }


  /*public static void getFriends(int userId){
    DB.getInstance().query((rs) -> {
      List<Friendship> friends = new ArrayList<>();
      while (rs.next()) {
        Friendship friend = new Friendship();

      }
    }), "SELECT frienship.user2, userprofile.id, userprofile.nick FROM userprofile\n" +
            "INNER JOIN friendship ON userprofile.id = friendship.user1\n" +
            "WHERE friendship.user2 = ?", userId;

  }*/
}


