package com.eugeniuparvan.multiplayer.client.observers;

import com.eugeniuparvan.multiplayer.core.event.server.internal.RoomNotificationEvent;

public abstract class OnUserExitedRoom extends ClientEventListener {

    private static final long serialVersionUID = 1L;

    public OnUserExitedRoom() {
        addListener(RoomNotificationEvent.USER_EXITED_ROOM);
    }
}
