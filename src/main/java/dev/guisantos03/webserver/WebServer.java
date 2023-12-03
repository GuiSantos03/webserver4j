package dev.guisantos03.webserver;

import dev.guisantos03.webserver.configuration.Configuration;
import dev.guisantos03.webserver.configuration.ConfigurationManager;
import dev.guisantos03.webserver.core.ServerListenerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Driver Class for the Http Server
 */
public class WebServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebServer.class);

    public static void main(String[] args) {
        LOGGER.info("Server starting...");
        ConfigurationManager.getInstance().loadConfiguration("src/main/resources/config.json");
        Configuration config = ConfigurationManager.getInstance().getCurrentConfiguration();
        LOGGER.info("Listening for connections on port: {}", config.getServer().getPort());
        LOGGER.info("WebRoot files in: {}", config.getServer().getWebRoot());

        try {
            ServerListenerThread serverListenerThread = new ServerListenerThread(config.getServer().getPort(), config.getServer().getWebRoot());
            serverListenerThread.start();
        } catch (IOException e) {
            LOGGER.error("An error occurred while starting the server", e);
        }
    }
}
