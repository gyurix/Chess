package barnes.chess.db.stats;

import barnes.chess.db.DB;
import barnes.chess.db.entity.SkipField;
import barnes.chess.utils.ErrorAcceptedConsumer;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserElement {
  private String id;
  private String name;
  private String rank;
  @SkipField
  private int rankId;
  private int tDraws;
  private int tGames;
  private int tLoses;
  private int tWins;

  public void updateRank(int newRank, ErrorAcceptedConsumer<Integer> resultHandler) {
    DB.getInstance().update(resultHandler, "UPDATE UserProfile SET rank = ? WHERE id = ?", newRank, Integer.valueOf(id.trim()));
  }
}
