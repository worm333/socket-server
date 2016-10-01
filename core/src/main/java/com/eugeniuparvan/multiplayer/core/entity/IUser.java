package com.eugeniuparvan.multiplayer.core.entity;

import java.io.Serializable;
import java.util.Set;

public interface IUser extends Serializable {

    Long getId();

    String getName();

    void setName(String name);

    IRoom getRoom(Long roomId);

    Set<IRoom> getJoinedRooms();

    void joinRoom(IRoom room);

    void exitRoom(IRoom room);

    void disconnect();

    void addVariable(String key, SerializableObject<? extends Serializable> value);

    void updateVariable(String key, SerializableObject<? extends Serializable> value);

    void removeVariable(String key);

    Set<IVariable> getVariables();

}
