package com.eugeniuparvan.multiplayer.client.observers;

import com.eugeniuparvan.multiplayer.core.event.server.ServerResponseEvent;

public abstract class OnMessage extends ClientEventListener {

    private static final long serialVersionUID = 1L;

    public OnMessage() {
        addListener(ServerResponseEvent.ON_MESSAGE);
    }
}
