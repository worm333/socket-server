package com.eugeniuparvan.multiplayer.core.event;

import java.io.Serializable;

import com.eugeniuparvan.multiplayer.core.entity.SerializableObject;

public interface IEventParams extends Serializable
{
    public SerializableObject<? extends Serializable> getParam(String key);
    
    public void putParam(String key, SerializableObject<? extends Serializable> value);
}
