package edu.ezip.ing1.pds.config;

import edu.ezip.ing1.pds.MainChatCli;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ChatCliConfig {
    private final int chatServerPort;
    private final String chatServerHost;

    public ChatCliConfig(int chatServerPort, String chatServerHost) {
        this.chatServerPort = chatServerPort;
        this.chatServerHost = chatServerHost;
    }

    public String getChatServerHost() {
        return chatServerHost;
    }

    public int getChatServerPort() {
        return chatServerPort;
    }

    @Override
    public String toString() {
        return "ChatCliConfig{" +
                "chatServerPort=" + chatServerPort +
                ", chatServerHost='" + chatServerHost + '\'' +
                '}';
    }

    public static ChatCliConfig loadProperties() throws IOException {
        Properties backendProperties = new Properties();

        try (InputStream input = MainChatCli.class.getClassLoader().getResourceAsStream("chat-cli.properties")) {
            backendProperties.load(input);
        }

        return new ChatCliConfig(Integer.parseInt((backendProperties.getProperty("chatServerPort"))),
                backendProperties.getProperty("chatServerHost"));
    }
}
