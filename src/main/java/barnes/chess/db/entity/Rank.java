package barnes.chess.db.entity;

import barnes.chess.db.DB;
import barnes.chess.utils.ErrorAcceptedConsumer;
import barnes.chess.utils.ThreadUtil;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
public class Rank extends AbstractEntity {
  private String name;

  public static void withRanks(ErrorAcceptedConsumer<List<Rank>> ranks) {
    DB.getInstance().query((rs) -> {
      List<Rank> out = new ArrayList<>();
      while (rs.next()) {
        Rank rank = new Rank();
        rank.load(rs);
        out.add(rank);
      }
      ThreadUtil.ui(() -> ranks.accept(out));
    }, "SELECT * FROM rank");
  }
}
