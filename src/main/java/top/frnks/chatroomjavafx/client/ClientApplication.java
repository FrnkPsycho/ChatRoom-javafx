package top.frnks.chatroomjavafx.client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import top.frnks.chatroomjavafx.common.model.entity.User;
import top.frnks.chatroomjavafx.common.util.TranslatableString;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

public class ClientApplication extends Application {
    public static final Logger LOGGER = Logger.getGlobal();
    public static final VBox mainRoot = new VBox();
    public static final TabPane mainTabsRoot = new TabPane();
    public static final Scene mainScene = new Scene(mainRoot);
    public static final Tab profileTab = new Tab(new TranslatableString("client.tab.profile").translate());
    public static final Tab chatRoomTab = new Tab(new TranslatableString("client.tab.chat_room").translate());
    public static final Tab friendsTab = new Tab(new TranslatableString("client.tab.friends").translate());
    public static final Tab privateTab = new Tab(new TranslatableString("client.tab.private").translate());

    @Override
    public void start(Stage stage) {
//        connectToServer("localhost", 6657);
        // TODO: login to get currentUser
        ClientDataBuffer.currentUser = new User(88888, "DEBUG_CURRENT_USER", "password");

        // TODO: uncomment this after server code is done.
        mainRoot.getChildren().add(mainTabsRoot);

        profileTab.setClosable(false);
        chatRoomTab.setClosable(false);
        friendsTab.setClosable(false);
        privateTab.setClosable(false);

        mainTabsRoot.getTabs().add(chatRoomTab);
        mainTabsRoot.getTabs().add(privateTab);
        mainTabsRoot.getTabs().add(profileTab);
        mainTabsRoot.getTabs().add(friendsTab);

        chatRoomTab.setContent(ClientChatRoomTab.chatRoomFrame);
        friendsTab.setContent(ClientFriendsTab.friendsFrame);

//        mainRoot.setPrefSize(800, 600);
        stage.setTitle(new TranslatableString("client.window.title").translate());
        stage.setScene(mainScene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }

    public static void connectToServer(String remote, int port) {
        try {
            ClientDataBuffer.clientSocket = new Socket(remote, port);
            ClientDataBuffer.objectInputStream = new ObjectInputStream(ClientDataBuffer.clientSocket.getInputStream());
            ClientDataBuffer.objectOutputStream = new ObjectOutputStream(ClientDataBuffer.clientSocket.getOutputStream());
        } catch (IOException e) {
            LOGGER.warning("Unable to connect to server: \"" + remote + ":" + port + "\"");
            // TODO: loop reconnect to server
        }

    }
}