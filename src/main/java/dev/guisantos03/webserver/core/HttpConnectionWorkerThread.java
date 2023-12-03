package dev.guisantos03.webserver.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class HttpConnectionWorkerThread extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpConnectionWorkerThread.class);
    private final Socket socket;
    private final String webRoot;

    public HttpConnectionWorkerThread(Socket socket, String webRoot) {
        this.socket = socket;
        this.webRoot = webRoot;
    }

    /**
     * This method handles the incoming request and sends the appropriate response.
     */
    @Override
    public void run() {
        try (InputStream _ = socket.getInputStream(); OutputStream outputStream = socket.getOutputStream()) {

            File file = new File(webRoot, "index.html");
            byte[] bytes = Files.readAllBytes(file.toPath());
            String body = new String(bytes, StandardCharsets.UTF_8);
            final String CRLF = "\r\n";

            String response = "HTTP/1.1 200 OK" +
                              CRLF +
                              "Content-Length: " + bytes.length +
                              CRLF +
                              CRLF +
                              body +
                              CRLF + CRLF;

            outputStream.write(response.getBytes());

            LOGGER.info("* Connection Processing Finished.");
        } catch (IOException e) {
            LOGGER.error("Problem with communication");
        } finally {
            closeSocket(socket);
        }
    }

    private void closeSocket(Socket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
