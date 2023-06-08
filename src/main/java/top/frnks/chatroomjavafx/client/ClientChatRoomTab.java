package top.frnks.chatroomjavafx.client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import top.frnks.chatroomjavafx.client.util.ClientAction;
import top.frnks.chatroomjavafx.common.model.entity.User;
import top.frnks.chatroomjavafx.common.util.TranslatableString;

import java.util.ArrayList;
import java.util.List;

public class ClientChatRoomTab {
    public static final GridPane chatRoomFrame = new GridPane();
    public static final TextArea chatRoomMessageArea = new TextArea(new TranslatableString("client.chat.motd").translate());
    public static final Button chatRoomTypeSendButton = new Button(new TranslatableString("client.chat.send").translate());
    public static final TextArea chatRoomTypeArea = new TextArea();
    public static final ListView<User> onlineUserListView = new ListView<>();

    static {


        chatRoomFrame.setHgap(10);
        chatRoomFrame.setVgap(10);

        chatRoomMessageArea.setEditable(false);
        chatRoomMessageArea.setWrapText(true);
        chatRoomMessageArea.setPrefSize(600, 500);
        chatRoomMessageArea.setScrollTop(Double.MAX_VALUE);

        chatRoomTypeSendButton.setPrefSize(200, 100);
        chatRoomTypeSendButton.setOnAction(event -> ClientAction.sendMessage());

        chatRoomTypeArea.setWrapText(true);
        chatRoomTypeArea.setPrefSize(600, 100);
        chatRoomTypeArea.setScrollTop(Double.MAX_VALUE);

        onlineUserListView.setItems(null);
        onlineUserListView.setItems(ClientDataBuffer.onlineUsersList);
        onlineUserListView.setCellFactory(new UserCellFactory());
        onlineUserListView.setEditable(false);
        onlineUserListView.setPrefSize(200, 500);
        onlineUserListView.setContextMenu(new UserListContextMenu());

        chatRoomFrame.add(chatRoomMessageArea, 0, 0);
        chatRoomFrame.add(chatRoomTypeArea, 0, 2);
        chatRoomFrame.add(chatRoomTypeSendButton, 1, 2);
        chatRoomFrame.add(onlineUserListView, 1, 0);
    }

}
