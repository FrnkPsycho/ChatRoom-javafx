package top.frnks.chatroomjavafx.client;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.javafx.Icon;
import top.frnks.chatroomjavafx.common.utils.TranslatableString;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientApplication extends Application {
    public static final VBox mainRoot = new VBox();
    public static final TabPane mainTabsRoot = new TabPane();
    public static final Scene mainScene = new Scene(mainRoot);
    public static final Tab profileTab = new Tab(new TranslatableString("client.tab.profile").translate());
    public static final Tab chatRoomTab = new Tab(new TranslatableString("client.tab.chat_room").translate());
    public static final Tab friendsTab = new Tab(new TranslatableString("client.tab.friends").translate());
    public static final Tab privateTab = new Tab(new TranslatableString("client.tab.private").translate());

    @Override
    public void start(Stage stage) {
        mainRoot.getChildren().add(mainTabsRoot);

        profileTab.setClosable(false);
        chatRoomTab.setClosable(false);
        friendsTab.setClosable(false);
        privateTab.setClosable(false);

        mainTabsRoot.getTabs().add(profileTab);
        mainTabsRoot.getTabs().add(chatRoomTab);
        mainTabsRoot.getTabs().add(friendsTab);
        mainTabsRoot.getTabs().add(privateTab);

        chatRoomTab.setContent(ClientChatRoomTab.chatRoomFrame);

//        mainRoot.setPrefSize(800, 600);
        stage.setTitle(new TranslatableString("client.window.title").translate());
        stage.setScene(mainScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}