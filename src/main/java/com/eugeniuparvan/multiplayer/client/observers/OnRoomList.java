package com.eugeniuparvan.multiplayer.client.observers;

import com.eugeniuparvan.multiplayer.core.event.IEvent;
import com.eugeniuparvan.multiplayer.core.event.server.ServerEvent;

public class OnRoomList extends ClientEventListener
{
    private static final long serialVersionUID = -5474809499186527840L;
    
    public OnRoomList()
    {
	addListener(ServerEvent.ON_ROOM_LIST);
    }
    
    @Override
    public void onEvent(IEvent event)
    {
	// TODO Auto-generated method stub
	
    }
    
}
