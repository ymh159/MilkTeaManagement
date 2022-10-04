package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class ReadFileProperties {
  public static Properties read() {
    String path = Constants.PATH_PROPERTIES;
    Properties properties = new Properties();
    try {
    InputStream inputStream = new FileInputStream(path);
      properties.load(inputStream);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return properties;
  }
}
