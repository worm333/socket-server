package com.eugeniuparvan.multiplayer.client.observers;

import com.eugeniuparvan.multiplayer.core.event.server.internal.RoomNotificationEvent;

public abstract class OnRoomVariableUpdated extends ClientEventListener {

    private static final long serialVersionUID = 1L;

    public OnRoomVariableUpdated() {
        addListener(RoomNotificationEvent.ROOM_VARIABLE_UPDATE);
    }
}
