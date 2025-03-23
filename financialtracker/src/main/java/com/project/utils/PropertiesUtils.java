package com.project.utils;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.util.Properties;

@UtilityClass
public final class PropertiesUtils {

    private static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    private static void loadProperties() {
        try (var inputStream = PropertiesUtils.class.getClassLoader().getResourceAsStream("config.properties")) {
            PROPERTIES.load(inputStream);
            if (PROPERTIES.getProperty("db.profile").equals("dev")) {
                PROPERTIES.load(PropertiesUtils.class.getClassLoader().getResourceAsStream("config-dev.properties"));
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static void set(String key, String value) {
        PROPERTIES.setProperty(key, value);
    }

}
