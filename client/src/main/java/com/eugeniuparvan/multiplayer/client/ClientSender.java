package com.eugeniuparvan.multiplayer.client;

import com.eugeniuparvan.multiplayer.core.event.IEvent;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class ClientSender implements Runnable {

    private final static Logger logger = Logger.getLogger(ClientSender.class);

    private final IEvent event;

    private final ObjectOutputStream out;

    public ClientSender(IEvent event, ObjectOutputStream out) {
        this.event = event;
        this.out = out;
    }

    @Override
    public void run() {
        try {
            out.writeObject(event);
        } catch (IOException e) {
            logger.error("Sending event error", e);
        }
    }

}
