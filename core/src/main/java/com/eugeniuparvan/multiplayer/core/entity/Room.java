package com.eugeniuparvan.multiplayer.core.entity;

import com.eugeniuparvan.multiplayer.core.Observable;
import com.eugeniuparvan.multiplayer.core.event.EventParams;
import com.eugeniuparvan.multiplayer.core.event.IEventParams;
import com.eugeniuparvan.multiplayer.core.event.server.internal.RoomNotificationEvent;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class Room extends Observable implements IRoom {

    public static final String DEFAULT_NAME = "Default";

    private static final long serialVersionUID = 1L;

    private final Long id;

    private final String name;

    private final Boolean privateRoom;

    private final Boolean autoRemove;

    private ConcurrentMap<Long, IUser> users;

    private ConcurrentMap<String, SerializableObject<? extends Serializable>> variables;

    public Room(long id, String name, boolean privateRoom, boolean autoRemove) {
        this.id = id;
        this.name = name;
        this.privateRoom = privateRoom;
        this.autoRemove = autoRemove;
        this.users = new ConcurrentHashMap<>();
        this.variables = new ConcurrentHashMap<>();
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
    public Boolean isPrivate() {
        return privateRoom;
    }

    @Override
    public Boolean isAutoRemove() {
        return autoRemove;
    }

    @Override
    public void addUser(IUser user) {
        Object object = users.putIfAbsent(user.getId(), user);
        if (object != null)
            return;
        setChanged();
        IEventParams params = new EventParams();
        params.putParam("room", new SerializableObject<IRoom>(this));
        params.putParam("user", new SerializableObject<IUser>(user));
        notifyObservers(new RoomNotificationEvent(RoomNotificationEvent.USER_ENTERED_ROOM, params));
    }

    @Override
    public void removeUser(IUser user) {
        users.remove(user.getId());
        setChanged();
        IEventParams params = new EventParams();
        params.putParam("room", new SerializableObject<IRoom>(this));
        params.putParam("user", new SerializableObject<IUser>(user));
        notifyObservers(new RoomNotificationEvent(RoomNotificationEvent.USER_EXITED_ROOM, params));
    }

    @Override
    public Set<IUser> getUsers() {
        return users.values().stream().collect(Collectors.toSet());
    }

    @Override
    public void addVariable(String key, SerializableObject<? extends Serializable> value) {
        Object object = variables.putIfAbsent(key, value);
        if (object != null)
            return;
        setChanged();
        IEventParams params = new EventParams();
        params.putParam("room", new SerializableObject<IRoom>(this));
        notifyObservers(new RoomNotificationEvent(RoomNotificationEvent.ROOM_VARIABLE_UPDATE, params));
    }

    @Override
    public void updateVariable(String key, SerializableObject<? extends Serializable> value) {
        variables.replace(key, value);
        setChanged();
        IEventParams params = new EventParams();
        params.putParam("room", new SerializableObject<IRoom>(this));
        notifyObservers(new RoomNotificationEvent(RoomNotificationEvent.ROOM_VARIABLE_UPDATE, params));
    }

    @Override
    public void removeVariable(String key) {
        variables.remove(key);
        setChanged();
        IEventParams params = new EventParams();
        params.putParam("room", new SerializableObject<IRoom>(this));
        notifyObservers(new RoomNotificationEvent(RoomNotificationEvent.ROOM_VARIABLE_UPDATE, params));
    }

    @Override
    public Set<IVariable> getVariables() {
        Set<IVariable> vars = variables.keySet().stream().map(key -> new Variable(key, variables.get(key)))
                .collect(Collectors.toSet());
        return vars;
    }
}
