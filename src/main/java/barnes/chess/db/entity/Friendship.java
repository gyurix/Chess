package barnes.chess.db.entity;

import lombok.Getter;

@Getter
public class Friendship extends DurationHolder {
  private int user1, user2;
}
