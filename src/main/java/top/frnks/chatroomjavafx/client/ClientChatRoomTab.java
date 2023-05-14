package top.frnks.chatroomjavafx.client;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import top.frnks.chatroomjavafx.common.model.entity.User;
import top.frnks.chatroomjavafx.common.util.TranslatableString;

import java.util.ArrayList;
import java.util.List;

public class ClientChatRoomTab {
    public static final GridPane chatRoomFrame = new GridPane();
    public static final TextArea chatRoomMessageArea = new TextArea("TEST MESSAGE");
    public static final ScrollPane chatRoomMessagePane = new ScrollPane(chatRoomMessageArea);
    public static final Button chatRoomTypeSendButton = new Button(new TranslatableString("client.chat.send").translate());
    public static final Button chatRoomTypeSendPictureButton = new Button("Pic"); // TODO: use icon instead of text
    public static final TilePane chatRoomTypeTools = new TilePane();
    public static final TextArea chatRoomTypeArea = new TextArea();
    public static final ScrollPane chatRoomTypePane = new ScrollPane(chatRoomTypeArea);
    public static final ListView<User> memberView;

    static {


        chatRoomFrame.setHgap(10);
        chatRoomFrame.setVgap(10);

        List<User> members = new ArrayList<>(List.of(
                new User(10000, "Admin", "P@s5W0rD"),
                new User(10001, "User1", "test")));
        members.get(0).setOnline(true);
        members.get(1).setOnline(true);
        members.get(0).addFriend(members.get(1));
        members.get(0).removeFriend(members.get(1));
        members.get(1).setOnline(false);
        // TODO: member list just for testing

        chatRoomMessageArea.setEditable(false);
//        chatRoomMessageArea.set(Pos.BOTTOM_LEFT); // TODO make TextArea aligned to bottom left
        chatRoomMessageArea.setWrapText(true);
        chatRoomMessageArea.setPrefSize(600, 500);

        chatRoomTypeSendButton.setPrefSize(200, 100);
//        chatRoomTypeSendButton.setOnAction(event -> {
//
//        });

        chatRoomTypeTools.getChildren().add(chatRoomTypeSendPictureButton);

//        chatRoomTypeArea.setAlignment(Pos.TOP_LEFT);
        chatRoomTypeArea.setWrapText(true);
        chatRoomTypeArea.setPrefSize(600, 100);

//        memberView = new ListView<>(FXCollections.observableList(ClientDataBuffer.allUsers));
        memberView = new ListView<>(FXCollections.observableList(members)); // TODO for debugging, use the line above this.
        memberView.setCellFactory(new UserCellFactory());
        memberView.setEditable(false);
        memberView.setPrefSize(200, 500);
//        memberView.setContextMenu(new ContextMenu(new MenuItem("Check")));

        chatRoomFrame.add(chatRoomMessagePane, 0, 0);
        chatRoomFrame.add(chatRoomTypeTools, 0, 1);
        chatRoomFrame.add(chatRoomTypePane, 0, 2);
        chatRoomFrame.add(chatRoomTypeSendButton, 1, 2);
        chatRoomFrame.add(memberView, 1, 0);

    }
}
