package com.cucumberconstants;


import com.cucumberconstants.LoggerUtil;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationReader {

    private static Properties properties;
    private static Logger log = LoggerUtil.getLogger(ConfigurationReader.class);
    private static String environment;

    public static void setEnvironment(String environment) {
        ConfigurationReader.environment = environment;
    }

    static {
        try {
            String path = "src/test/resources/config.properties";
            FileInputStream input = new FileInputStream(path);
            properties = new Properties();
            properties.load(input);
            input.close();
            log.info("config.properties loaded successfully from: {}", path);
        } catch (IOException e) {
            log.error("Error loading config.properties file: {}", e.getMessage());
            throw new RuntimeException("Failed to load config.properties file.", e);
        }
    }

    /**
     * Retrieves a property value from the config.properties file.
     * @param key The key of the property.
     * @return The value of the property.
     */
    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            log.warn("Property '{}' not found in config.properties.", key);
        }
        return value;
    }

    /**
     * Gets an environment-specific property.
     * Example: getEnvProperty("base.url") will return "qa.base.url" if environment is "qa".
     *
     * @param key The key of the property (e.g., "base.url").
     * @return The environment-specific value.
     */
    public static String getEnvProperty(String key) {
//        String environment = getProperty("environment");
        if (environment == null || environment.isEmpty()) {
            log.error("Environment property is not set. Please set 'environment' in config.properties or via command line.");
            throw new IllegalStateException("Environment property is not set.");
        }
        log.info("Environment property: {}", environment);
        String envSpecificKey = environment + "." + key;
        String value = getProperty(envSpecificKey);
        if (value == null) {
            log.warn("Environment specific property '" + envSpecificKey + "' not found.");
        }
        return value;
    }
}
