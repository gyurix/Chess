package barnes.chess.db.entity;

import lombok.Getter;

@Getter
public class Game extends DurationHolder {
  private int player1, player2, winner;
}
