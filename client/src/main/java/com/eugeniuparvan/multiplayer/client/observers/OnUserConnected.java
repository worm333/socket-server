package com.eugeniuparvan.multiplayer.client.observers;

import com.eugeniuparvan.multiplayer.core.event.server.internal.UserNotificationEvent;

/**
 * IEvent
 */
public abstract class OnUserConnected extends ClientEventListener {

    private static final long serialVersionUID = 1L;

    public OnUserConnected() {
        addListener(UserNotificationEvent.USER_CONNECTED);
    }
}
