package top.frnks.chatroomjavafx.server;

import javafx.scene.control.Alert;
import top.frnks.chatroomjavafx.client.ClientChatRoomTab;
import top.frnks.chatroomjavafx.client.ClientDataBuffer;
import top.frnks.chatroomjavafx.client.util.ClientUtil;
import top.frnks.chatroomjavafx.common.model.entity.*;
import top.frnks.chatroomjavafx.common.util.TranslatableString;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServerAction {
    public static void kickUser(User user) {
        user.setOnline(false);

        Response response = new Response();
        response.setResponseStatus(ResponseStatus.OK);
        response.setResponseType(ResponseType.LOGOUT);
        response.setData("user", user);
        try {
            RequestProcessor.broadcastResponse(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ServerDataBuffer.onlineUsersMap.remove(user.getId());
        ServerDataBuffer.onlineUsersList.removeIf(user1 -> user1.getId() == user.getId());
        ServerApplication.onlineUserListView.refresh();
        ServerDataBuffer.onlineClientIOCacheMap.remove(user.getId());
    }
    public static void sendMessage(){
        String content = ServerApplication.chatRoomTypeArea.getText();
        ServerApplication.chatRoomTypeArea.setText("");

        if ( content.equals("/list") ) {
            var users = RequestProcessor.userService.loadUsers();
            for ( var user : users ) {
                ServerApplication.appendTextToMessageArea(user.getDisplayName() + ": " + user.getFriendsList() + "\n\n");
            }
            return;
        }

        if ( content.isBlank() ) {
            Alert blankMessageAlert = new Alert(Alert.AlertType.ERROR, new TranslatableString("alert.blank_message").translate());
            blankMessageAlert.show();
        } else {
            Message msg = new Message();
//            msg.setContent(content);
            msg.setSendTime(LocalDateTime.now());
            msg.setFromUser(ServerDataBuffer.serverUser);
            msg.setToUser(null);

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String sb = msg.getSendTime().format(dtf) +
                    " " + msg.getFromUser().getDisplayName() +
                    "\n" + content + "\n";
            msg.setContent(sb);

            ServerApplication.appendTextToMessageArea(sb);

            Response response = new Response();
            response.setResponseStatus(ResponseStatus.OK);
            response.setResponseType(ResponseType.CHAT);
            response.setData("msg", msg);
            try {
                RequestProcessor.broadcastResponse(response);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
