package top.frnks.chatroomjavafx.client;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import top.frnks.chatroomjavafx.common.utils.TranslatableString;

import java.util.ArrayList;
import java.util.List;

public class ClientChatRoomTab {
    public static final GridPane chatRoomFrame = new GridPane();
    public static final TextField chatRoomMessageField = new TextField("TEST MESSAGE");
    public static final Button chatRoomTypeSendButton = new Button(new TranslatableString("client.chat.send").translate());
    public static final Button chatRoomTypeSendPictureButton = new Button("Pic"); // TODO: use icon instead of text
    public static final TilePane chatRoomTypeTools = new TilePane();
    public static final TextField chatRoomTypeField = new TextField();
    public static final ListView<String> memberView;

    static {


        chatRoomFrame.setHgap(10);
        chatRoomFrame.setVgap(10);

        List<String> members = new ArrayList<>(List.of("Admin", "User111")); // TODO: member list just for testing


        chatRoomMessageField.setEditable(false);
        chatRoomMessageField.setAlignment(Pos.BOTTOM_LEFT);
        chatRoomMessageField.setPrefSize(600, 500);

        chatRoomTypeSendButton.setPrefSize(200, 100);

        chatRoomTypeTools.getChildren().add(chatRoomTypeSendPictureButton);

        chatRoomTypeField.setAlignment(Pos.TOP_LEFT);
        chatRoomTypeField.setPrefSize(600, 100);

        memberView = new ListView<>(FXCollections.observableList(members));
        memberView.setEditable(false);
        memberView.setPrefSize(200, 500);
//        memberView.setContextMenu(new ContextMenu(new MenuItem("Check")));

        chatRoomFrame.add(chatRoomMessageField, 0, 1);
        chatRoomFrame.add(chatRoomTypeTools, 0, 2);
        chatRoomFrame.add(chatRoomTypeField, 0, 3);
        chatRoomFrame.add(chatRoomTypeSendButton, 1, 3);
        chatRoomFrame.add(memberView, 1, 1);

    }
}
