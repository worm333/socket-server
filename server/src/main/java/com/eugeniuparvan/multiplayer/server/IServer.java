package com.eugeniuparvan.multiplayer.server;

import com.eugeniuparvan.multiplayer.core.entity.IRoom;
import com.eugeniuparvan.multiplayer.core.entity.IUser;

import java.util.Set;

public interface IServer {

    void start();

    void stop();

    long generateUserId();

    long generateRoomId();

    IRoom getRoom(Long roomId);

    IConnection getConnection(Long userId);

    Set<IConnection> getConnections();

    Set<IRoom> getRooms();

    void removeConnection(IConnection connection);

    IRoom getDefaultRoom();

    IRoom createRoom(String name);

    IRoom createRoom(String name, String password);

    IRoom joinRoom(long roomId, IUser user, String password);

    IRoom exitRoom(long roomId, IUser user);

}
