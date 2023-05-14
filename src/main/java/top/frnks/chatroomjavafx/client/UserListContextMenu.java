package top.frnks.chatroomjavafx.client;

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
            User target = ClientChatRoomTab.memberView.getSelectionModel().getSelectedItem();
            ClientAction.addFriend(target);
            System.out.println("Sent request!");
        });
    }

    public UserListContextMenu() {
        super(addFriendMenuItem, deleteFriendMenuItem, startPrivateChatMenuItem);
    }
}
