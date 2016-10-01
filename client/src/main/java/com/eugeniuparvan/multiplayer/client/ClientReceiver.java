package com.eugeniuparvan.multiplayer.client;

import com.eugeniuparvan.multiplayer.core.Observable;
import com.eugeniuparvan.multiplayer.core.event.IEvent;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;

public class ClientReceiver extends Observable implements Runnable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(ClientReceiver.class);

    private final ObjectInputStream in;

    public ClientReceiver(ObjectInputStream in) {
        this.in = in;
    }

    @Override
    public void run() {
        while (true) {
            try {
                IEvent event = (IEvent) in.readObject();
                setChanged();
                notifyObservers(event);
            } catch (IOException e) {
                logger.fatal("Receiving event fatal error", e);
                break;
            } catch (ClassNotFoundException e) {
                logger.error("Receiving event error", e);
            }
        }
    }
}
