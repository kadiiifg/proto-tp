package edu.ezip.ing1.pds.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ezip.ing1.pds.business.dto.Shutdown;
import edu.ezip.ing1.pds.request.Request;
import edu.ezip.ing1.pds.request.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.ezip.ing1.pds.business.dto.AskHello;
import edu.ezip.ing1.pds.business.dto.HelloAnswer;
import edu.ezip.ing1.pds.config.ChatCliConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

public class ChatService {
    private final Logger logger = LoggerFactory.getLogger(ChatService.class);

    private final ChatCliConfig chatCliConfig;
    private Socket socket;

    public ChatService() throws IOException {
        this.chatCliConfig = ChatCliConfig.loadProperties();
    }

    private void connectToServer() throws IOException {
        socket = new Socket(chatCliConfig.getChatServerHost(), chatCliConfig.getChatServerPort());
    }

    private <T> Response sendCommand(String requestOrder, T content) throws IOException {
        PrintWriter writer = new PrintWriter(socket.getOutputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        ObjectMapper objectMapper = new ObjectMapper();

        Request request = new Request();
        request.setRequestId(String.valueOf(UUID.randomUUID()));
        request.setRequestOrder(requestOrder);
        request.setRequestContent(objectMapper.writeValueAsString(content));

        writer.println(objectMapper.writeValueAsString(request));
        writer.flush();
        String responseStr = reader.readLine();
        Response response = objectMapper.readValue(responseStr, Response.class);
        return response;
    }

    private void closeSocket() throws IOException {
        socket.close();
    }

    public void sendHello(String name, String message) throws IOException {
        connectToServer();

        ObjectMapper objectMapper = new ObjectMapper();

        AskHello askHello = new AskHello();
        askHello.setName(name);
        askHello.setBody(message);

        Response response = sendCommand("CHAT.HELLO", askHello);
        HelloAnswer helloAnswer = objectMapper.readValue(response.responseBody, HelloAnswer.class);

        logger.info("Hello answer from server: {}", helloAnswer.getMessage());

        closeSocket();
    }

    public void sendShutdown(String reason) throws IOException {
        connectToServer();

        ObjectMapper objectMapper = new ObjectMapper();

        Shutdown shutdown = new Shutdown();
        shutdown.setReason(reason);

        Response response = sendCommand("CHAT.SHUTDOWN", shutdown);
        Shutdown shutdownAnswer = objectMapper.readValue(response.responseBody, Shutdown.class);
        logger.info("Response for shutdown : {}", shutdownAnswer.reason);

        closeSocket();
    }
}
