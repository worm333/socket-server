package com.eugeniuparvan.multiplayer.client.observers;

import com.eugeniuparvan.multiplayer.core.event.server.internal.UserNotificationEvent;

public abstract class OnUserJoinedRooms extends ClientEventListener {

    private static final long serialVersionUID = 1L;

    public OnUserJoinedRooms() {
        addListener(UserNotificationEvent.USER_JOINED_ROOMS);
    }
}
