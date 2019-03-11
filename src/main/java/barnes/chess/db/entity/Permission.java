package barnes.chess.db.entity;

import lombok.Getter;

@Getter
public class Permission extends AbstractEntity {
  private String name;
  private int rank;
}
