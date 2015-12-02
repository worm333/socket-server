package com.eugeniuparvan.multiplayer.client.observers;

import com.eugeniuparvan.multiplayer.core.event.IEvent;
import com.eugeniuparvan.multiplayer.core.event.server.ServerEvent;

public class OnMessage extends ClientEventListener
{
    private static final long serialVersionUID = 5077202257021709064L;
    
    public OnMessage()
    {
	addListener(ServerEvent.ON_MESSAGE);
    }
    
    @Override
    public void onEvent(IEvent event)
    {
	// TODO Auto-generated method stub
	
    }
    
}
