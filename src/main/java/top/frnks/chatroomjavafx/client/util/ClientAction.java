package top.frnks.chatroomjavafx.client.util;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import top.frnks.chatroomjavafx.client.*;
import top.frnks.chatroomjavafx.common.model.entity.*;
import top.frnks.chatroomjavafx.common.util.TranslatableString;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static top.frnks.chatroomjavafx.client.ClientLogin.stage;

public class ClientAction {
    public static void logoutResponseHandler(Response response) {
        if ( response.getResponseStatus() == ResponseStatus.OK ) {
            User user = (User) response.getData("user");
            if ( user.getId() == ClientDataBuffer.currentUser.getId() ) {
                Platform.exit();
            } else {
                ClientUtil.appendTextToMessageArea(user.getDisplayName() + new TranslatableString("client.chat.logout").translate());
                ClientDataBuffer.onlineUsersList.removeIf(u -> u.getId() == user.getId());
                ClientChatRoomTab.onlineUserListView.refresh();
            }
        }
    }
    public static void broadcastResponseHandler(Response response) {
        if ( response.getResponseStatus() == ResponseStatus.OK ) {
            User loginUser = (User) response.getData("loginUser");
            ClientUtil.appendTextToMessageArea(loginUser.getDisplayName() + new TranslatableString("client.chat.online").translate());
            ClientDataBuffer.onlineUsersList.add(loginUser);
            ClientChatRoomTab.onlineUserListView.refresh();
        }
    }
    public static void signupResponseHandler(Response response) {
        if ( response.getResponseStatus() == ResponseStatus.OK ) {
            User user = (User) response.getData("user");
            if ( user == null ) {
                ClientUtil.popAlert(Alert.AlertType.ERROR, "client.login.registered");
                return;
            }

            ClientUtil.popAlert(Alert.AlertType.INFORMATION, "client.login.signup_success");
            ClientUtil.appendTextToMessageArea("\nHello new user: " + user.getNickname() + ", we have allocate an ID for you: " + user.getId() + ", keep in mind!");
        }
        // TODO: other statuses handling
    }

    public static void loginResponseHandler(Response response) {
        User user = (User) response.getData("user");

        if ( response.getResponseType() == ResponseType.INVALID_LOGIN ) {
            ClientUtil.popAlert(Alert.AlertType.ERROR, "client.login.login_failed");
        } else if ( response.getResponseType() == ResponseType.ALREADY_LOGON ) {
            ClientUtil.popAlert(Alert.AlertType.ERROR, "client.login.already_logon");
        } else {
            user.setOnline(true);
//        ClientUtil.appendTextToMessageArea("\nHello user: " + user.getNickname() + " <" + user.getId() + ">\n");
            ClientDataBuffer.onlineUsers = (CopyOnWriteArrayList<User>) response.getData("onlineUsers");
            ClientDataBuffer.onlineUsersList.setAll(ClientDataBuffer.onlineUsers);
            ClientDataBuffer.currentUser = user;
            ClientDataBuffer.isLoggedIn = true;
            Platform.runLater(stage::close);
        }
    }

    public static void sendMessage() {
        String content = ClientChatRoomTab.chatRoomTypeArea.getText();
        ClientChatRoomTab.chatRoomTypeArea.setText("");
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
                    " " + msg.getFromUser().getDisplayName() +
                    "\n" + content + "\n";
            msg.setContent(sb);

            Request request = new Request();
            request.setAction(ActionType.CHAT);
            request.setAttribute("msg", msg);
            try {
                ClientUtil.sendRequestWithoutResponse(request);
//                Response response = ClientUtil.sendRequestWithResponse(request);
//                if ( response.getResponseType() == ResponseType.CHAT && response.getResponseStatus() == ResponseStatus.OK ) {
//                   Message serverMsg = (Message) response.getData("Chat");
//                   ClientUtil.appendTextToMessageArea(serverMsg.getContent());
//                }
            } catch (IOException e) {
                e.printStackTrace(); // TODO: proper exception handling
            }

            // TODO: ctrl+enter to send message
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
