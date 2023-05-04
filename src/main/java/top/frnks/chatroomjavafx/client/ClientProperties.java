package top.frnks.chatroomjavafx.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Properties;

public class ClientProperties {
    private static final Properties properties = new Properties();
    static {
        loadProperties();
    }

    public static void loadProperties() {
        try {
            properties.load(ClientApplication.class.getClassLoader().getResourceAsStream("client.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    public static void saveProperty() throws IOException {
        properties.store(new FileOutputStream(
                ClientApplication.class.getClassLoader().getResource("client.properties").getPath()
        ), "Properties created by program at " + LocalDateTime.now()
        );
    }
}
