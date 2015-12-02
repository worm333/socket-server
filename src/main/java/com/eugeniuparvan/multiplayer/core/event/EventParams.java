package com.eugeniuparvan.multiplayer.core.event;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.eugeniuparvan.multiplayer.core.entity.SerializableObject;

public class EventParams implements IEventParams
{
    private static final long serialVersionUID = 3076601674081054716L;
    
    private final Map<String, SerializableObject<? extends Serializable>> map;
    
    public EventParams()
    {
	this.map = new HashMap<String, SerializableObject<? extends Serializable>>();
    }
    
    @Override
    public SerializableObject<? extends Serializable> getParam(String key)
    {
	if (map.containsKey(key))
	    return map.get(key);
	else
	    return new SerializableObject<Serializable>(null);
    }
    
    @Override
    public void putParam(String key, SerializableObject<? extends Serializable> value)
    {
	map.put(key, value);
    }
}
