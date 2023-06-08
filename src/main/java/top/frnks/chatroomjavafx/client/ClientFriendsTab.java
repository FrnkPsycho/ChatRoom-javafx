package top.frnks.chatroomjavafx.client;

import javafx.collections.FXCollections;
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;
import top.frnks.chatroomjavafx.common.model.entity.User;

public class ClientFriendsTab {
    public static final FlowPane friendsFrame = new FlowPane();
    public static final ListView<User> friendsListView = new ListView<>();
    static {
        friendsListView.setItems(null);
        friendsListView.setItems(ClientDataBuffer.currentUserFriendList);
        friendsListView.setCellFactory(new UserCellFactory());
//        friendsListView.setMaxSize(1000, 1000);
        friendsFrame.getChildren().add(friendsListView);
    }

}
