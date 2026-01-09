package edu.ezip.ing1.pds.backend;

import edu.ezip.ing1.pds.backend.config.ChatServerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.SQLException;


public class ChatBackendServer
{
    private final Logger logger = LoggerFactory.getLogger(ChatBackendServer.class);

    private final ChatServerConfiguration config;
    private final ServerSocket serverSocket;

    public ChatBackendServer() throws IOException, SQLException {
        config = ChatServerConfiguration.loadProperties();
        serverSocket = new ServerSocket(config.getListeningPort());
        logger.debug("Configuration loaded : {}", serverSocket.toString());
    }

    public void start() {
        boolean stop = false;
        while (!stop) {
            try {
                final Socket accept = serverSocket.accept();

                final RequestHandler requestHandler = new RequestHandler(
                                accept,this);
                requestHandler.handle();
            }
            catch (SocketTimeoutException es) {
                logger.trace("Timeout on accept");
                stop = true;
            }
            catch (IOException e) {
                logger.error("There is I/O mess here : exception tells", e);
                stop = true;
            }
        }
        logger.debug("Main Thread in Core Backend Server is terminated") ;
    }
}
