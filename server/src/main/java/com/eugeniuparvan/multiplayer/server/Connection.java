package com.eugeniuparvan.multiplayer.server;

import com.eugeniuparvan.multiplayer.core.Observable;
import com.eugeniuparvan.multiplayer.core.Observer;
import com.eugeniuparvan.multiplayer.core.entity.*;
import com.eugeniuparvan.multiplayer.core.event.EventParams;
import com.eugeniuparvan.multiplayer.core.event.IEvent;
import com.eugeniuparvan.multiplayer.core.event.client.ClientRequestEvent;
import com.eugeniuparvan.multiplayer.core.event.server.ServerResponseEvent;
import com.eugeniuparvan.multiplayer.core.event.server.internal.RoomNotificationEvent;
import com.eugeniuparvan.multiplayer.core.event.server.internal.UserNotificationEvent;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Connection implements IConnection, Observer {

    private static final long serialVersionUID = 1L;

    private final Logger logger = Logger.getLogger(Connection.class);

    private final Socket socket;

    private final IServer server;

    private final IUser user;

    private final ObjectInputStream in;

    private final ObjectOutputStream out;

    public Connection(Socket socket, IServer server) throws IOException {
        this.socket = socket;
        this.server = server;
        this.user = new User(server.generateUserId());
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());

        ((Observable) user).addObserver(this);
    }

    @Override
    public void run() {
        boolean keepGoing = true;

        sendOnConnectedEvent();
        joinRoom(server.getDefaultRoom().getId(), null);

        while (keepGoing) {
            try {
                IEvent event = (IEvent) in.readObject();
                switch (event.getType()) {
                    case ClientRequestEvent.CREATE_ROOM: {
                        createRoom(event);
                        break;
                    }
                    case ClientRequestEvent.GET_ROOM_LIST: {
                        retrieveRoomList();
                        break;
                    }
                    case ClientRequestEvent.GET_JOINED_ROOMS: {
                        retrieveJoinedRooms();
                        break;
                    }
                    case ClientRequestEvent.GET_USER_LIST: {
                        retrieveUserList(event);
                        break;
                    }
                    case ClientRequestEvent.JOIN_ROOM: {
                        joinRoom(event);
                        break;
                    }
                    case ClientRequestEvent.EXIT_ROOM: {
                        exitRoom(event);
                        break;
                    }
                    case ClientRequestEvent.SEND_PRIVATE_MESSAGE: {
                        sendPrivateMessage(event);
                        break;
                    }
                    case ClientRequestEvent.SEND_PUBLIC_MESSAGE: {
                        sendPublicMessage(event);
                        break;
                    }
                    case ClientRequestEvent.SEND_ROOM_EXTENSION_MESSAGE: {
                        //TODO: to implement
                        break;
                    }
                    case ClientRequestEvent.SET_ROOM_VARIABLE: {
                        //TODO: to implement
                        break;
                    }
                    case ClientRequestEvent.REMOVE_ROOM_VARIABLE: {
                        //TODO: to implement
                        break;
                    }
                    case ClientRequestEvent.SET_USER_VARIABLE: {
                        //TODO: to implement
                        break;
                    }
                    case ClientRequestEvent.REMOVE_USER_VARIABLE: {
                        //TODO: to implement
                        break;
                    }
                }
            } catch (IOException e) {
                logger.info("Disconnecting user: " + user.getId() + " ....");// TODO: nice message
                break;
            } catch (Exception e) {
                logger.error("TODO: nice message", e);// TODO: nice message
            }
        }
        releaseResources();
    }

    @Override
    public void update(Observable observable, Object argument) {
        IUser u;
        IEvent event = (IEvent) argument;
        Set<IUser> users = new HashSet<>();
        switch (event.getType()) {
            case RoomNotificationEvent.USER_ENTERED_ROOM:
                u = (IUser) event.getParams().getParam("user").getObject();
                if (!user.equals(u)) //sends event only if other user has entered the room
                    users.add(user);
                logger.info(event);
                break;
            case RoomNotificationEvent.USER_EXITED_ROOM:
                u = (IUser) event.getParams().getParam("user").getObject();
                if (!user.equals(u)) //sends event only if other user has exited the room
                    users.add(user);
                logger.info(event);
                break;
            case RoomNotificationEvent.ROOM_VARIABLE_UPDATE:
                users.add(user);
                logger.info(event);
                break;
            case UserNotificationEvent.USER_VARIABLE_UPDATE:
                users = getAllUsersFromJoinedRooms(user);
                logger.info(event);
                break;
        }

        sendEventToAll(event, users);
    }

    @Override
    public Socket getSocket() {
        return socket;
    }

    @Override
    public ObjectInputStream getInputStream() {
        return in;
    }

    @Override
    public ObjectOutputStream getOutputStream() {
        return out;
    }

    @Override
    public IUser getUser() {
        return user;
    }

    @Override
    public IServer gerServer() {
        return server;
    }

    @Override
    public void sendToClient(IEvent event) {
        try {
            out.writeObject(event);
        } catch (IOException e) {
            logger.error("Sending event to client error", e);
        }

    }

    private void sendOnConnectedEvent() {
        EventParams params = new EventParams();
        params.putParam("user", new SerializableObject<>(user));
        sendToClient(new UserNotificationEvent(UserNotificationEvent.USER_CONNECTED, params));
    }

    /**
     * Creates room with provided name and password
     *
     * @param event must contain: name and password params
     */
    private void createRoom(IEvent event) {
        String roomName = (String) event.getParams().getParam("name").getObject();
        String password = (String) event.getParams().getParam("password").getObject();
        IRoom room = server.createRoom(roomName, password);

        EventParams params = new EventParams();
        params.putParam("room", new SerializableObject<>((Room) room));
        sendToClient(new ServerResponseEvent(ServerResponseEvent.ON_ROOM_CREATED, params));
    }

    /**
     * Retrieves room list
     */
    private void retrieveRoomList() {
        EventParams params = new EventParams();
        params.putParam("rooms",
                new SerializableObject<>((Serializable) server.getRooms()));
        sendToClient(new ServerResponseEvent(ServerResponseEvent.ON_ROOM_LIST, params));
    }

    /**
     * Retrieves room list
     */
    private void retrieveJoinedRooms() {
        EventParams params = new EventParams();
        params.putParam("rooms",
                new SerializableObject<>((Serializable) user.getJoinedRooms()));
        sendToClient(new ServerResponseEvent(UserNotificationEvent.USER_JOINED_ROOMS, params));
    }

    /**
     * Retrieves user list
     *
     * @param event must contain: roomId param
     */
    private void retrieveUserList(IEvent event) {
        Long roomId = (Long) event.getParams().getParam("roomId").getObject();
        EventParams params = new EventParams();

        IRoom room = user.getRoom(roomId);
        if (room != null)
            params.putParam("users", new SerializableObject<>((Serializable) room.getUsers()));
        else
            params.putParam("users", new SerializableObject<>((Serializable) new HashSet<IUser>()));
        sendToClient(new ServerResponseEvent(ServerResponseEvent.ON_USER_LIST, params));
    }

    /**
     * Joins room with provided roomId and password
     *
     * @param event must contain: roomId and password params
     */
    private void joinRoom(IEvent event) {
        Long roomId = (Long) event.getParams().getParam("roomId").getObject();
        String roomPassword = (String) event.getParams().getParam("password").getObject();
        joinRoom(roomId, roomPassword);
    }

    /**
     * Joins room with provided roomId and password
     *
     * @param roomId
     * @param roomPassword
     */
    private void joinRoom(Long roomId, String roomPassword) {
        IRoom room = server.joinRoom(roomId, user, roomPassword);

        EventParams params = new EventParams();
        params.putParam("room", new SerializableObject<>(room));
        if (room != null)
            sendToClient(new ServerResponseEvent(ServerResponseEvent.ON_JOIN_ROOM, params));
        else
            sendToClient(new ServerResponseEvent(ServerResponseEvent.ON_JOIN_ROOM_ERROR, params));
    }

    /**
     * Exits from room with provided roomId
     *
     * @param event must contain: roomId param
     */
    private void exitRoom(IEvent event) {
        Long roomId = (Long) event.getParams().getParam("roomId").getObject();
        server.exitRoom(roomId, user);
    }

    /**
     * Sends private message to specified user
     *
     * @param event must contain: userId and message params
     */
    private void sendPrivateMessage(IEvent event) {
        Long userId = (Long) event.getParams().getParam("userId").getObject();
        String message = (String) event.getParams().getParam("message").getObject();

        EventParams params = new EventParams();
        params.putParam("message", new SerializableObject<>(message));
        params.putParam("user", new SerializableObject<>(user));
        IConnection connection = server.getConnection(userId);
        connection.sendToClient(new ServerResponseEvent(ServerResponseEvent.ON_PRIVATE_MESSAGE, params));
    }

    /**
     * Sends public message to all users in the same room with sender
     *
     * @param event must contain: message param
     */
    private void sendPublicMessage(IEvent event) {
        String message = (String) event.getParams().getParam("message").getObject();

        EventParams params = new EventParams();
        params.putParam("message", new SerializableObject<>(message));
        params.putParam("user", new SerializableObject<>(user));

        Set<IUser> users = getAllUsersFromJoinedRooms(user);
        IEvent e = new ServerResponseEvent(ServerResponseEvent.ON_PUBLIC_MESSAGE, params);

        sendEventToAll(e, users);
    }

    /**
     * Notifies all users in the same room that user has been disconnected
     */
    private void sendOnDisconnectedEvent() {
        EventParams params = new EventParams();
        params.putParam("user", new SerializableObject<>(user));

        Set<IUser> users = getAllUsersFromJoinedRooms(user);
        IEvent e = new UserNotificationEvent(UserNotificationEvent.USER_DISCONNECTED, params);

        sendEventToAll(e, users);
    }

    /**
     * @return all users from all rooms joined by current user
     */
    private Set<IUser> getAllUsersFromJoinedRooms(IUser user) {
        Set<IRoom> rooms = user.getJoinedRooms();
        Set<IUser> users = new HashSet<>();
        for (IRoom room : rooms) {
            users.addAll(room.getUsers());
        }
        users.remove(user);
        return users;
    }

    private void sendEventToAll(IEvent event, Set<IUser> users) {
        for (IUser u : users) {
            IConnection connection = server.getConnection(u.getId());
            connection.sendToClient(event);
            logger.debug(event);
        }
    }

    private void releaseResources() {
        // TODO: Test this!
        sendOnDisconnectedEvent();

        user.disconnect();
        server.removeConnection(this);

        close(out);
        close(in);
        close(socket);
    }

    private void close(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException e) {
            logger.error("TODO: nice message", e);// TODO: nice message
        }
    }
}
