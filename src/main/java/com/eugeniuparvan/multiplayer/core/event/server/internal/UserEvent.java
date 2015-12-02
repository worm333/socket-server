package com.eugeniuparvan.multiplayer.core.event.server.internal;

import com.eugeniuparvan.multiplayer.core.event.Event;
import com.eugeniuparvan.multiplayer.core.event.IEventParams;

public class UserEvent extends Event
{
    private static final long serialVersionUID = -354465297972355949L;
    
    public static final String USER_CONNECTED = "USER_CONNECTED";
    public static final String USER_CONNECTION_ERROR = "USER_CONNECTION_ERROR";
    public static final String USER_LOGGED_IN = "USER_LOGGED_IN";
    public static final String USER_LOGGED_OUT = "USER_LOGGED_OUT";
    public static final String USER_DISCONNECTED = "USER_DISCONNECTED";
    public static final String USER_VARIABLE_UPDATE = "USER_VARIABLE_UPDATE";
    
    public UserEvent(String type, IEventParams params)
    {
	super(type, params);
    }
}
