package top.frnks.chatroomjavafx.client.util;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import top.frnks.chatroomjavafx.client.ClientApplication;
import top.frnks.chatroomjavafx.client.ClientChatRoomTab;
import top.frnks.chatroomjavafx.client.ClientDataBuffer;
import top.frnks.chatroomjavafx.client.ClientPrivateTab;
import top.frnks.chatroomjavafx.common.model.entity.ActionType;
import top.frnks.chatroomjavafx.common.model.entity.Request;
import top.frnks.chatroomjavafx.common.model.entity.Response;
import top.frnks.chatroomjavafx.common.model.entity.User;
import top.frnks.chatroomjavafx.common.util.TranslatableString;

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
     *
     * @deprecated due to some stream issue, we don't recommend read response immediately after sending request.
     * We recommend listening to response in a dedicated thread.
     */
    @Deprecated
    public static Response sendRequestWithResponse(Request request) throws IOException {
        Response response = null;

        try {
            ClientDataBuffer.objectOutputStream.writeObject(request);
            ClientDataBuffer.objectOutputStream.flush();
            LOGGER.info("Client sent request: " + request.getAction());

            if ( request.getAction().equals(ActionType.EXIT) ) {
                LOGGER.info("Client closed connection.");
            } else {
//                Thread.sleep(500);
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

    public static void popAlert(Alert.AlertType alertType, String i18nIdentifier) {
        Platform.runLater(() -> {
            Alert alert = new Alert(alertType, new TranslatableString(i18nIdentifier).translate());
            alert.show();
        });
    }

    public static void switchToPrivateChat(User target) {
        for ( var tab : ClientPrivateTab.privateChatUsersTabPane.getTabs() ) {
            if ( target.getNickname().equals(tab.getText()) ) {
                ClientApplication.mainTabsRoot.getSelectionModel().select(tab);
                ClientApplication.mainTabsRoot.getSelectionModel().select(1);
                return;
            }
        }
        Tab newTab = ClientPrivateTab.addPrivateChatTab(target);
        ClientPrivateTab.privateChatUsersTabPane.getSelectionModel().select(newTab);
        ClientApplication.mainTabsRoot.getSelectionModel().select(1);
    }


//    public static void appendTextToMessageArea(String text, User user) {
//        ClientChatRoomTab.chatRoomMessageArea.setText(ClientChatRoomTab.chatRoomMessageArea.getText() + "\n" + text);
//
//    }
}
