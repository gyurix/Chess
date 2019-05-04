package barnes.chess.db.entity;

public enum WinnerType {
  DRAW, P1, P2;

  public WinnerType inverse() {
    return this == P1 ? P2 : this == P2 ? P1 : this;
  }
}
