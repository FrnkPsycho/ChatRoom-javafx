package top.frnks.chatroomjavafx.server;

import top.frnks.chatroomjavafx.common.model.entity.User;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class UserService {
    // TODO: using list to store data causes very bad performance, consider using database
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
            FileInputStream fis = new FileInputStream(ServerProperties.getProperty("databasePath"));
            ois = new ObjectInputStream(fis);
            list = (List<User>) ois.readObject();
        } catch (EOFException eof) {
            return new CopyOnWriteArrayList<>();
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

    /**
     * return login user object if success, null if login failed
     */
    public User login(long id, String password) {
        User searchResult = null;
        List<User> users = this.loadUsers();
        for ( User user : users ) {
            if ( user.getId() == id && user.getPassword().equals(password) ) {
                searchResult = user;
                break;
            }
        }
        return searchResult;
    }

    public void initUserService() {
        // Test users
        User testUser1 = new User(0, "TEST_USER1", "password");
        List<User> users = new CopyOnWriteArrayList<>();
        users.add(testUser1);
        this.saveUsers(users);
    }

    /**
     * init user database and print all users
     */
    public static void main(String[] args) {
        UserService us = new UserService();
        List<User> users = us.loadUsers();
        if ( users.size() == 0 ) {
            us.initUserService();
            users = us.loadUsers();
        }
        for ( User user : users ) {
            System.out.println(user);
        }
    }
}
