package com.eugeniuparvan.multiplayer.client.observers;

import com.eugeniuparvan.multiplayer.core.event.server.ServerResponseEvent;

public abstract class OnPublicMessage extends ClientEventListener {

    private static final long serialVersionUID = 1L;

    public OnPublicMessage() {
        addListener(ServerResponseEvent.ON_PUBLIC_MESSAGE);
    }
}
