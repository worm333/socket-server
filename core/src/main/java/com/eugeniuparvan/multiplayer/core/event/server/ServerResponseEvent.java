package com.eugeniuparvan.multiplayer.core.event.server;

import com.eugeniuparvan.multiplayer.core.event.Event;
import com.eugeniuparvan.multiplayer.core.event.IEventParams;

public class ServerResponseEvent extends Event {

    public static final String ON_ROOM_LIST = "ON_ROOM_LIST";

    public static final String ON_ROOM_CREATED = "ON_ROOM_CREATED";

    public static final String ON_USER_LIST = "ON_USER_LIST";

    public static final String ON_JOIN_ROOM = "ON_JOIN_ROOM";

    public static final String ON_JOIN_ROOM_ERROR = "ON_JOIN_ROOM_ERROR";

    public static final String ON_PUBLIC_MESSAGE = "ON_PUBLIC_MESSAGE";

    public static final String ON_PRIVATE_MESSAGE = "ON_PRIVATE_MESSAGE";

    private static final long serialVersionUID = 2052790993186037269L;

    public ServerResponseEvent(String type, IEventParams params) {
        super(type, params);
    }

}
