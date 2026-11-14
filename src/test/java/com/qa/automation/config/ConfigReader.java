package com.qa.automation.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

/**
 * Reads config.properties once. A JVM system property (-Dkey=value) or an
 * environment variable (KEY_NAME) wins over the file, so CI can change
 * settings without editing code.
 */
public final class ConfigReader {

    private static final Properties PROPERTIES = load();

    private ConfigReader() {
    }

    private static Properties load() {
        Properties properties = new Properties();
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IllegalStateException("config.properties not found on the test classpath");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Could not read config.properties", e);
        }
        return properties;
    }

    /** Returns the value, or empty when it is missing or blank everywhere. */
    public static Optional<String> find(String key) {
        String fromSystem = System.getProperty(key);
        if (fromSystem != null && !fromSystem.isBlank()) {
            return Optional.of(fromSystem.trim());
        }
        String fromEnv = System.getenv(key.toUpperCase().replace('.', '_'));
        if (fromEnv != null && !fromEnv.isBlank()) {
            return Optional.of(fromEnv.trim());
        }
        String value = PROPERTIES.getProperty(key);
        return value == null || value.isBlank() ? Optional.empty() : Optional.of(value.trim());
    }

    public static String get(String key) {
        return find(key).orElseThrow(() -> new IllegalArgumentException("Missing config key: " + key));
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static boolean getBoolean(String key) {
        return find(key).map(Boolean::parseBoolean).orElse(false);
    }

    public static String internetUrl() {
        return get("internet.url");
    }

    public static String sauceDemoUrl() {
        return get("saucedemo.url");
    }
}
