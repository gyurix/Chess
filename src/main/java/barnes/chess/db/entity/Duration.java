package barnes.chess.db.entity;

import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class Duration extends AbstractEntity {
  Timestamp end_time;
  Timestamp start_time;
}
