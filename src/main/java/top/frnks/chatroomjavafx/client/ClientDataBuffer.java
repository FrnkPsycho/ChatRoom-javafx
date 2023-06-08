package top.frnks.chatroomjavafx.client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import top.frnks.chatroomjavafx.common.model.entity.User;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClientDataBuffer {
    public static boolean isLoggedIn = false;
    public static User currentUser;
    public static List<User> onlineUsers = new CopyOnWriteArrayList<>();
    public static ObservableList<User> onlineUsersList = FXCollections.observableArrayList();
    public static ObservableList<User> currentUserFriendList = FXCollections.observableArrayList();
    public static Socket clientSocket;
    public static ObjectInputStream objectInputStream;
    public static ObjectOutputStream objectOutputStream;
    public static String clientIp;

    static {
        try {
            clientIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    private ClientDataBuffer() {}

}
