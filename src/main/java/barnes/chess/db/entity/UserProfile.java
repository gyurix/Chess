package barnes.chess.db.entity;

import java.sql.Timestamp;

public class UserProfile extends AbstractEntity {
  private String first_name;
  private String last_name;
  private String nick;
  private String pwd_hash;
  private int rank;
  private Timestamp registered;
}
