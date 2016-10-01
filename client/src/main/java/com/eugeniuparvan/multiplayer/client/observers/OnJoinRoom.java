package com.eugeniuparvan.multiplayer.client.observers;

import com.eugeniuparvan.multiplayer.core.event.server.ServerResponseEvent;

public abstract class OnJoinRoom extends ClientEventListener {

    private static final long serialVersionUID = 1L;

    public OnJoinRoom() {
        addListener(ServerResponseEvent.ON_JOIN_ROOM);
    }
}
