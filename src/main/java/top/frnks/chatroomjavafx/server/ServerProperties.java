package top.frnks.chatroomjavafx.server;

import top.frnks.chatroomjavafx.client.ClientApplication;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Properties;

public class ServerProperties {
    private static final Properties properties = new Properties();
    static {
        loadProperties();
    }

    public static void loadProperties() {
        try {
            properties.load(ServerApplication.class.getClassLoader().getResourceAsStream("server.properties"));
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
                ServerApplication.class.getClassLoader().getResource("server.properties").getPath()
        ), "Properties created by program at " + LocalDateTime.now()
        );
    }
}
