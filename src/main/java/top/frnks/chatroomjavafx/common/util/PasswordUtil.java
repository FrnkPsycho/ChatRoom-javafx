package top.frnks.chatroomjavafx.common.util;


import javafx.util.Pair;
import top.frnks.chatroomjavafx.client.ClientDataBuffer;
import top.frnks.chatroomjavafx.client.ClientProperties;
import top.frnks.chatroomjavafx.common.model.entity.User;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;

public class PasswordUtil {
    public static Pair<byte[], String> digestPassword(String rawPassword) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        digest.update(salt);

        byte[] hashedPassword = digest.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
        return new Pair<>(salt, HexFormat.of().formatHex(hashedPassword));
    }

    public static boolean verifyPassword(byte[] salt, String rawPassword, String hashPassword) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        digest.update(salt);

        return hashPassword.equals(HexFormat.of().formatHex(digest.digest(rawPassword.getBytes(StandardCharsets.UTF_8))));

    }
}
