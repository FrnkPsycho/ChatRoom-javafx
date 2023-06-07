package top.frnks.chatroomjavafx.server;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import top.frnks.chatroomjavafx.client.ClientChatRoomTab;
import top.frnks.chatroomjavafx.client.ClientDataBuffer;
import top.frnks.chatroomjavafx.client.UserCellFactory;
import top.frnks.chatroomjavafx.client.UserListContextMenu;
import top.frnks.chatroomjavafx.client.util.ClientAction;
import top.frnks.chatroomjavafx.common.model.entity.User;
import top.frnks.chatroomjavafx.common.util.TranslatableString;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class ServerApplication extends Application {
//    public static final String LOOPBACK_ADDRESS = "localhost";
    public static final int SERVER_PORT = Integer.parseInt(ServerProperties.getProperty("serverPort"));
    public static final Logger LOGGER = Logger.getGlobal();
    public static final GridPane mainRoot = new GridPane();
    public static final Scene mainScene = new Scene(mainRoot);
    public static final ListView<User> onlineUserListView = new ListView<>();

    public static final GridPane chatRoomFrame = new GridPane();
    public static final TextArea chatRoomMessageArea = new TextArea();
    public static final Button chatRoomTypeSendButton = new Button(new TranslatableString("client.chat.send").translate());
    public static final Button chatRoomTypeSendPictureButton = new Button("Pic"); // TODO: use icon instead of text
    public static final TilePane chatRoomTypeTools = new TilePane();
    public static final TextArea chatRoomTypeArea = new TextArea();

    private static void loadServer() {
        // TODO: make server a System User or else.

        try ( ServerSocket serverSocket = new ServerSocket(SERVER_PORT) ) {
            ServerDataBuffer.serverSocket = serverSocket;
            LOGGER.info("ChatRoom Server Start Listening on Port " + SERVER_PORT);
            while ( true ) {
                Socket clientSocket = serverSocket.accept();
                LOGGER.info("Received Connection from " + clientSocket.getRemoteSocketAddress());
                Thread t = new Thread(new RequestProcessor(clientSocket));
                t.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Thread listenerThread = new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
                ServerDataBuffer.serverSocket = serverSocket;
                LOGGER.info("ChatRoom Server Start Listening on Port " + SERVER_PORT);
                appendTextToMessageArea("Server started for connection...");
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    LOGGER.info("Received Connection from " + clientSocket.getRemoteSocketAddress());
                    Thread t = new Thread(new RequestProcessor(clientSocket));
                    t.start();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        listenerThread.start();

//        chatRoomFrame.setHgap(10);
//        chatRoomFrame.setVgap(10);

        chatRoomMessageArea.setEditable(false);
//        chatRoomMessageArea.set(Pos.BOTTOM_LEFT); // TODO make TextArea aligned to bottom left
        chatRoomMessageArea.setWrapText(true);
        chatRoomMessageArea.setPrefSize(600, 500);
        chatRoomMessageArea.setScrollTop(Double.MAX_VALUE);

        chatRoomTypeSendButton.setPrefSize(200, 100);
        chatRoomTypeSendButton.setOnAction(event -> ServerAction.sendMessage());
        // TODO: send message when a combination of key pressed
//        ClientApplication.chatRoomTab.getTabPane().getScene().setOnKeyPressed(event -> {
//            if ( chatRoomMessageArea.isFocused() && event.getCode() == KeyCode.ALT) {
//                sendMessage();
//            }
//        });


        chatRoomTypeTools.getChildren().add(chatRoomTypeSendPictureButton);

//        chatRoomTypeArea.setAlignment(Pos.TOP_LEFT);
        chatRoomTypeArea.setWrapText(true);
        chatRoomTypeArea.setPrefSize(600, 100);
//        chatRoomTypePane.setVvalue(1.0);
        chatRoomTypeArea.setScrollTop(Double.MAX_VALUE);

        onlineUserListView.setItems(null);
        onlineUserListView.setItems(ServerDataBuffer.onlineUsersList);
//        memberView = new ListView<>(FXCollections.observableList(members)); // TODO for debugging, use the line above this.
        onlineUserListView.setCellFactory(new ServerUserCellFactory());
        onlineUserListView.setEditable(false);
        onlineUserListView.setPrefSize(200, 500);
        onlineUserListView.setContextMenu(new ServerUserListContextMenu());
//        memberView.setContextMenu(new ContextMenu(new MenuItem("Check")));

        chatRoomFrame.add(chatRoomMessageArea, 0, 0);
        chatRoomFrame.add(chatRoomTypeTools, 0, 1);
        chatRoomFrame.add(chatRoomTypeArea, 0, 2);
        chatRoomFrame.add(chatRoomTypeSendButton, 1, 2);
        chatRoomFrame.add(onlineUserListView, 1, 0);

        mainScene.setRoot(chatRoomFrame);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("JavaFX Chat Room Server GUI");
        primaryStage.show();
    }

    public static void appendTextToMessageArea(String text) {
        ServerApplication.chatRoomMessageArea.appendText("\n" + text);
    }
}
