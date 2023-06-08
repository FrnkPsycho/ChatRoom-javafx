package top.frnks.chatroomjavafx.client;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import top.frnks.chatroomjavafx.client.util.ClientAction;
import top.frnks.chatroomjavafx.common.model.entity.Message;
import top.frnks.chatroomjavafx.common.model.entity.User;
import top.frnks.chatroomjavafx.common.util.TranslatableString;

public class ClientPrivateTab {
    public static final VBox privateChatFrame = new VBox();
    public static final TabPane privateChatUsersTabPane = new TabPane();
//    public static final TextArea privateChatMessageArea = new TextArea();
    public static final HBox privateChatTypeBox = new HBox();
    public static final Button privateChatTypeSendButton = new Button(new TranslatableString("client.chat.send").translate());
    public static final TextArea privateChatTypeArea = new TextArea();

    static {
//        addPrivateChatTab(ClientDataBuffer.currentUser);

        privateChatTypeSendButton.setPrefSize(200, 100);
        privateChatTypeSendButton.setOnAction(event -> ClientAction.sendPrivateMessage());

        privateChatTypeBox.getChildren().add(privateChatTypeArea);
        privateChatTypeBox.getChildren().add(privateChatTypeSendButton);

        privateChatFrame.getChildren().add(privateChatUsersTabPane);
        privateChatFrame.getChildren().add(privateChatTypeBox);

    }

    public static Tab addPrivateChatTab(User user) {
        Tab tab = new Tab(user.getNickname());
        TextArea messageArea = new TextArea();
        messageArea.setPrefSize(600, 500);
        messageArea.setEditable(false);
        messageArea.setWrapText(true);
        messageArea.setScrollTop(Double.MAX_VALUE);
        tab.setContent(messageArea);
        Platform.runLater(() -> {
            privateChatUsersTabPane.getTabs().add(tab);
        });

        return tab;
    }

    public static void appendTextToMessageArea(String text) {

        TextArea messageArea = (TextArea) privateChatUsersTabPane.getSelectionModel().getSelectedItem().getContent();
        messageArea.appendText("\n" + text);
    }
}
