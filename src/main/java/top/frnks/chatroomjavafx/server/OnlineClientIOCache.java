package top.frnks.chatroomjavafx.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class OnlineClientIOCache {
    private final ObjectInputStream objectInputStream;
    private final ObjectOutputStream objectOutputStream;

    public OnlineClientIOCache(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;
    }

    public ObjectInputStream getObjectInputStream() {
        return objectInputStream;
    }

    public ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }
}
