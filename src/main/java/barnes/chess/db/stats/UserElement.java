package barnes.chess.db.stats;

import barnes.chess.db.DB;
import barnes.chess.db.entity.SkipField;
import barnes.chess.utils.ErrorAcceptedConsumer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserElement {
  private int id;
  private String name;
  private String rank;
  @SkipField
  private int rankId;

  public void updateRank(int newRank, ErrorAcceptedConsumer<Integer> resultHandler) {
    DB.getInstance().update(resultHandler, "UPDATE UserProfile SET rank = ? WHERE id = ?", newRank, id);
  }
}
