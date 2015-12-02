package com.eugeniuparvan.multiplayer.client.observers;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.eugeniuparvan.multiplayer.core.Observable;
import com.eugeniuparvan.multiplayer.core.Observer;
import com.eugeniuparvan.multiplayer.core.event.IEvent;

public abstract class ClientEventListener implements Observer, IEventListener
{
    private static final long serialVersionUID = -421341416156763090L;
    
    protected static final Logger logger = Logger.getLogger(ClientEventListener.class);
    
    private final Set<String> set;
    
    public ClientEventListener()
    {
	this.set = new HashSet<String>();
    }
    
    public final void addListener(String eventType)
    {
	set.add(eventType);
    }
    
    @Override
    public final void update(Observable observable, Object arg)
    {
	IEvent event = (IEvent) arg;
	
	if (!set.contains(event.getType()))
	    return;
	onEvent(event);
    }
    
    public abstract void onEvent(IEvent event);
}