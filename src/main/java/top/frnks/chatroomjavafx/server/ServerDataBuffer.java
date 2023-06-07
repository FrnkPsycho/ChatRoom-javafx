package top.frnks.chatroomjavafx.server;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import top.frnks.chatroomjavafx.common.model.entity.User;

import java.net.ServerSocket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerDataBuffer {
    public static ServerSocket serverSocket;
    public static Map<Long, OnlineClientIOCache> onlineClientIOCacheMap  = new ConcurrentSkipListMap<>();;
    public static Map<Long, User> onlineUsersMap =  new ConcurrentSkipListMap<>();
    public static List<User> onlineUsers = new CopyOnWriteArrayList<>();
    public static ObservableList<User> onlineUsersList = FXCollections.observableArrayList();
    public static final User serverUser = new User(0, "System Admin", "Uc@nTL0g1N");

    static {
        onlineUsersList.add(serverUser);
    }

}
