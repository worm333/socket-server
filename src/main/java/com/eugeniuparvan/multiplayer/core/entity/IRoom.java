package com.eugeniuparvan.multiplayer.core.entity;

import java.io.Serializable;
import java.util.Set;

public interface IRoom extends Serializable
{
    public Long getId();
    
    public String getName();
    
    public Boolean isPrivate();
    
    public Boolean isAutoRemove();
    
    public void addUser(IUser user);
    
    public void removeUser(IUser user);
    
    public Set<IUser> getUsers();
    
    public void addVariable(String key, SerializableObject<? extends Serializable> value);
    
    public void updateVariable(String key, SerializableObject<? extends Serializable> value);
    
    public void removeVariable(String key);
    
    public Set<IVariable> getVariables();
}
