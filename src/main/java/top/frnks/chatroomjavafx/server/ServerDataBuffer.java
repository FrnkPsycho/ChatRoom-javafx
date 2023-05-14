package top.frnks.chatroomjavafx.server;

import top.frnks.chatroomjavafx.common.model.entity.User;

import java.net.ServerSocket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

public class ServerDataBuffer {
    public static ServerSocket serverSocket;
    public static Map<Long, OnlineClientIOCache> onlineClientIOCacheMap;
    public static Map<Long, User> onlineUsersMap;
    public static List<User> FriendList;

    static {
        onlineClientIOCacheMap = new ConcurrentSkipListMap<>();
        onlineUsersMap = new ConcurrentSkipListMap<>();
    }
}
