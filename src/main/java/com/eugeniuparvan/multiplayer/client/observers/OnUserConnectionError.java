package com.eugeniuparvan.multiplayer.client.observers;

import com.eugeniuparvan.multiplayer.core.event.IEvent;
import com.eugeniuparvan.multiplayer.core.event.server.internal.UserEvent;

public class OnUserConnectionError extends ClientEventListener
{
    private static final long serialVersionUID = -3570242852418232549L;
    
    public OnUserConnectionError()
    {
	addListener(UserEvent.USER_CONNECTION_ERROR);
    }
    
    @Override
    public void onEvent(IEvent event)
    {
	// TODO Auto-generated method stub
	
    }
    
}
