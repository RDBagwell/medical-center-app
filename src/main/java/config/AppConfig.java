package config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class AppConfig {
    private static final Properties props = new Properties();

    static {
        Path envFile = Path.of(".env");

        if (Files.exists(envFile)) {
            try (var reader = Files.newBufferedReader(envFile)) {
                props.load(reader);
            } catch (IOException e) {
                throw new IllegalStateException("Could not read .env configuration file.", e);
            }
        }
    }

    public static final String DB_URL = require("DB_URL");
    public static final String MYSQL_USER = require("MYSQL_USER");
    public static final String MYSQL_PASSWORD = require("MYSQL_PASSWORD");

    private static String require(String key) {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            value = props.getProperty(key);
        }
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(key + " is not set. Configure it in .env or your environment.");
        }
        return value.trim();
    }

    private AppConfig() {
    }
}

