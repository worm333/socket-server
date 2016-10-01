package com.eugeniuparvan.multiplayer.core;

import java.io.Serializable;

/**
 * @see java.util.Observer
 */
public interface Observer extends Serializable {

    void update(Observable o, Object arg);

}