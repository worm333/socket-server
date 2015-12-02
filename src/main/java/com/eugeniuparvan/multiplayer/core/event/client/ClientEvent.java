package com.eugeniuparvan.multiplayer.core.event.client;

import com.eugeniuparvan.multiplayer.core.event.Event;
import com.eugeniuparvan.multiplayer.core.event.IEventParams;

public class ClientEvent extends Event
{
    private static final long serialVersionUID = 1593306585691445246L;
    
    public static final String GET_USER_LIST = "GET_USER_LIST";
    public static final String GET_ROOM_LIST = "GET_ROOM_LIST";
    public static final String SEND_PRIVATE_MESSAGE = "SEND_PRIVATE_MESSAGE";
    public static final String SEND_PUBLIC_MESSAGE = "SEND_PUBLIC_MESSAGE";
    public static final String CREATE_ROOM = "CREATE_ROOM";
    public static final String JOIN_ROOM = "JOIN_ROOM";
    public static final String SEND_ROOM_EXTENSION_MESSAGE = "SEND_ROOM_EXTENSION_MESSAGE";
    
    public ClientEvent(String type, IEventParams params)
    {
	super(type, params);
    }
}