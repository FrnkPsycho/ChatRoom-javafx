package top.frnks.chatroomjavafx.client;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import top.frnks.chatroomjavafx.client.util.ClientAction;
import top.frnks.chatroomjavafx.client.util.ClientUtil;
import top.frnks.chatroomjavafx.common.model.entity.ActionType;
import top.frnks.chatroomjavafx.common.model.entity.Message;
import top.frnks.chatroomjavafx.common.model.entity.Request;
import top.frnks.chatroomjavafx.common.model.entity.User;
import top.frnks.chatroomjavafx.common.util.TranslatableString;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClientChatRoomTab {
    public static final GridPane chatRoomFrame = new GridPane();
    public static final TextArea chatRoomMessageArea = new TextArea(new TranslatableString("client.chat.motd").translate());
//    public static final ScrollPane chatRoomMessagePane = new ScrollPane(chatRoomMessageArea);
    public static final Button chatRoomTypeSendButton = new Button(new TranslatableString("client.chat.send").translate());
    public static final Button chatRoomTypeSendPictureButton = new Button("Pic"); // TODO: use icon instead of text
    public static final TilePane chatRoomTypeTools = new TilePane();
    public static final TextArea chatRoomTypeArea = new TextArea();
//    public static final ScrollPane chatRoomTypePane = new ScrollPane(chatRoomTypeArea);
    public static final ListView<User> memberView;

    static {


        chatRoomFrame.setHgap(10);
        chatRoomFrame.setVgap(10);

        List<User> members = new ArrayList<>();
        for ( int i = 10000; i<10030; i++) {
            members.add(new User(i, "User"+i, "aaa"));
        }
//        members.get(0).setOnline(true);
//        members.get(1).setOnline(true);
//        members.get(0).addFriend(members.get(1));
//        members.get(0).removeFriend(members.get(1));
//        members.get(1).setOnline(false);
        // TODO: member list just for testing
        // TODO: update members when a user is online

        chatRoomMessageArea.setEditable(false);
//        chatRoomMessageArea.set(Pos.BOTTOM_LEFT); // TODO make TextArea aligned to bottom left
        chatRoomMessageArea.setWrapText(true);
        chatRoomMessageArea.setPrefSize(600, 500);
        chatRoomMessageArea.setScrollTop(Double.MAX_VALUE);

        chatRoomTypeSendButton.setPrefSize(200, 100);
        chatRoomTypeSendButton.setOnAction(event -> ClientAction.sendMessage());
        // TODO: send message when a combination of key pressed
//        ClientApplication.chatRoomTab.getTabPane().getScene().setOnKeyPressed(event -> {
//            if ( chatRoomMessageArea.isFocused() && event.getCode() == KeyCode.ALT) {
//                sendMessage();
//            }
//        });

        // TODO: debug
//        for ( int i = 0; i<9; i++ ) {
//            chatRoomTypeArea.setText("a");
//            ClientAction.sendMessage();
//        }

        chatRoomTypeTools.getChildren().add(chatRoomTypeSendPictureButton);

//        chatRoomTypeArea.setAlignment(Pos.TOP_LEFT);
        chatRoomTypeArea.setWrapText(true);
        chatRoomTypeArea.setPrefSize(600, 100);
//        chatRoomTypePane.setVvalue(1.0);
        chatRoomTypeArea.setScrollTop(Double.MAX_VALUE);

//        memberView = new ListView<>(FXCollections.observableList(ClientDataBuffer.onlineUsers));
        memberView = new ListView<>(FXCollections.observableList(members)); // TODO for debugging, use the line above this.
        memberView.setCellFactory(new UserCellFactory());
        memberView.setEditable(false);
        memberView.setPrefSize(200, 500);
        memberView.setContextMenu(new UserListContextMenu());
//        memberView.setContextMenu(new ContextMenu(new MenuItem("Check")));

        chatRoomFrame.add(chatRoomMessageArea, 0, 0);
        chatRoomFrame.add(chatRoomTypeTools, 0, 1);
        chatRoomFrame.add(chatRoomTypeArea, 0, 2);
        chatRoomFrame.add(chatRoomTypeSendButton, 1, 2);
        chatRoomFrame.add(memberView, 1, 0);
    }

}
