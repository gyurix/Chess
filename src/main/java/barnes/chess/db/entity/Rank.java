package barnes.chess.db.entity;

import barnes.chess.db.DB;
import barnes.chess.utils.ThreadUtil;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Getter
public class Rank extends AbstractEntity {
  String name;

  public static void withRanks(Consumer<List<Rank>> ranks) {
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
