package com.eugeniuparvan.multiplayer.client.observers;

import com.eugeniuparvan.multiplayer.core.event.server.ServerResponseEvent;

public abstract class OnUserList extends ClientEventListener {

    private static final long serialVersionUID = 1L;

    public OnUserList() {
        addListener(ServerResponseEvent.ON_USER_LIST);
    }
}
