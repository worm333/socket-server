package com.eugeniuparvan.multiplayer.client.observers;

import com.eugeniuparvan.multiplayer.core.event.server.ServerResponseEvent;

public abstract class OnPrivateMessage extends ClientEventListener {

    private static final long serialVersionUID = 1L;

    public OnPrivateMessage() {
        addListener(ServerResponseEvent.ON_PRIVATE_MESSAGE);
    }
}
