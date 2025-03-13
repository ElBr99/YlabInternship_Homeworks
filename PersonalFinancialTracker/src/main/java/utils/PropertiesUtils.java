package utils;
import java.io.IOException;
import java.util.Properties;


public final class PropertiesUtils {

    private static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    public PropertiesUtils() {
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    private static void loadProperties() {
        try (var inputStream = Properties.class.getClassLoader().getResourceAsStream("config.properties")) {
            PROPERTIES.load(inputStream);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

}
