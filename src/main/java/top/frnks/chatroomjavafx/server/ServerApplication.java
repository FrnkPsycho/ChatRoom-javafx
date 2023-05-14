package top.frnks.chatroomjavafx.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class ServerApplication {
//    public static final String LOOPBACK_ADDRESS = "localhost";
    public static final int SERVER_PORT = Integer.parseInt(ServerProperties.getProperty("serverPort"));
    public static final Logger LOGGER = Logger.getGlobal();
    public static void main(String[] args) throws IOException {
        try ( ServerSocket serverSocket = new ServerSocket(SERVER_PORT) ) {
            ServerDataBuffer.serverSocket = serverSocket;
            LOGGER.info("ChatRoom Server Start Listening on Port " + SERVER_PORT);
//            while (true) {
//                Socket clientSocket = serverSocket.accept();
//                LOGGER.info("Received Connection from " + clientSocket.getRemoteSocketAddress());
//                Thread thread = new ServerTcpHandler(clientSocket);
//                thread.start();
//            }
            while ( true ) {
                Socket clientSocket = serverSocket.accept();
                LOGGER.info("Received Connection from " + clientSocket.getRemoteSocketAddress());
                Thread t = new Thread(new RequestProcessor(clientSocket));
                t.start();
            }
        }
    }
}
