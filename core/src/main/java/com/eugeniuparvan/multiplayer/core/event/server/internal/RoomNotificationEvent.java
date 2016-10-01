package com.eugeniuparvan.multiplayer.core.event.server.internal;

import com.eugeniuparvan.multiplayer.core.event.Event;
import com.eugeniuparvan.multiplayer.core.event.IEventParams;

public class RoomNotificationEvent extends Event {

    public static final String USER_ENTERED_ROOM = "USER_ENTERED_ROOM";

    public static final String USER_EXITED_ROOM = "USER_EXITED_ROOM";

    public static final String ROOM_VARIABLE_UPDATE = "ROOM_VARIABLE_UPDATED";

    private static final long serialVersionUID = 1L;

    public RoomNotificationEvent(String type, IEventParams params) {
        super(type, params);
    }
}
