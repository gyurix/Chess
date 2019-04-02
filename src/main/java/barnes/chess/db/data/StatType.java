package barnes.chess.db.data;

import barnes.chess.db.entity.Game;

import java.util.List;

import static barnes.chess.utils.FormattingUtils.toCamelCase;

public enum StatType {
  PLAYED_GAMES, WINS, LOSES, DRAWS, WLR {
    @Override
    public String toString() {
      return "Win / Lose Ratio";
    }
  }, WGR {
    @Override
    public String toString() {
      return "Win / Games Ratio";
    }
  };

  public abstract double get(int userId, List<Game> games);

  @Override
  public String toString() {
    return toCamelCase(name());
  }
}
