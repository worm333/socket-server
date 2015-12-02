package com.eugeniuparvan.multiplayer.server;

import java.util.Set;

import com.eugeniuparvan.multiplayer.core.entity.IRoom;
import com.eugeniuparvan.multiplayer.core.entity.IUser;

public interface IServer
{
    public void start();
    
    public void stop();
    
    public long generateUserId();
    
    public long generateRoomId();
    
    public IRoom getRoom(Long roomId);
    
    public IConnection getConnection(Long userId);
    
    public Set<IConnection> getConnections();
    
    public Set<IRoom> getRooms();
    
    public void removeConnection(IConnection connection);
    
    public IRoom getDefaultRoom();
    
    public IRoom createRoom(String name);
    
    public IRoom createRoom(String name, String password);
    
    public IRoom joinRoom(long roomId, IUser user, String password);
    
}
