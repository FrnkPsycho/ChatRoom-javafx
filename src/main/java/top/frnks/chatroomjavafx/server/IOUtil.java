package top.frnks.chatroomjavafx.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtil {
    public static void close(InputStream inputStream) {
        if ( inputStream != null ) {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void close(OutputStream outputStream) {
        if ( outputStream != null ) {
            try {
                outputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void close(InputStream inputStream, OutputStream outputStream) {
        close(inputStream);
        close(outputStream);
    }
}
