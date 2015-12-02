package com.eugeniuparvan.multiplayer.client.observers;

import com.eugeniuparvan.multiplayer.core.event.IEvent;
import com.eugeniuparvan.multiplayer.core.event.server.internal.UserEvent;

public class OnUserConnected extends ClientEventListener
{
    private static final long serialVersionUID = 1L;
    
    public OnUserConnected()
    {
	addListener(UserEvent.USER_CONNECTED);
    }
    
    @Override
    public void onEvent(IEvent event)
    {
    
    }
    
}
