package top.frnks.chatroomjavafx.client.util;

import top.frnks.chatroomjavafx.client.ClientApplication;
import top.frnks.chatroomjavafx.client.ClientChatRoomTab;
import top.frnks.chatroomjavafx.client.ClientDataBuffer;
import top.frnks.chatroomjavafx.common.model.entity.ActionType;
import top.frnks.chatroomjavafx.common.model.entity.Request;
import top.frnks.chatroomjavafx.common.model.entity.Response;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;
import java.util.logging.Logger;

public class ClientUtil {
    private static final Logger LOGGER = ClientApplication.LOGGER;

    /**
     * Send request, but do not receive response.
     */
    public static void sendRequestWithoutResponse(Request request) throws IOException {
        // TODO: initialize data buffer oos
        ClientDataBuffer.objectOutputStream.writeObject(request);
        ClientDataBuffer.objectOutputStream.flush();
        LOGGER.info("Client sent request: " + request.getAction());
    }

    /**
     * Send request, receive response and return.
     */
    public static Response sendRequestWithResponse(Request request) throws IOException {
        Response response = null;

        try {
            ClientDataBuffer.objectOutputStream.writeObject(request);
            ClientDataBuffer.objectOutputStream.flush();
            LOGGER.info("Client sent request: " + request.getAction());

            if ( request.getAction().equals(ActionType.EXIT) ) {
                LOGGER.info("Client closed connection.");
            } else {
                response = (Response) ClientDataBuffer.objectInputStream.readObject();
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static void appendTextToMessageArea(String text) {
        ClientChatRoomTab.chatRoomMessageArea.appendText("\n" + text);

    }

    public static String digestPassword(String rawPassword) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        digest.update(salt);

        byte[] hashedPassword = digest.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
        return HexFormat.of().formatHex(hashedPassword);
    }

//    public static void appendTextToMessageArea(String text, User user) {
//        ClientChatRoomTab.chatRoomMessageArea.setText(ClientChatRoomTab.chatRoomMessageArea.getText() + "\n" + text);
//
//    }
}
