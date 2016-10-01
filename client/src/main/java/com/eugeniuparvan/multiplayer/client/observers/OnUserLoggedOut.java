package com.eugeniuparvan.multiplayer.client.observers;

import com.eugeniuparvan.multiplayer.core.event.server.internal.UserNotificationEvent;

public abstract class OnUserLoggedOut extends ClientEventListener {

    private static final long serialVersionUID = 1L;

    public OnUserLoggedOut() {
        addListener(UserNotificationEvent.USER_LOGGED_OUT);
    }
}
