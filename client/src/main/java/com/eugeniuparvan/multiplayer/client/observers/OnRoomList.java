package com.eugeniuparvan.multiplayer.client.observers;

import com.eugeniuparvan.multiplayer.core.event.server.ServerResponseEvent;

public abstract class OnRoomList extends ClientEventListener {

    private static final long serialVersionUID = 1L;

    public OnRoomList() {
        addListener(ServerResponseEvent.ON_ROOM_LIST);
    }
}
