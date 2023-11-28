package dev.guisantos03.webserver.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import dev.guisantos03.webserver.util.Json;

import java.io.FileReader;
import java.io.IOException;

/**
 * Singleton that manages the config.json file
 */
public class ConfigurationManager {

    private static ConfigurationManager configurationManagerInstance;
    private static Configuration currentConfiguration;

    private ConfigurationManager() {
    }

    public static ConfigurationManager getInstance() {
        if (configurationManagerInstance == null) configurationManagerInstance = new ConfigurationManager();
        return configurationManagerInstance;
    }

    public void loadConfiguration(String filePath) {
        StringBuilder sb = new StringBuilder();
        JsonNode config;

        try (FileReader fr = new FileReader(filePath)) {
            int i;
            while ((i = fr.read()) != -1) sb.append((char) i);
        } catch (IOException e) {
            throw new ServerConfigurationException(e);
        }

        try {
            config = Json.parse(sb.toString());
        } catch (IOException e) {
            throw new ServerConfigurationException("Error parsing the configuration file", e);
        }

        try {
            currentConfiguration = Json.fromJson(config, Configuration.class);
        } catch (JsonProcessingException e) {
            throw new ServerConfigurationException("Error parsing the configuration file, internal", e);
        }
    }

    /**
     * Returns the current loaded configuration
     */
    public Configuration getCurrentConfiguration() {
        if (currentConfiguration == null) {
            throw new ServerConfigurationException("No current configuration set.");
        }
        return currentConfiguration;
    }
}
