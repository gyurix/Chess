package barnes.chess.db.stats;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserElement {
  private int id;
  private String name;
  private String role;
  private int roleId;
}
