package top.frnks.chatroomjavafx.server;

import com.fasterxml.jackson.databind.ser.impl.ReadOnlyClassToSerializerMap;
import top.frnks.chatroomjavafx.common.model.entity.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

public class RequestProcessor implements Runnable {
    public static final Logger LOGGER = ServerApplication.LOGGER;
    private final Socket currentClientSocket;
    private Request request;
    private UserService userService;

    public RequestProcessor(Socket currentClientSocket) {
        this.currentClientSocket = currentClientSocket;
    }

    @Override
    public void run() {
        LOGGER.info("Started Request Listener and Processor for: " + currentClientSocket.getRemoteSocketAddress());
        userService = new UserService();


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
                    case LOGIN -> login(currentClientIOCache);
                    case SIGNUP -> signup(currentClientIOCache);
                    case LOGOUT -> listening = logout();
                }
                LOGGER.info("Sent Response to Client " + currentClientSocket.getRemoteSocketAddress());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean logout() throws IOException {
        User user = (User) request.getAttribute("user");
        user.setOnline(false);

        ServerDataBuffer.onlineUsersMap.remove(user.getId());
        ServerDataBuffer.onlineClientIOCacheMap.remove(user.getId());

        Response response = new Response();
        response.setResponseType(ResponseType.LOGOUT);
        response.setResponseStatus(ResponseStatus.OK);
        response.setData("user", user);
        broadcastResponse(response);

        currentClientSocket.close();
        return false;
    }
    private void signup(OnlineClientIOCache currentClientIO) throws IOException {
//        User user = (User) request.getAttribute("user");
//        user = userService.signup(user);
//        userService.addUser(user);
        String password = (String) request.getAttribute("password");
        String nickname = (String) request.getAttribute("nickname");
        User user = userService.signup(nickname, password);

        Response response = new Response();
        response.setResponseType(ResponseType.SIGNUP);
        response.setResponseStatus(ResponseStatus.OK);
        response.setData("user", user);

        currentClientIO.getObjectOutputStream().writeObject(response);
        currentClientIO.getObjectOutputStream().flush();
        LOGGER.info("Sent signup response to client"); // FIXME: debug line
    }

    private void login(OnlineClientIOCache currentClientIO) throws IOException {
        String password = (String) request.getAttribute("password");
        String id = (String) request.getAttribute("id");
        String nickname = (String) request.getAttribute("nickname");
        User user;
        if ( !id.isBlank() ) {
            user = userService.login(Long.parseLong(id), password);
        } else {
            user = userService.login(nickname, password);
        }

        Response response = new Response();
        response.setResponseStatus(ResponseStatus.OK);
        if ( user == null ) {
            // User not found, invalid id or password
            response.setResponseType(ResponseType.INVALID_LOGIN);
        } else if ( ServerDataBuffer.onlineUsersMap.containsKey(user.getId()) ) {
            // User is already logon
            response.setResponseType(ResponseType.ALREADY_LOGON);
        } else {
            response.setResponseType(ResponseType.LOGIN);
            response.setResponseStatus(ResponseStatus.OK);
            // Successfully logon
            ServerDataBuffer.onlineUsersMap.put(user.getId(), user);

            // add login user to cache map
            ServerDataBuffer.onlineClientIOCacheMap.put(user.getId(), currentClientIO);

            // response to login user
            response.setData("onlineUsers", new CopyOnWriteArrayList<>(ServerDataBuffer.onlineUsersMap.values()));
            response.setData("user", user);
            currentClientIO.getObjectOutputStream().writeObject(response);
            currentClientIO.getObjectOutputStream().flush();

            // broadcast online
            Response response1 = new Response();
            response1.setResponseType(ResponseType.BROADCAST);
            response1.setResponseStatus(ResponseStatus.OK);
            response1.setData("loginUser", user);
            response1.setData("onlineUsers", new CopyOnWriteArrayList<>(ServerDataBuffer.onlineUsersMap.values()));
            broadcastResponse(response1);


        }

        currentClientIO.getObjectOutputStream().writeObject(response);
        currentClientIO.getObjectOutputStream().flush();
    }

    private void chat() throws IOException {
        Message msg = (Message) request.getAttribute("msg");
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
            broadcastResponse(response);
        }
    }

    private void friendRequest() throws IOException {
        Response response = new Response();
        response.setResponseStatus(ResponseStatus.OK);
        response.setResponseType(ResponseType.FRIEND_REQUEST);
        Message msg = (Message) request.getAttribute("msg");
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
