package com.eugeniuparvan.multiplayer.server;

import com.eugeniuparvan.multiplayer.core.entity.IUser;
import com.eugeniuparvan.multiplayer.core.event.IEvent;

public interface IConnection extends Runnable {

    void stop();

    IUser getUser();

    void sendToClient(IEvent event);
}
