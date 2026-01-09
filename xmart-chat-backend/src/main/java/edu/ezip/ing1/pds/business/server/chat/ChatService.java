package edu.ezip.ing1.pds.business.server.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ezip.ing1.pds.backend.request.Request;
import edu.ezip.ing1.pds.backend.request.Response;
import edu.ezip.ing1.pds.business.dto.AskHello;
import edu.ezip.ing1.pds.business.dto.HelloAnswer;
import edu.ezip.ing1.pds.business.dto.Shutdown;

import java.io.IOException;

public class ChatService {

    private enum ChatServiceRequest {
        HELLO,
        SHUTDOWN
    }

    public ChatService() {

    }

    public Response handleChatRequest(Request request, String serviceRequest) throws IOException {
        Response response = null;
        ChatServiceRequest chatServiceRequest = ChatServiceRequest.valueOf(serviceRequest);
        switch(chatServiceRequest) {
            case HELLO:
                response = helloRequest(request);
                break;
            case SHUTDOWN:
                response = shutdownRequest(request);
                break;
            default:
                break;
        }
        return response;
    }

    private Response helloRequest(Request request) throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final AskHello hello = objectMapper.readValue(request.getRequestBody(), AskHello.class);
        String answer = String.format("Hello %s you sent this message : %s", hello.name, hello.body);
        final HelloAnswer helloAnswer = new HelloAnswer();
        helloAnswer.setMessage(answer);
        return new Response(request.getRequestId(), objectMapper.writeValueAsString(helloAnswer));
    }

    private Response shutdownRequest(Request request) throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Shutdown shutdown = objectMapper.readValue(request.getRequestBody(), Shutdown.class);
        String answer = String.format("SHUTDOWN THE SERVER because of : %s", shutdown.getReason());
        final Shutdown shutdownAnswer = new Shutdown();
        shutdownAnswer.setReason(answer);
        return new Response(request.getRequestId(), objectMapper.writeValueAsString(shutdownAnswer));
    }

}
