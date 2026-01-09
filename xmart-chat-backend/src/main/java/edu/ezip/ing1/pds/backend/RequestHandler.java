package edu.ezip.ing1.pds.backend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.ezip.ing1.pds.backend.request.Request;
import edu.ezip.ing1.pds.backend.request.Response;
import edu.ezip.ing1.pds.business.server.RequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class RequestHandler {
    private final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket socket;
    private final ChatBackendServer parent;
    private final RequestService requestService;

    protected RequestHandler(final Socket socket,
                             final ChatBackendServer parent) throws IOException {
        this.socket = socket;
        this.parent = parent;
        this.requestService = RequestService.getInstance();
    }

    public void handle() throws IOException {
        try {
            BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            String clientRequest = is.readLine();
            logger.info("New request from " + socket.getInetAddress().getHostAddress() + " : " + clientRequest);
            Request request = getRequest(clientRequest);
            Response response = requestService.handleRequest(request);

            logger.info("Sending response to " + socket.getInetAddress().getHostAddress() + " : " + response);
            String responseStr = getResponse(response);
            out.println(responseStr);
            out.flush();

            logger.info("Response sent to " + socket.getInetAddress().getHostAddress());
        } catch (IOException e) {
            logger.error("There is I/O mess here : exception tells {}", e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            socket.close();
        }
    }

    private Request getRequest(String requestData) throws IOException, InterruptedException {
        final ObjectMapper mapper = new ObjectMapper();
        Thread.sleep(10000);
        final Request request = mapper.readValue(requestData, Request.class);
        logger.debug(request.toString());
        return request;
    }

    private String getResponse(final Response response) throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(response);
    }
}
