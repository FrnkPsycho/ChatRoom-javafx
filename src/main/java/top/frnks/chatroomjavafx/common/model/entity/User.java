package top.frnks.chatroomjavafx.common.model.entity;

import top.frnks.chatroomjavafx.client.ClientApplication;
import top.frnks.chatroomjavafx.client.util.ClientUtil;
import top.frnks.chatroomjavafx.common.type.Gender;
import top.frnks.chatroomjavafx.common.util.PasswordUtil;
import top.frnks.chatroomjavafx.common.util.TranslatableString;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 100L;
    private long id;
    private String nickname;
    private String password; // salted password
    private byte[] passwordSalt;
    // TODO: avatar
    private Gender gender;
    private List<User> friendsList = new ArrayList<>(50);

    // TODO: chat history
    private boolean online = false;

    public User(long id, String nickname, String rawPassword) {
        this.id = id;
        this.nickname = checkNickname(nickname);

        var pwPair = PasswordUtil.digestPassword(rawPassword);
        this.passwordSalt = pwPair.getKey();
        this.password = pwPair.getValue();

    }
    static String checkNickname(String nickname) {
        if ( nickname.isEmpty() ) return new TranslatableString("common.default_nickname").translate();
        else return nickname;
        // TODO: sensitive nickname check
    }
    public void addFriend(User user) {
        if ( friendsList.contains(user) ) {
            // TODO: handle if target user is already user's friend
            ClientApplication.LOGGER.info(user + " is already your friend.");
        }
        else {
            friendsList.add(user);
            ClientApplication.LOGGER.info("Successfully added friend: " + user);
        }
    }
    public void removeFriend(User user) {
        friendsList.remove(user);
        ClientApplication.LOGGER.info("Successfully removed friend: " + user);
    }

    @Override
    public String toString() {
        return this.getClass().getName()
                + " [id=" + id
                + ", nickname=" + nickname
                + ", gender=" + gender
                + ", online=" + online
                + "]";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    // TODO: make password modifiable
//    public void setRawPassword(String rawPassword) {
//        this.rawPassword = rawPassword;
//    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
        if ( online ) {
            ClientApplication.LOGGER.info(this + " " + "is now online.");
        } else {
            ClientApplication.LOGGER.info(this + " " + "is now offline.");
        }
        // TODO: user online server broadcast
    }

    public List<User> getFriendsList() {
        // TODO: debug
        friendsList.add(new User(10086, "DEBUG_USER", "111")); // TODO this user is for debugging
        return friendsList;
    }
}