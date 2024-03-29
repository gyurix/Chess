package barnes.chess.db.stats;

import barnes.chess.db.entity.Game;
import barnes.chess.db.entity.WinnerType;

import java.util.List;

import static barnes.chess.utils.FormattingUtils.toCamelCase;

public enum StatType {
  PLAYED_GAMES {
    @Override
    public double get(int userId, List<Game> games) {
      return games.size();
    }
  }, WINS {
    @Override
    public double get(int userId, List<Game> games) {
      int countWins = 0;
      for (Game g : games) {
        if (g.getWinner() == WinnerType.P1 && g.getPlayer1() == userId || g.getWinner() == WinnerType.P2 && g.getPlayer2() == userId) {
          countWins++;
        }
      }
      return countWins;
    }
  }, LOSES {
    @Override
    public double get(int userId, List<Game> games) {
      int countLoses = 0;
      for (Game g : games) {
        if (g.getWinner() == WinnerType.P1 && g.getPlayer1() != userId || g.getWinner() == WinnerType.P2 && g.getPlayer2() != userId) {
          countLoses++;
        }
      }
      return countLoses;
    }
  }, DRAWS {
    @Override
    public double get(int userId, List<Game> games) {
      int countDraws = 0;
      for (Game g : games) {
        if (g.getWinner() == WinnerType.DRAW) {
          countDraws++;
        }
      }
      return countDraws;
    }
  }, WLR {
    @Override
    public double get(int userId, List<Game> games) {
      return StatType.fixDouble(StatType.WINS.get(userId, games) / StatType.LOSES.get(userId, games));
    }

    @Override
    public String toString() {
      return "Win / Lose Ratio";
    }
  }, WGR {
    @Override
    public double get(int userId, List<Game> games) {
      return StatType.fixDouble(StatType.WINS.get(userId, games) / games.size());
    }

    @Override
    public String toString() {
      return "Win / Games Ratio";
    }
  }/*NAME{
        public String nick(int userId, List<UserProfile> users){
            for(UserProfile u : users){
                if(userId == u.getId()){
                    return u.getNick();
                }
            }
            return "N/A";
        }
        @Override
        public double get(int userId, List<Game> games){
            return 0;
        }
    }*/;

  private static double fixDouble(double d) {
    if (Double.isFinite(d))
      return ((int) d * 100) / 100.0;
    return d;
  }

  public abstract double get(int userId, List<Game> games);

  @Override
  public String toString() {
    return toCamelCase(name());
  }
}
