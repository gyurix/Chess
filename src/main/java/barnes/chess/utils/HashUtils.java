package barnes.chess.utils;

import lombok.Getter;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.nio.charset.StandardCharsets.UTF_8;


public final class HashUtils {
  public static String hash(String msg, HashType type) {
    return toHex(type.getDigest().digest(msg.getBytes(UTF_8)));
  }

  public static String toHex(byte[] data) {
    return DatatypeConverter.printHexBinary(data);
  }

  public enum HashType {
    MD5,
    SHA_256;
    @Getter
    private MessageDigest digest;

    HashType() {
      String algo = toString();
      try {
        digest = MessageDigest.getInstance(algo);
      } catch (NoSuchAlgorithmException ignored) {
      }
    }

    @Override
    public String toString() {
      return name().replace("_", "-");
    }
  }
}
