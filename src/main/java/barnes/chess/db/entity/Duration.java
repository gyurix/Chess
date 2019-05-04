package barnes.chess.db.entity;

import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class Duration extends AbstractEntity {
  Timestamp end_time;
  Timestamp start_time;

  public Duration(Timestamp start_time, Timestamp end_time) {
    this.end_time = end_time;
    this.start_time = start_time;
  }
}
