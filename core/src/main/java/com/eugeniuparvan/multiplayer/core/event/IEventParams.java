package com.eugeniuparvan.multiplayer.core.event;

import com.eugeniuparvan.multiplayer.core.entity.SerializableObject;

import java.io.Serializable;

public interface IEventParams extends Serializable {

    SerializableObject<? extends Serializable> getParam(String key);

    void putParam(String key, SerializableObject<? extends Serializable> value);
}
