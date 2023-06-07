package top.frnks.chatroomjavafx.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import top.frnks.chatroomjavafx.client.util.ClientUtil;
import top.frnks.chatroomjavafx.common.model.entity.ActionType;
import top.frnks.chatroomjavafx.common.model.entity.Request;
import top.frnks.chatroomjavafx.common.util.TranslatableString;

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
        loadClient();
        ClientLogin.showWindow();
        while (!ClientDataBuffer.isLoggedIn) {
            // TODO: avoid busy wait
        }

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

    @Override
    public void stop() throws Exception {
        Request request = new Request();
        request.setAction(ActionType.LOGOUT);
        request.setAttribute("user", ClientDataBuffer.currentUser);
        ClientUtil.sendRequestWithoutResponse(request);
    }

    public static void main(String[] args) {
        launch();
    }


    public static void loadClient() {
        new ClientThread().start();
    }
}