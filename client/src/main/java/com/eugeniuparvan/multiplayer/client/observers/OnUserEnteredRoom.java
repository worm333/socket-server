package com.eugeniuparvan.multiplayer.client.observers;

import com.eugeniuparvan.multiplayer.core.event.server.internal.RoomNotificationEvent;

/**
 * IEvent contains: user and room params
 */
public abstract class OnUserEnteredRoom extends ClientEventListener {

    private static final long serialVersionUID = 1L;

    public OnUserEnteredRoom() {
        addListener(RoomNotificationEvent.USER_ENTERED_ROOM);
    }
}
