package com.eugeniuparvan.multiplayer.core.event.server.internal;

import com.eugeniuparvan.multiplayer.core.event.Event;
import com.eugeniuparvan.multiplayer.core.event.IEventParams;

public class UserNotificationEvent extends Event {

    public static final String USER_CONNECTED = "USER_CONNECTED";

    public static final String USER_LOGGED_IN = "USER_LOGGED_IN";

    public static final String USER_LOGGED_OUT = "USER_LOGGED_OUT";

    public static final String USER_DISCONNECTED = "USER_DISCONNECTED";

    public static final String USER_VARIABLE_UPDATE = "USER_VARIABLE_UPDATE";

    public static final String USER_JOINED_ROOMS = "USER_JOINED_ROOMS";

    private static final long serialVersionUID = 1L;

    public UserNotificationEvent(String type, IEventParams params) {
        super(type, params);
    }
}
