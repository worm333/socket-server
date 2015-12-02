package com.eugeniuparvan.multiplayer.client.observers;

import com.eugeniuparvan.multiplayer.core.event.IEvent;
import com.eugeniuparvan.multiplayer.core.event.server.ServerEvent;

public class OnJoinRoom extends ClientEventListener
{
    private static final long serialVersionUID = 6235354917145782863L;
    
    public OnJoinRoom()
    {
	addListener(ServerEvent.ON_JOIN_ROOM);
    }
    
    @Override
    public void onEvent(IEvent event)
    {
	// TODO Auto-generated method stub
	
    }
    
}
