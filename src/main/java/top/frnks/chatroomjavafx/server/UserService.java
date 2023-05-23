package top.frnks.chatroomjavafx.server;

import top.frnks.chatroomjavafx.common.model.entity.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private static final int ID_BASE = 10000;
    private static int currentID;
    public void saveUsers(List<User> users) {
        ObjectOutputStream oos = null;

        try {
            oos = new ObjectOutputStream(new FileOutputStream(ServerProperties.getProperty("databasePath")));
            oos.writeObject(users);
            oos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtil.close(oos);
        }
    }

    @SuppressWarnings("unchecked")
    public List<User> loadUsers() {
        ObjectInputStream ois = null;
        List<User> list;

        try {
            ois = new ObjectInputStream(new FileInputStream(ServerProperties.getProperty("databasePath")));
            list = (List<User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtil.close(ois);
        }

        return list;
    }

    public void addUser(User user) {
        var users = this.loadUsers();
        user.setId(ID_BASE + users.size());
        users.add(user);
        this.saveUsers(users);
    }

    public void deleteUser(User user) {
        var users = this.loadUsers();
        users.remove(user);
        this.saveUsers(users);
    }

//    public User login(long id, String hash) {
//
//    }
}
