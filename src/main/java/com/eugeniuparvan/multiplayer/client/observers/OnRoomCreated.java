package com.eugeniuparvan.multiplayer.client.observers;

import com.eugeniuparvan.multiplayer.core.event.IEvent;
import com.eugeniuparvan.multiplayer.core.event.server.ServerEvent;

public class OnRoomCreated extends ClientEventListener
{
    private static final long serialVersionUID = 9118384738328571332L;
    
    public OnRoomCreated()
    {
	addListener(ServerEvent.ON_ROOM_CREATED);
    }
    
    @Override
    public void onEvent(IEvent event)
    {
	// TODO Auto-generated method stub
	
    }
    
}
