package barnes.chess.db.entity;

import barnes.chess.db.DB;
import barnes.chess.db.stats.FriendshipInfo;
import barnes.chess.utils.ErrorAcceptedConsumer;
import barnes.chess.utils.ErrorAcceptedRunnable;
import barnes.chess.utils.ThreadUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class Friendship extends DurationHolder {
  private int user1, user2;

  public Friendship(int user1, int user2, ErrorAcceptedRunnable onFinish) {
    this.user1 = user1;
    this.user2 = user2;
    Duration dur = new Duration(new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
    dur.insert(() -> {
      duration = dur.id;
      ThreadUtil.async(() -> insert(onFinish));
    });
  }

  public static void getAll(int user, ErrorAcceptedConsumer<List<FriendshipInfo>> consumer) {
    DB.getInstance().query((rs) -> {
      List<FriendshipInfo> friends = new ArrayList<>();
      while (rs.next())
        friends.add(new FriendshipInfo(rs));
      rs.close();
      DB.getInstance().query((rs2) -> {
        while (rs2.next())
          friends.add(new FriendshipInfo(rs2));
        rs2.close();
        ThreadUtil.ui(() -> consumer.accept(friends));
      }, "SELECT Friendship.id, Friendship.user1," +
              "        Friendship.user2, Friendship.duration," +
              "        UserProfile.nick," +
              "        Duration.start_time, Duration.end_time" +
              " FROM Friendship" +
              " INNER JOIN Duration ON Friendship.duration=Duration.id" +
              " INNER JOIN UserProfile ON UserProfile.id=Friendship.user1" +
              " WHERE Friendship.user2=?" +
              " ORDER BY Duration.start_time", user);
    }, "SELECT Friendship.id, Friendship.user1," +
            "        Friendship.user2, Friendship.duration," +
            "        UserProfile.nick," +
            "        Duration.start_time, Duration.end_time" +
            " FROM Friendship" +
            " INNER JOIN Duration ON Friendship.duration=Duration.id" +
            " INNER JOIN UserProfile ON UserProfile.id=Friendship.user2" +
            " WHERE Friendship.user1=?" +
            " ORDER BY Duration.start_time", user);
  }
}
