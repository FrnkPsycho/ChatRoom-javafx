package top.frnks.chatroomjavafx.client;

import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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
                    if ( item.isOnline() ) {
                        setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                    }
                    setText(item.getNickname() + " <" + item.getId() + ">");
                }
                // TODO: add avatar
            }
        };
    }
}
