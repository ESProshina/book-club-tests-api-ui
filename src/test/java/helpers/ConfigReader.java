package helpers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final Properties PROPERTIES = new Properties();
    private static final String CONFIG_FILE = "config.properties";

    static {
        try (InputStream is = ConfigReader.class.getClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {
            if (is != null) {
                PROPERTIES.load(is);
            }
        } catch (IOException e) {
            throw new RuntimeException("Не удалось загрузить " + CONFIG_FILE, e);
        }
    }

    private ConfigReader() {
    }

    public static String get(String key, String defaultValue) {

        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue;
        }
        // 2. Значение из config.properties
        String fileValue = PROPERTIES.getProperty(key);
        if (fileValue != null && !fileValue.isBlank()) {
            return fileValue;
        }
        // 3. Значение по умолчанию
        return defaultValue;
    }

    public static String getBaseUrl() {
        return get("base.url", "https://book-club.qa.guru");
    }

    public static String getBasePath() {
        return get("base.path", "/api/v1");
    }
}
