package com.eugeniuparvan.multiplayer.client.observers;

import com.eugeniuparvan.multiplayer.core.event.server.internal.UserNotificationEvent;

public abstract class OnUserDisconnected extends ClientEventListener {

    private static final long serialVersionUID = 1L;

    public OnUserDisconnected() {
        addListener(UserNotificationEvent.USER_DISCONNECTED);
    }

}
