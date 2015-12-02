package com.eugeniuparvan.multiplayer.core.entity;

import java.io.Serializable;

public class SerializableObject<T extends Serializable> extends Object implements Serializable
{
    private static final long serialVersionUID = 5054566143978572980L;
    
    private T object;
    
    public SerializableObject(T object)
    {
	this.object = object;
    }
    
    public T getObject()
    {
	return object;
    }
    
    public void setObject(T object)
    {
	this.object = object;
    }
    
}
