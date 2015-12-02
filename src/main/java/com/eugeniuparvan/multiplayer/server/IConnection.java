package com.eugeniuparvan.multiplayer.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.eugeniuparvan.multiplayer.core.entity.IUser;
import com.eugeniuparvan.multiplayer.core.event.IEvent;

public interface IConnection extends Runnable
{
    public Socket getSocket();
    
    public IServer gerServer();
    
    public ObjectInputStream getInputStream();
    
    public ObjectOutputStream getOutputStream();
    
    public IUser getUser();
    
    public void sendToClient(IEvent event);
}
