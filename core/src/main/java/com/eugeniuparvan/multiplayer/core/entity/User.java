package com.eugeniuparvan.multiplayer.core.entity;

import com.eugeniuparvan.multiplayer.core.Observable;
import com.eugeniuparvan.multiplayer.core.Observer;
import com.eugeniuparvan.multiplayer.core.event.EventParams;
import com.eugeniuparvan.multiplayer.core.event.server.internal.UserNotificationEvent;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class User extends Observable implements IUser, Observer {

    private static final long serialVersionUID = 1L;

    private final Long id;

    private volatile String name;

    private ConcurrentMap<Long, IRoom> idRoomMap;

    private ConcurrentMap<String, SerializableObject<? extends Serializable>> variables;

    public User(long id) {
        this.id = id;
        this.idRoomMap = new ConcurrentHashMap<>();
        this.variables = new ConcurrentHashMap<>();

        setName(new Long(id).toString());
    }

    @Override
    public void update(Observable observable, Object object) {
        setChanged();
        notifyObservers(object);
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public synchronized void setName(String name) {
        this.name = name;
    }

    @Override
    public IRoom getRoom(Long roomId) {
        return idRoomMap.get(roomId);
    }

    @Override
    public Set<IRoom> getJoinedRooms() {
        return idRoomMap.values().stream().collect(Collectors.toSet());
    }

    @Override
    public void joinRoom(IRoom room) {
        idRoomMap.putIfAbsent(room.getId(), room);
        ((Observable) room).addObserver(this);
        room.addUser(this);
    }

    @Override
    public void exitRoom(IRoom room) {
        idRoomMap.remove(room.getId());
        room.removeUser(this);
        ((Observable) room).deleteObserver(this);
    }

    @Override
    public void disconnect() {
        idRoomMap.values().stream().forEach(room -> {
            room.removeUser(this);
            ((Observable) room).deleteObserver(this);
        });
        idRoomMap.clear();
        variables.clear();
        deleteObservers();
    }

    @Override
    public void addVariable(String key, SerializableObject<? extends Serializable> value) {
        Object object = variables.putIfAbsent(key, value);
        if (object != null)
            return;
        setChanged();
        notifyObservers(new UserNotificationEvent(UserNotificationEvent.USER_VARIABLE_UPDATE, new EventParams()));
    }

    @Override
    public void updateVariable(String key, SerializableObject<? extends Serializable> value) {
        variables.replace(key, value);
        setChanged();
        notifyObservers(new UserNotificationEvent(UserNotificationEvent.USER_VARIABLE_UPDATE, new EventParams()));
    }

    @Override
    public void removeVariable(String key) {
        variables.remove(key);
        setChanged();
        notifyObservers(new UserNotificationEvent(UserNotificationEvent.USER_VARIABLE_UPDATE, new EventParams()));
    }

    @Override
    public Set<IVariable> getVariables() {
        Set<IVariable> vars = variables.keySet().stream().map(key -> new Variable(key, variables.get(key)))
                .collect(Collectors.toSet());
        return vars;
    }
}
