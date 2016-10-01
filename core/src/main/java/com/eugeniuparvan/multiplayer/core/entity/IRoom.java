package com.eugeniuparvan.multiplayer.core.entity;

import java.io.Serializable;
import java.util.Set;

public interface IRoom extends Serializable {

    Long getId();

    String getName();

    Boolean isPrivate();

    Boolean isAutoRemove();

    void addUser(IUser user);

    void removeUser(IUser user);

    Set<IUser> getUsers();

    void addVariable(String key, SerializableObject<? extends Serializable> value);

    void updateVariable(String key, SerializableObject<? extends Serializable> value);

    void removeVariable(String key);

    Set<IVariable> getVariables();
}
