package top.frnks.chatroomjavafx.client;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class ClientProfileTab {

    public static final GridPane profileFrame = new GridPane();
    public static final Label tbdLabel = new Label("To be done...");

    static {
        profileFrame.add(tbdLabel, 0, 0);
    }

}
