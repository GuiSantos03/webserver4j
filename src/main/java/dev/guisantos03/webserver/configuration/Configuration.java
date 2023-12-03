package dev.guisantos03.webserver.configuration;

public class Configuration {

    private Database database;
    private Server server;

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public static class Database {
        private String host;
        private int port;
        private String name;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Server {
        private int port;
        private String webRoot;

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getWebRoot() {
            return webRoot;
        }

        public void setWebRoot(String webRoot) {
            this.webRoot = webRoot;
        }
    }
}

