package barnes.chess.db.stats;

import barnes.chess.db.entity.Game;
import lombok.Getter;

import java.util.List;

@Getter
public class StatElement {
  private StatType stat;
  private Object value;

  public StatElement(StatType stat, int userId, List<Game> games) {
    double dValue = stat.get(userId, games);
    this.stat = stat;
    value = dValue == (int) dValue ? (int) dValue : value;
  }
}
