package dev.guisantos03.webserver;

import dev.guisantos03.webserver.configuration.ConfigurationManager;

/**
 *
 * Driver Class for the Http Server
 *
 */
public class WebServer {
    public static void main(String[] args) {
        System.out.println("Server starting");

        ConfigurationManager.getInstance().loadConfiguration("src/main/resources/config.json");
    }
}
