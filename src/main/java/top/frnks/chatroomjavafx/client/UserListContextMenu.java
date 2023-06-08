package top.frnks.chatroomjavafx.client;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import top.frnks.chatroomjavafx.client.util.ClientAction;
import top.frnks.chatroomjavafx.client.util.ClientUtil;
import top.frnks.chatroomjavafx.common.model.entity.User;
import top.frnks.chatroomjavafx.common.util.TranslatableString;

public class UserListContextMenu extends ContextMenu {
    private static final MenuItem addFriendMenuItem = new MenuItem(new TranslatableString("menu.add_friend").translate());
    private static final MenuItem deleteFriendMenuItem = new MenuItem(new TranslatableString("menu.delete_friend").translate());
    private static final MenuItem startPrivateChatMenuItem = new MenuItem(new TranslatableString("menu.private_chat").translate());
    private static MenuItem checkProfileMenuItem;

    static {
        addFriendMenuItem.setOnAction(event -> {
            User target = ClientChatRoomTab.onlineUserListView.getSelectionModel().getSelectedItem();
            ClientAction.addFriend(target);
//            System.out.println("Sent request!");
        });

        startPrivateChatMenuItem.setOnAction(event -> {
            User target = ClientChatRoomTab.onlineUserListView.getSelectionModel().getSelectedItem();
            for ( var user : ClientDataBuffer.currentUser.getFriendsList() ) {
                if ( target.getId() == user.getId() ) {
                    ClientUtil.switchToPrivateChat(target);
                    return;
                }
            }
            Platform.runLater(() -> {
                ClientUtil.popAlert(Alert.AlertType.ERROR, "client.private.not_friend");
            });
        });
    }

    public UserListContextMenu() {
        super(addFriendMenuItem, deleteFriendMenuItem, startPrivateChatMenuItem);
    }
}
