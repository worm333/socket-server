package com.eugeniuparvan.multiplayer.client.observers;

import com.eugeniuparvan.multiplayer.core.event.IEvent;
import com.eugeniuparvan.multiplayer.core.event.server.ServerEvent;

public class OnUserList extends ClientEventListener
{
    private static final long serialVersionUID = 777164204802586897L;
    
    public OnUserList()
    {
	addListener(ServerEvent.ON_USER_LIST);
    }
    
    @Override
    public void onEvent(IEvent event)
    {
	// TODO Auto-generated method stub
	
    }
    
}
