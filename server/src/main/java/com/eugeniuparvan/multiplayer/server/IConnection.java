package com.eugeniuparvan.multiplayer.server;

import com.eugeniuparvan.multiplayer.core.entity.IUser;
import com.eugeniuparvan.multiplayer.core.event.IEvent;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public interface IConnection extends Runnable {

    Socket getSocket();

    IServer gerServer();

    ObjectInputStream getInputStream();

    ObjectOutputStream getOutputStream();

    IUser getUser();

    void sendToClient(IEvent event);
}
