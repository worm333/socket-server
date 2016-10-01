package com.eugeniuparvan.multiplayer.core.event;

import java.io.Serializable;

public interface IEvent extends Serializable {

    String getType();

    IEventParams getParams();
}
