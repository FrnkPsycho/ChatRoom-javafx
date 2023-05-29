package top.frnks.chatroomjavafx.client;

import top.frnks.chatroomjavafx.client.util.ClientAction;
import top.frnks.chatroomjavafx.client.util.ClientUtil;
import top.frnks.chatroomjavafx.common.model.entity.Message;
import top.frnks.chatroomjavafx.common.model.entity.Response;
import top.frnks.chatroomjavafx.common.model.entity.ResponseType;
import top.frnks.chatroomjavafx.server.ServerProperties;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * ClientThread is for communicating with server and updating GUI contents.
 */
public class ClientThread extends Thread {
    public static final Logger LOGGER = ClientApplication.LOGGER;
    @Override
    public void run() {
        connectToServer(ServerProperties.getProperty("serverIp"), Integer.parseInt(ServerProperties.getProperty("serverPort")));
        LOGGER.info("Started Server Listener Thread");

        try {
            while (ClientDataBuffer.clientSocket.isConnected()) {
                Response response = (Response) ClientDataBuffer.objectInputStream.readObject();
                ResponseType responseType = response.getResponseType();
                LOGGER.info("Received response from server: " + response + " " + responseType);
                // FIXME: 5/29/23 Unable to receive response from server

                switch (responseType) {
                    case BROADCAST -> {
                        Message message = (Message) response.getData("Chat");
                        ClientUtil.appendTextToMessageArea(message.getContent());
                        // TODO: chat records
                        LOGGER.info("Update public board with chat message: " + message.getContent());
                    }
//                    case ALREADY_LOGON, INVALID_LOGIN, LOGIN -> ClientLogin.login();
//                    case SIGNUP ->
                }
            }
        } catch ( IOException e ) {
            LOGGER.warning("IO Error");
            connectToServer(ServerProperties.getProperty("serverIp"), Integer.parseInt(ServerProperties.getProperty("serverPort")));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void connectToServer(String remote, int port) {
        try {
            LOGGER.info("Connecting to " + remote + ":" + port);
            ClientDataBuffer.clientSocket = new Socket(remote, port);
            ClientDataBuffer.objectOutputStream = new ObjectOutputStream(ClientDataBuffer.clientSocket.getOutputStream());
            ClientDataBuffer.objectInputStream = new ObjectInputStream(ClientDataBuffer.clientSocket.getInputStream());
            LOGGER.info("Connected to " + remote + ":" + port);
            ClientUtil.appendTextToMessageArea("Connected to Server: " + remote + ":" + port);
        } catch (IOException e) {
            LOGGER.warning("Unable to connect to server: \"" + remote + ":" + port + "\"");
            ClientUtil.appendTextToMessageArea("Unable to connect to server: \"" + remote + ":" + port + "\"" + ", reconnect in 5 seconds...");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            connectToServer(remote, port); // TODO: don't use recursion!!!
        }
    }
}
