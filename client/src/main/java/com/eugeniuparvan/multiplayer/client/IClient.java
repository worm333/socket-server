package com.eugeniuparvan.multiplayer.client;

import com.eugeniuparvan.multiplayer.core.Observer;
import com.eugeniuparvan.multiplayer.core.entity.IUser;
import com.eugeniuparvan.multiplayer.core.event.IEvent;

public interface IClient {

    void start(String host, int port);

    void stop();

    IUser getUser();

    void setUser(IUser user);

    void addObserver(Observer observer);

    void deleteObserver(Observer observer);

    void sendEvent(IEvent event);

    void getUsers(long roomId);

    void getRooms();

    void sendPrivateMessage(Long userId, String message);

    void sendPublicMessage(String message);

    void getJoinedRooms();

    void createRoom(String roomName, String password);

    void joinRoom(Long roomId, String password);

    void exitRoom(Long roomId);
}
