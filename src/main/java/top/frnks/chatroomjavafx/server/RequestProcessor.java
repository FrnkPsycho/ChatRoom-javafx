package top.frnks.chatroomjavafx.server;

import top.frnks.chatroomjavafx.common.model.entity.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

public class RequestProcessor implements Runnable {
    public static final Logger LOGGER = ServerApplication.LOGGER;
    private Socket currentClientSocket;
    private Request request;

    public RequestProcessor(Socket currentClientSocket) {
        this.currentClientSocket = currentClientSocket;
    }

    @Override
    public void run() {
        LOGGER.info("Started Request Listener and Processor for: " + currentClientSocket.getRemoteSocketAddress());



        boolean listening = true;
        try {
            OnlineClientIOCache currentClientIOCache = new OnlineClientIOCache(
                    new ObjectInputStream(currentClientSocket.getInputStream()),
                    new ObjectOutputStream(currentClientSocket.getOutputStream())
            );

//            // TODO: debug
//            Response test = new Response();
//            test.setResponseType(ResponseType.BROADCAST);
//            test.setResponseStatus(ResponseStatus.OK);
//            test.setData("ASDSADAS", 111);
//            sendResponse(currentClientIOCache, test);
//            LOGGER.info("Sent test response");

            while (listening) {
                request = (Request) currentClientIOCache.getObjectInputStream().readObject();
                ActionType actionType = request.getAction();
                LOGGER.info("Received Request: " + actionType + " from " + currentClientSocket.getRemoteSocketAddress());
                switch (actionType) {
                    case CHAT -> chat();
                    case FRIEND_REQUEST -> friendRequest();
                    case LOGIN -> login();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void login() throws IOException {
        String id = (String) request.getAttribute("id");
        String password = (String) request.getAttribute("password");
        // TODO: check database

        Response response = new Response();
        // TODO: login response

        Message msg = (Message) request.getAttribute("Message");

    }

    private void chat() throws IOException {
        Message msg = (Message) request.getAttribute("Message");
        Response response = new Response();
        response.setResponseStatus(ResponseStatus.OK);
        response.setResponseType(ResponseType.CHAT);
        response.setData("Chat", msg);


        if ( msg.getToUser() != null ) { // private chat
            OnlineClientIOCache from = ServerDataBuffer.onlineClientIOCacheMap.get(msg.getFromUser().getId());
            OnlineClientIOCache to = ServerDataBuffer.onlineClientIOCacheMap.get(msg.getToUser().getId());
            sendResponse(from, response);
            sendResponse(to, response);
        } else { // public chat
            for ( Long id: ServerDataBuffer.onlineClientIOCacheMap.keySet() ) {
                sendResponse(ServerDataBuffer.onlineClientIOCacheMap.get(id), response);
            }
            LOGGER.info("Broadcast response: " + response.getResponseType());
//            broadcastResponse(response);
        }
    }

    private void friendRequest() throws IOException {
        Response response = new Response();
        response.setResponseStatus(ResponseStatus.OK);
        response.setResponseType(ResponseType.FRIEND_REQUEST);
        Message msg = (Message) request.getAttribute("Message");
        response.setData("FriendRequest", msg);

        LOGGER.info("Receive Friend Request from " + msg.getFromUser() + " To " + msg.getToUser());

        OnlineClientIOCache ioCache = ServerDataBuffer.onlineClientIOCacheMap.get(msg.getToUser().getId());
        sendResponse(ioCache, response);
    }

    private void sendResponse(OnlineClientIOCache ioCache, Response response) throws IOException {
        ObjectOutputStream oos = ioCache.getObjectOutputStream();
        oos.writeObject(response);
        oos.flush();
        LOGGER.info("Successfully sent response: " + response + " to " + ioCache);
    }

    private void broadcastResponse(Response response) throws IOException {
        for ( var onlineUserIO : ServerDataBuffer.onlineClientIOCacheMap.values()) {
            ObjectOutputStream oos = onlineUserIO.getObjectOutputStream();
            oos.writeObject(response);
            oos.flush();
        }
        LOGGER.info("Successfully broadcast response: " + response);
    }
}
