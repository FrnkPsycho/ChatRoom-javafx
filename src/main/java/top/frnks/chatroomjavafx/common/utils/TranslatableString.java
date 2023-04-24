package top.frnks.chatroomjavafx.common.utils;

import top.frnks.chatroomjavafx.client.ClientApplication;
import top.frnks.chatroomjavafx.client.ClientLanguage;
import top.frnks.chatroomjavafx.client.ClientSettings;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class TranslatableString {
    public final String identifier;
    public static String JSONContent;
    static {
        try (var JSONInputStream = ClientApplication.class.getClassLoader().getResourceAsStream("assets/i18n/" + ClientSettings.clientLanguage + ".json")) {
            JSONContent = new String(JSONInputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public TranslatableString(String identifier) {
        this.identifier = identifier;
    }

    public String translate() {
        return "";
    }
}
