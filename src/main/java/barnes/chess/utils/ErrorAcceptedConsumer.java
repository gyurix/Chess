package barnes.chess.utils;

public interface ErrorAcceptedConsumer<T> {
  void accept(T t) throws Throwable;
}
