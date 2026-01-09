package edu.ezip.ing1.pds.backend.config;

import edu.ezip.ing1.pds.backend.MainBackendServer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ChatServerConfiguration {
    private final int listeningPort;

    public ChatServerConfiguration(int listeningPort) {
        this.listeningPort = listeningPort;
    }

    public int getListeningPort() {
        return listeningPort;
    }

    @Override
    public String toString() {
        return "ChatServerConfiguration{" +
                "listeningPort=" + listeningPort +
                '}';
    }

    public static ChatServerConfiguration loadProperties() throws IOException {
        Properties backendProperties = new Properties();

        try (InputStream input = MainBackendServer.class.getClassLoader().getResourceAsStream("chat-backend-server.properties")) {
            backendProperties.load(input);
        }

        return new ChatServerConfiguration(
                Integer.parseInt(backendProperties.getProperty("listeningPort"))
        );
    }
}
