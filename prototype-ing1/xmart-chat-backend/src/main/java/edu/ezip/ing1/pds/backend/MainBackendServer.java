package edu.ezip.ing1.pds.backend;

import java.io.IOException;
import java.sql.SQLException;

public class MainBackendServer {

    public static void main(String[] args) throws IOException, InterruptedException, SQLException {
        final ChatBackendServer chatBackendServer = new ChatBackendServer();
        chatBackendServer.start();
    }

}
