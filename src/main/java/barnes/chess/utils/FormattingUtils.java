package barnes.chess.utils;

public class FormattingUtils {
  public static String toCamelCase(String name) {
    StringBuilder sb = new StringBuilder();
    for (String s : name.split("[ _]")) {
      if (s.isEmpty())
        continue;
      sb.append(' ').append(Character.toUpperCase(s.charAt(0))).append(s.substring(1).toLowerCase());
    }
    return sb.length() == 0 ? sb.toString() : sb.substring(1);
  }
}
