package barnes.chess.db.entity;

import lombok.Getter;

@Getter
public class LeaguePlayer extends DurationHolder {
  private int league;
  private int player;
  private WinnerType winner;
}
