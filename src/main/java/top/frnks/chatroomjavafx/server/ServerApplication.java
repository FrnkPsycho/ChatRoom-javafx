package top.frnks.chatroomjavafx.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class ServerApplication {
//    public static final String LOOPBACK_ADDRESS = "localhost";
    public static final int SERVER_PORT = 6657;
    public static final Logger LOGGER = Logger.getGlobal();
    public static void main(String[] args) throws IOException {
        try ( ServerSocket serverSocket = new ServerSocket(SERVER_PORT) ) {
            LOGGER.info("ChatRoom Server Start Listening on Port " + SERVER_PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                LOGGER.info("Received Connection from " + clientSocket.getRemoteSocketAddress());
                Thread thread = new ServerTcpHandler(clientSocket);
                thread.start();
            }
        }
    }
}
