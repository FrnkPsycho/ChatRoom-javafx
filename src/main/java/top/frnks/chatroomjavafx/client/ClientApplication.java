package top.frnks.chatroomjavafx.client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientApplication extends Application {
    public static final VBox mainRoot = new VBox();
    public static final TabPane mainTabsRoot = new TabPane();
    public static final TabPane roomTabsRoot = new TabPane();
    public static final Scene mainScene = new Scene(mainRoot);
    @Override
    public void start(Stage stage) throws IOException {
        mainRoot.getChildren().add(mainTabsRoot);
//        Tab profileTab = new Tab("");

        stage.setTitle("Chat Room");
        stage.setScene(mainScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}