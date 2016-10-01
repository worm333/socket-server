package com.eugeniuparvan.multiplayer.client.observers;

import com.eugeniuparvan.multiplayer.core.event.server.ServerResponseEvent;

public abstract class OnRoomCreated extends ClientEventListener {

    private static final long serialVersionUID = 1L;

    public OnRoomCreated() {
        addListener(ServerResponseEvent.ON_ROOM_CREATED);
    }

}
