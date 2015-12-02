package com.eugeniuparvan.multiplayer.core.entity;

import java.io.Serializable;

public interface IVariable extends Serializable
{
    public String getKey();
    
    public SerializableObject<? extends Serializable> getValue();
}
