package com.eugeniuparvan.multiplayer.core.event.server.internal;

import com.eugeniuparvan.multiplayer.core.event.Event;
import com.eugeniuparvan.multiplayer.core.event.IEventParams;

public class RoomEvent extends Event
{
    public static final String USER_ENTERED_ROOM = "USER_ENTERED_ROOM";
    public static final String USER_EXITED_ROOM = "USER_EXITED_ROOM";
    public static final String ROOM_VAIABLE_UPDATE = "ROOM_VAIABLE_UPDATE";
    
    private static final long serialVersionUID = 6147596545836197048L;
    
    public RoomEvent(String type, IEventParams params)
    {
	super(type, params);
    }
}
