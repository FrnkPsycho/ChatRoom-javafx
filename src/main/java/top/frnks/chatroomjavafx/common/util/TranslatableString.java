package top.frnks.chatroomjavafx.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import top.frnks.chatroomjavafx.client.ClientApplication;
import top.frnks.chatroomjavafx.client.ClientSettings;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class TranslatableString {
    public final String identifier;
    public static String JSONContent;
    static {
        try (var JSONInputStream = ClientApplication.class.getClassLoader().getResourceAsStream("i18n/" + ClientSettings.clientLanguage + ".json")) {
            JSONContent = new String(JSONInputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public TranslatableString(String identifier) {
        this.identifier = identifier;

    }

    public String translate(){
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(JSONContent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return jsonNode.get(identifier).asText();
    }
}
