package com.eugeniuparvan.multiplayer.client;

import com.eugeniuparvan.multiplayer.core.Observer;
import com.eugeniuparvan.multiplayer.core.entity.IUser;
import com.eugeniuparvan.multiplayer.core.entity.SerializableObject;
import com.eugeniuparvan.multiplayer.core.event.EventParams;
import com.eugeniuparvan.multiplayer.core.event.IEvent;
import com.eugeniuparvan.multiplayer.core.event.IEventParams;
import com.eugeniuparvan.multiplayer.core.event.client.ClientRequestEvent;
import org.apache.log4j.Logger;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Client implements IClient {

    private static final Logger logger = Logger.getLogger(Client.class);

    private Socket socket;

    private ObjectInputStream in;

    private ObjectOutputStream out;

    private ExecutorService executor;

    private ClientReceiver receiver;

    private IUser user;

    public void start(String host, int port) {
        executor = Executors.newCachedThreadPool();

        try {
            socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            receiver = new ClientReceiver(in);
            executor.submit(receiver);
        } catch (Exception e) {
            logger.info("Starting client fatal error", e);
        }
    }

    public void stop() {
        releaseResources();

        try {
            if (executor.awaitTermination(10, TimeUnit.SECONDS))
                logger.info("Shutting down client: task completed");
            else
                logger.info("Shutting down client: Forcing shutdown...");
            executor.shutdownNow();
        } catch (InterruptedException e) {
            logger.error("TODO: nice message", e);// TODO: nice message
        }
        logger.info("client is stopped.");
    }

    @Override
    public void addObserver(Observer observer) {
        receiver.addObserver(observer);
    }

    @Override
    public void deleteObserver(Observer observer) {
        receiver.deleteObserver(observer);
    }

    @Override
    public IUser getUser() {
        return user;
    }

    @Override
    public void setUser(IUser user) {
        this.user = user;
    }

    @Override
    public void sendEvent(IEvent event) {
        executor.submit(new ClientSender(event, out));
    }

    @Override
    public void getUsers(long roomId) {
        IEventParams params = new EventParams();
        params.putParam("roomId", new SerializableObject<>(roomId));
        IEvent event = new ClientRequestEvent(ClientRequestEvent.GET_USER_LIST, params);
        sendEvent(event);
    }

    @Override
    public void getRooms() {
        IEventParams params = new EventParams();
        IEvent event = new ClientRequestEvent(ClientRequestEvent.GET_ROOM_LIST, params);
        sendEvent(event);
    }

    @Override
    public void sendPrivateMessage(Long userId, String message) {
        IEventParams params = new EventParams();
        params.putParam("userId", new SerializableObject<>(userId));
        params.putParam("message", new SerializableObject<>(message));
        IEvent event = new ClientRequestEvent(ClientRequestEvent.SEND_PRIVATE_MESSAGE, params);

        sendEvent(event);
    }

    @Override
    public void sendPublicMessage(String message) {
        IEventParams params = new EventParams();
        params.putParam("message", new SerializableObject<>(message));
        IEvent event = new ClientRequestEvent(ClientRequestEvent.SEND_PUBLIC_MESSAGE, params);

        sendEvent(event);
    }

    @Override
    public void createRoom(String roomName, String password) {
        IEventParams params = new EventParams();
        params.putParam("name", new SerializableObject<>(roomName));
        params.putParam("password", new SerializableObject<>(password));
        IEvent event = new ClientRequestEvent(ClientRequestEvent.CREATE_ROOM, params);

        sendEvent(event);
    }

    @Override
    public void getJoinedRooms() {
        IEventParams params = new EventParams();
        IEvent event = new ClientRequestEvent(ClientRequestEvent.GET_JOINED_ROOMS, params);

        sendEvent(event);
    }

    @Override
    public void joinRoom(Long roomId, String password) {
        IEventParams params = new EventParams();
        params.putParam("roomId", new SerializableObject<>(roomId));
        params.putParam("password", new SerializableObject<>(password));
        IEvent event = new ClientRequestEvent(ClientRequestEvent.JOIN_ROOM, params);

        sendEvent(event);
    }

    @Override
    public void exitRoom(Long roomId) {
        IEventParams params = new EventParams();
        params.putParam("roomId", new SerializableObject<>(roomId));
        IEvent event = new ClientRequestEvent(ClientRequestEvent.EXIT_ROOM, params);

        sendEvent(event);
    }

    private void releaseResources() {
        close(socket);
        close(in);
        close(out);
        receiver.deleteObservers();
    }

    private void close(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException e) {
            logger.error("TODO: nice message", e);// TODO: nice message
        }
    }
}
