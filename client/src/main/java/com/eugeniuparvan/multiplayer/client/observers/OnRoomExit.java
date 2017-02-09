package com.eugeniuparvan.multiplayer.client.observers;

import com.eugeniuparvan.multiplayer.core.event.server.ServerResponseEvent;

/**
 * Created by eugeniuparvan on 2/9/17.
 */
public abstract class OnRoomExit extends ClientEventListener {

    private static final long serialVersionUID = 1L;

    public OnRoomExit() {
        addListener(ServerResponseEvent.ON_ROOM_EXIT);
    }
}
