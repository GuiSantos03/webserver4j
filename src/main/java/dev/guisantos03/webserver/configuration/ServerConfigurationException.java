package dev.guisantos03.webserver.configuration;

public class ServerConfigurationException extends RuntimeException {

    public ServerConfigurationException() {}

    public ServerConfigurationException(String message) {
        super(message);
    }

    public ServerConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerConfigurationException(Throwable cause) {
        super(cause);
    }
}
