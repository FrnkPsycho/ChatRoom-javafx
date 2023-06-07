package top.frnks.chatroomjavafx.server;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import top.frnks.chatroomjavafx.common.model.entity.User;

public class ServerUserListContextMenu extends ContextMenu {
    private static final MenuItem kickUserMenuItem = new MenuItem("Kick");

    static {
        kickUserMenuItem.setOnAction( event -> {
            User target = ServerApplication.onlineUserListView.getSelectionModel().getSelectedItem();
            if ( target != null ) ServerAction.kickUser(target);
        });
    }
    public ServerUserListContextMenu() {
        super(kickUserMenuItem);
    }
}
