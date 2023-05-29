package top.frnks.chatroomjavafx.client.util;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import top.frnks.chatroomjavafx.client.ClientChatRoomTab;
import top.frnks.chatroomjavafx.client.ClientDataBuffer;
import top.frnks.chatroomjavafx.client.ClientLogin;
import top.frnks.chatroomjavafx.common.model.entity.*;
import top.frnks.chatroomjavafx.common.util.TranslatableString;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClientAction {
//    public static void loginRequest(String id, String nickname, String password) {
//        if ( ClientDataBuffer.clientSocket == null ) {
//            Alert alert = new Alert(Alert.AlertType.ERROR, new TranslatableString("client.login.no_connection").translate());
//            alert.show();
//            return;
//        }
//        if ( (id.isBlank() && nickname.isBlank()) || password.isBlank() ) {
//            Alert alert = new Alert(Alert.AlertType.ERROR, new TranslatableString("client.login.login_missing_fields").translate());
//            alert.show();
//            return;
//        }
//
//        Request request = new Request();
//        request.setAction(ActionType.LOGIN);
//        request.setAttribute("id", id);
//        request.setAttribute("nickname", nickname);
//        request.setAttribute("password", password);
//
////        Response response;
////        try {
////            response = ClientUtil.sendRequestWithResponse(request);
////        } catch (IOException e) {
////            throw new RuntimeException(e);
////        }
//        try {
//            ClientUtil.sendRequestWithoutResponse(request);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//
//    }
//
//    public static void loginResponseHandle(Response response) {
//        User user = (User) response.getData("user");
//        if ( response.getResponseType() == ResponseType.INVALID_LOGIN ) {
//            ClientUtil.popAlert(Alert.AlertType.ERROR, "client.login.login_failed");
//            return;
//        } else if ( response.getResponseType() == ResponseType.ALREADY_LOGON ) {
//            ClientUtil.popAlert(Alert.AlertType.ERROR, "client.login.already_logon");
//        }
//
//        user.setOnline(true);
//        ClientUtil.appendTextToMessageArea("\nHello user: " + user.getNickname() + " <" + user.getId() + ">\n");
//        ClientDataBuffer.currentUser = user;
//        ClientDataBuffer.isLoggedIn = true;
//
//        Platform.runLater(ClientLogin.stage::close);
//    }
    public static void sendMessage() {
        String content = ClientChatRoomTab.chatRoomTypeArea.getText();
        if ( content.isBlank() ) {
            Alert blankMessageAlert = new Alert(Alert.AlertType.ERROR, new TranslatableString("alert.blank_message").translate());
            blankMessageAlert.show();
        } else {
            Message msg = new Message();
//            msg.setContent(content);
            msg.setSendTime(LocalDateTime.now());
            msg.setFromUser(ClientDataBuffer.currentUser);
            msg.setToUser(null);
//            msg.setToUser(); // TODO: if ToUser is null means the message broadcasts.

//            DateFormat df = new SimpleDateFormat("HH:mm:ss"); // TODO: make format compatible with different locale
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String sb = msg.getSendTime().format(dtf) +
                    " " +
                    msg.getFromUser().getNickname() +
                    "<" + msg.getFromUser().getId() + ">" +
                    "\n" + content + "\n";
            msg.setContent(sb);

            Request request = new Request();
            request.setAction(ActionType.CHAT);
            request.setAttribute("msg", msg);
            try {
                ClientUtil.sendRequestWithoutResponse(request);
            } catch (IOException e) {
                e.printStackTrace(); // TODO: proper exception handling
            }

            // TODO: ctrl+enter to send message
//            ClientUtil.appendTextToMessageArea(msg.getContent()); // TODO: debug line
//            ClientChatRoomTab.chatRoomTypeArea.setText("");
        }
    }

    public static void addFriend(User target) {
        if ( target == null ) {
            new Alert(Alert.AlertType.WARNING, new TranslatableString("alert.friend.need_selection").translate()).show();
        } else if ( target.getId() == ClientDataBuffer.currentUser.getId()) {
            new Alert(Alert.AlertType.WARNING, new TranslatableString("alert.friend.invalid_self").translate()).show();
        } else if (ClientDataBuffer.currentUser.getFriendsList().contains(target)) {
            new Alert(Alert.AlertType.WARNING, new TranslatableString("alert.friend.already_friend").translate()).show();
        } else {
            Message msg = new Message();
            msg.setFromUser(ClientDataBuffer.currentUser);
            msg.setToUser(target);
            msg.setSendTime(LocalDateTime.now());
            Request request = new Request();
            request.setAction(ActionType.FRIEND_REQUEST);
            request.setAttribute("msg", msg);
            try {
                ClientUtil.sendRequestWithoutResponse(request);
            } catch (IOException e) {
                e.printStackTrace();
            }
            new Alert(Alert.AlertType.INFORMATION, new TranslatableString("alert.friend.sent_request").translate()).show();
        }
    }
}
