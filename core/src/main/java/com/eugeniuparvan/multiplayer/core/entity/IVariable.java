package com.eugeniuparvan.multiplayer.core.entity;

import java.io.Serializable;

public interface IVariable extends Serializable {

    String getKey();

    SerializableObject<? extends Serializable> getValue();
}
