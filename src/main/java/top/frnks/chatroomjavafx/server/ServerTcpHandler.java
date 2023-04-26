package top.frnks.chatroomjavafx.server;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ServerTcpHandler extends Thread {
    Socket socket;
    public ServerTcpHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (InputStream inputStream = this.socket.getInputStream() ) {
            try (OutputStream outputStream = this.socket.getOutputStream() ) {
                handle(inputStream, outputStream);
            }
        } catch (Exception e) {
            try {
                this.socket.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            ServerApplication.LOGGER.info("Connection Closed.");
        }
    }

    private void handle(InputStream inputStream, OutputStream outputStream) throws IOException {
        var writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        var reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
//        writer.write("hello\n");
//        writer.flush();
//        for (;;) {
//            String s = reader.readLine();
//            if (s.equals("bye")) {
//                writer.write("bye\n");
//                writer.flush();
//                break;
//            }
//            writer.write("ok: " + s + "\n");
//            writer.flush();
//        }

    }
}
