package com.eugeniuparvan.multiplayer.client.observers;

import com.eugeniuparvan.multiplayer.core.event.server.ServerResponseEvent;

public abstract class OnJoinRoomError extends ClientEventListener {

    private static final long serialVersionUID = 1L;

    public OnJoinRoomError() {
        addListener(ServerResponseEvent.ON_JOIN_ROOM_ERROR);
    }
}
