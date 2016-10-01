package com.eugeniuparvan.multiplayer.core.entity;

import java.io.Serializable;

public class Variable implements IVariable {

    private static final long serialVersionUID = 1L;

    private String key;

    private SerializableObject<? extends Serializable> value;

    public Variable(String key, SerializableObject<? extends Serializable> value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public SerializableObject<? extends Serializable> getValue() {
        return value;
    }
}
