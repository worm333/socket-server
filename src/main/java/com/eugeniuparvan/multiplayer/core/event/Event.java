package com.eugeniuparvan.multiplayer.core.event;

public class Event implements IEvent
{
    private static final long serialVersionUID = -2068272793727990491L;
    
    private String type;
    private IEventParams params;
    
    public Event(String type, IEventParams params)
    {
	this.type = type;
	this.params = params;
    }
    
    @Override
    public String getType()
    {
	return type;
    }
    
    @Override
    public IEventParams getParams()
    {
	return params;
    }
    
}
