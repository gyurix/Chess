package barnes.chess.utils;

import java.util.function.Function;

public interface ErrorAcceptedFunction<X, Y> {
  Y apply(X t) throws Throwable;

  default Function<X, Y> toFunction() {
    return (t) -> {
      try {
        return apply(t);
      } catch (Throwable e) {
        e.printStackTrace();
      }
      return null;
    };
  }
}
