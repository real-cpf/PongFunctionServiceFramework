package env;


import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

final public class EnvLoader {

  final private String NULL_VALUE = "<null>";

  private EnvLoader() {
    String root = System.getProperty("user.dir");
    try (InputStream ins = Files.newInputStream(Path.of(root).resolve(".env"), StandardOpenOption.READ)) {
      Properties properties = new Properties();
      properties.load(ins);
      properties.forEach((k, v) -> {
        EnvLoaderHolder.prop.put((String) k, (String) v);
      });
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static class EnvLoaderHolder {
    final static Map<String, String> prop = new HashMap<>(4);
    final static EnvLoader HOLDER = new EnvLoader();
  }

  public String getByName(String key) {
    return EnvLoaderHolder.prop.getOrDefault(key, NULL_VALUE);
  }

  public static EnvLoader get() {
    return EnvLoaderHolder.HOLDER;
  }
}
