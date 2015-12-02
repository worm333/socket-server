package com.eugeniuparvan.multiplayer.client;

import com.eugeniuparvan.multiplayer.core.Observer;
import com.eugeniuparvan.multiplayer.core.entity.IUser;
import com.eugeniuparvan.multiplayer.core.event.IEvent;

public interface IClient
{
    public void start(String host, int port);
    
    public void stop();
    
    public void setUser(IUser user);
    
    public IUser getUser();
    
    public void addObserver(Observer observer);
    
    public void sendEvent(IEvent event);
    
    public void getUsers();
    
    public void getRooms();
    
    public void sendPrivateMessage(Long userId, String message);
    
    public void sendPublicMessage(String message);
    
    public void createRoom(String roomName, String password);
    
    public void joinRoom(Long roomId, String password);
}
