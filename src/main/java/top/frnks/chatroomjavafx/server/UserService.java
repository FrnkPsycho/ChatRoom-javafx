package top.frnks.chatroomjavafx.server;

import javafx.util.Pair;
import top.frnks.chatroomjavafx.common.model.entity.User;
import top.frnks.chatroomjavafx.common.util.PasswordUtil;

import java.io.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

public class UserService {
    // TODO: using list to store data causes very bad performance, consider using database
    private static final int ID_BASE = 10000;
    private static int currentID;
    public static Logger LOGGER = ServerApplication.LOGGER;
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
        LOGGER.info("Successfully saved users to database");
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

        LOGGER.info("Successfully load users from database");
        return list;
    }

    public void addUser(User user) {
        var users = this.loadUsers();
        user.setId(ID_BASE + users.size());
        users.add(user);
        LOGGER.info("Added user: " + user + " to database");
        this.saveUsers(users);
    }

    public void deleteUser(User user) {
        var users = this.loadUsers();
        users.remove(user);
        LOGGER.info("Deleted user: " + user + " from database");
        this.saveUsers(users);
    }

    public void saveUser(User user) {
        List<User> users = loadUsers();
        for ( var u : users ) {
            if ( user.getId() == u.getId() ) {
                users.set((int) (user.getId()-1), user);
                break;
            }
        }
        LOGGER.info("Saved user" + user + " to database");
        saveUsers(users);
    }

    public User loadUser(long id) {
        User result = null;
        List<User> users = loadUsers();
        for ( var user : users ) {
            if ( user.getId() == id ) {
                result = user;
                break;
            }
        }
        LOGGER.info("Successfully load user: " + result + " from database");
        return result;
    }

    /**
     * return login user object if success, null if login failed
     */

    @SuppressWarnings("unchecked")
    public User login(long id, String password) {
        User searchResult = null;
        List<User> users = this.loadUsers();
        for ( User user : users ) {
            if ( user.getId() == id ) {
                var salt = user.getPasswordSalt();
                if ( PasswordUtil.verifyPassword(salt, password, user.getPassword())) {
                    searchResult = user;
                    break;
                }
            }
        }
        LOGGER.info("User login success");
        return searchResult;
    }

    @SuppressWarnings("unchecked")
    public User login(String nickname, String password) {
        User searchResult = null;
        List<User> users = this.loadUsers();
        for ( User user : users ) {
            if ( user.getNickname().equals(nickname) ) {
                var salt = user.getPasswordSalt();
                if ( PasswordUtil.verifyPassword(salt, password, user.getPassword())) {
                    searchResult = user;
                    break;
                }
            }
        }
        LOGGER.info("User login success");
        return searchResult;
    }

    /**
     * return signup user object if nickname was not occupied, null if so.
     */
    public User signup(String nickname, String password) {
        var users = loadUsers();
        for ( var u : users ) {
            if ( u.getNickname().equals(nickname) ) {
                return null;
            }
        }
        User user = new User(0, nickname, password);
        addUser(user);
        LOGGER.info("User signup success");
        return user;
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
