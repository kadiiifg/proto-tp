package edu.ezip.ing1.pds;

import edu.ezip.ing1.pds.config.ChatCliConfig;
import edu.ezip.ing1.pds.service.ChatService;

import java.io.IOException;

public class MainChatCli {
    public enum ChatServiceRequest {
        HELLO,
        SHUTDOWN
    }

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            printUsage();
            return;
        }

        ChatService chatService = new ChatService();

        String cmd = args[0];
        try {
            ChatServiceRequest serviceRequest = ChatServiceRequest.valueOf(cmd.toUpperCase());
            switch(serviceRequest) {
                case HELLO:
                    if (args.length != 3) {
                        printUsage();
                        return;
                    }
                    chatService.sendHello(args[1], args[2]);
                    break;
                case SHUTDOWN:
                    if (args.length != 2) {
                        printUsage();
                        return;
                    }
                    chatService.sendShutdown(args[1]);
                    break;
            }
        }catch(IllegalArgumentException e) {
            printUsage();
        }
    }

    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("xmart-chat-cli.jar hello <name> <message>");
        System.out.println("xmart-chat-cli.jar shutdown <reason>");
    }
}
