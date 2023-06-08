package top.frnks.chatroomjavafx.client.model.entity;

import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import top.frnks.chatroomjavafx.common.model.entity.User;

public class PrivateChatTab {
    public final Tab tab = new Tab();
    public final TextArea messageArea = new TextArea();
    public PrivateChatTab(User user) {
        tab.setText(user.getNickname());
        messageArea.setPrefSize(600, 500);
        messageArea.setEditable(false);
        messageArea.setWrapText(true);
        messageArea.setScrollTop(Double.MAX_VALUE);
        tab.setContent(messageArea);
    }
}
