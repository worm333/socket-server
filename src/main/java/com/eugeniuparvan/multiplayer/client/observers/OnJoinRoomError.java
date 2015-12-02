package com.eugeniuparvan.multiplayer.client.observers;

import com.eugeniuparvan.multiplayer.core.event.IEvent;
import com.eugeniuparvan.multiplayer.core.event.server.ServerEvent;

public class OnJoinRoomError extends ClientEventListener
{
    private static final long serialVersionUID = 7114448412276114050L;
    
    public OnJoinRoomError()
    {
	addListener(ServerEvent.ON_JOIN_ROOM_ERROR);
    }
    
    @Override
    public void onEvent(IEvent event)
    {
	// TODO Auto-generated method stub
	
    }
    
}
