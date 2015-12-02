package com.eugeniuparvan.multiplayer.core.entity;

import java.io.Serializable;
import java.util.Set;

public interface IUser extends Serializable
{
    public Long getId();
    
    public String getName();
    
    public void setName(String name);
    
    public IRoom getCurrentRoom();
    
    public void setCurrentRoom(IRoom room);
    
    public void addVariable(String key, SerializableObject<? extends Serializable> value);
    
    public void updateVariable(String key, SerializableObject<? extends Serializable> value);
    
    public void removeVariable(String key);
    
    public Set<IVariable> getVariables();
    
}
