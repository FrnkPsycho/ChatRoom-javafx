package top.frnks.chatroomjavafx.client.util;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import top.frnks.chatroomjavafx.client.*;
import top.frnks.chatroomjavafx.common.model.entity.*;
import top.frnks.chatroomjavafx.common.util.TranslatableString;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import static top.frnks.chatroomjavafx.client.ClientLogin.stage;

public class ClientAction {
    public static void chatResponseHandler(Response response) {
        Message message = (Message) response.getData("msg");
        if ( message.getToUser() != null ) {
//            Platform.runLater(() -> ClientUtil.switchToPrivateChat(message.getFromUser()) );
            if ( message.getFromUser().getId() != ClientDataBuffer.currentUser.getId() ) ClientUtil.switchToPrivateChat(message.getFromUser());
            ClientPrivateTab.appendTextToMessageArea(message.getContent());
        }
        else ClientUtil.appendTextToMessageArea(message.getContent());
        // TODO: chat records
    }
    public static void agreeFriendRequestResponseHandler(Response response) {
        if ( response.getResponseStatus() == ResponseStatus.OK ) {
            Message message = (Message) response.getData("friend_request");
            User user = message.getToUser();
            ClientDataBuffer.currentUser.addFriend(user);
            ClientFriendsTab.friendsListView.refresh();
            ClientChatRoomTab.onlineUserListView.refresh();
        }
    }
    public static void friendRequestResponseHandler(Response response) {
        if ( response.getResponseStatus() == ResponseStatus.OK ) {
            Message message = (Message) response.getData("friend_request");
            User user = message.getFromUser();

            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, user.getDisplayName() + new TranslatableString("alert.friend.prompt").translate());
                Optional<ButtonType> result = alert.showAndWait();

                Request request = new Request();
                request.setAttribute("friend_request", message);

                if ( result.get() == ButtonType.OK ) {
                    ClientDataBuffer.currentUser.addFriend(user);
                    ClientFriendsTab.friendsListView.refresh();
                    ClientChatRoomTab.onlineUserListView.refresh();

                    request.setAction(ActionType.AGREE_FRIEND_REQUEST); // FIXME: maybe useless
                    request.setResponseType(ResponseType.AGREE_FRIEND_REQUEST);

                } else if ( result.get() == ButtonType.NO ) {
                    // TODO: refuse friend request
                }

                try {
                    ClientUtil.sendRequestWithoutResponse(request);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
    public static void logoutResponseHandler(Response response) {
        if ( response.getResponseStatus() == ResponseStatus.OK ) {
            User user = (User) response.getData("user");
            if ( user.getId() == ClientDataBuffer.currentUser.getId() ) {
                Platform.exit();
            } else {
                ClientUtil.appendTextToMessageArea(user.getDisplayName() + new TranslatableString("client.chat.logout").translate());
//                Platform.runLater(() -> {
//                });
                ClientDataBuffer.onlineUsersList.removeIf(u -> u.getId() == user.getId());
                ClientChatRoomTab.onlineUserListView.refresh();
            }
        }
    }
    public static void broadcastResponseHandler(Response response) {
        if ( response.getResponseStatus() == ResponseStatus.OK ) {
            User loginUser = (User) response.getData("loginUser");
            ClientUtil.appendTextToMessageArea(loginUser.getDisplayName() + new TranslatableString("client.chat.online").translate());
//            Platform.runLater(() -> {
//            });
//            ClientDataBuffer.onlineUsers.add(loginUser);
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

    public static void sendPrivateMessage() {
        String content = ClientPrivateTab.privateChatTypeArea.getText();
        ClientPrivateTab.privateChatTypeArea.setText("");
        if ( content.isBlank() ) {
            Alert blankMessageAlert = new Alert(Alert.AlertType.ERROR, new TranslatableString("alert.blank_message").translate());
            blankMessageAlert.show();
        } else {
            Message msg = new Message();
            msg.setSendTime(LocalDateTime.now());
            msg.setFromUser(ClientDataBuffer.currentUser);
            String name = ClientPrivateTab.privateChatUsersTabPane.getSelectionModel().getSelectedItem().getText();
            for ( var u : ClientDataBuffer.onlineUsersList ) {
                if ( u.getNickname().equals(name) ) {
                    msg.setToUser(u);
                    break;
                }
            }
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
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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
        for ( var u : ClientDataBuffer.currentUser.getFriendsList() ) {
            if ( target.getId() == u.getId() ) {
                new Alert(Alert.AlertType.WARNING, new TranslatableString("alert.friend.already_friend").translate()).show();
                return;
            }
        }
        if ( target == null ) {
            new Alert(Alert.AlertType.WARNING, new TranslatableString("alert.friend.need_selection").translate()).show();
        } else if ( target.getId() == ClientDataBuffer.currentUser.getId()) {
            new Alert(Alert.AlertType.WARNING, new TranslatableString("alert.friend.invalid_self").translate()).show();
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
