package top.frnks.chatroomjavafx.server;

import top.frnks.chatroomjavafx.common.model.entity.ActionType;
import top.frnks.chatroomjavafx.common.model.entity.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RequestProcessor implements Runnable {
    private Socket currentClientSocket;
    private Request request;

    public RequestProcessor(Socket currentClientSocket) {
        this.currentClientSocket = currentClientSocket;
    }

    @Override
    public void run() {
        boolean listening = true;
        try {
            OnlineClientIOCache currentClientIOCache = new OnlineClientIOCache(
                    new ObjectInputStream(currentClientSocket.getInputStream()),
                    new ObjectOutputStream(currentClientSocket.getOutputStream())
            );
            while (listening) {
                request = (Request) currentClientIOCache.getObjectInputStream().readObject();
                ActionType actionType = request.getAction();
                switch (actionType) {
                    case CHAT -> chat();
                    case FRIEND_REQUEST -> friendRequest();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void chat() {

    }

    private void friendRequest() {

    }
}
