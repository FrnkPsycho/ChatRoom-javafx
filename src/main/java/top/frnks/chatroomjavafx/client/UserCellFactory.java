package top.frnks.chatroomjavafx.client;

import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import top.frnks.chatroomjavafx.common.model.entity.User;

public class UserCellFactory implements Callback<ListView<User>, ListCell<User>> {
    @Override
    public ListCell<User> call(ListView<User> param) {
        return new ListCell<>() {
            @Override
            public void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if ( empty || item == null) setText(null);
                else {
                    // decided to make public chat list only shows online users
//                    if ( item.isOnline() ) {
//                        setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
//                    }
                    if ( ClientDataBuffer.currentUser.getFriendsList().contains(item) ) {
                        setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                    }
                    setText(item.getNickname() + " <" + item.getId() + ">");
                }
                // TODO: add avatar
            }
        };
    }
}
