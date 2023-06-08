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
                    for ( var u : ClientDataBuffer.currentUser.getFriendsList() ) {
                        if ( u.getId() == item.getId() ) {
                            setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                        }
                    }
                    if ( item.getId() == ClientDataBuffer.currentUser.getId() ) {
                        setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
                    }
                    setText(item.getDisplayName());
                }
            }
        };
    }
}
