package com.eugeniuparvan.multiplayer.client.observers;

import com.eugeniuparvan.multiplayer.core.event.server.internal.UserNotificationEvent;

public abstract class OnUserLoggedIn extends ClientEventListener {

    private static final long serialVersionUID = 1L;

    public OnUserLoggedIn() {
        addListener(UserNotificationEvent.USER_LOGGED_IN);
    }
}
