package com.eugeniuparvan.multiplayer.core.entity;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import com.eugeniuparvan.multiplayer.core.Observable;
import com.eugeniuparvan.multiplayer.core.Observer;
import com.eugeniuparvan.multiplayer.core.event.EventParams;
import com.eugeniuparvan.multiplayer.core.event.IEventParams;
import com.eugeniuparvan.multiplayer.core.event.server.internal.UserEvent;

public class User extends Observable implements IUser, Observer
{
    private static final long serialVersionUID = 1814955197547434660L;
    
    private final Long id;
    private volatile String name;
    private volatile IRoom currentRoom;
    private ConcurrentMap<String, SerializableObject<? extends Serializable>> variables;
    
    public User(long id, IRoom room)
    {
	this.id = id;
	this.currentRoom = room;
	this.variables = new ConcurrentHashMap<String, SerializableObject<? extends Serializable>>();
	
	currentRoom.addUser(this);
	setName(new Long(id).toString());
	
	((Observable) currentRoom).addObserver(this);
    }
    
    @Override
    public void update(Observable observable, Object object)
    {
	setChanged();
	notifyObservers(object);
    }
    
    @Override
    public Long getId()
    {
	return id;
    }
    
    @Override
    public String getName()
    {
	return name;
    }
    
    @Override
    public synchronized void setName(String name)
    {
	this.name = name;
    }
    
    @Override
    public IRoom getCurrentRoom()
    {
	return currentRoom;
    }
    
    @Override
    public synchronized void setCurrentRoom(IRoom room)
    {
	// TODO: Test this!
	currentRoom.removeUser(this);
	((Observable) currentRoom).deleteObserver(this);
	
	this.currentRoom = room;
	((Observable) currentRoom).addObserver(this);
	
	this.currentRoom.addUser(this);
    }
    
    @Override
    public void addVariable(String key, SerializableObject<? extends Serializable> value)
    {
	Object object = variables.putIfAbsent(key, value);
	if (object != null)
	    return;
	setChanged();
	IEventParams params = new EventParams();
	params.putParam("user", new SerializableObject<IUser>(this));
	notifyObservers(new UserEvent(UserEvent.USER_VARIABLE_UPDATE, params));
    }
    
    @Override
    public void updateVariable(String key, SerializableObject<? extends Serializable> value)
    {
	variables.replace(key, value);
	setChanged();
	IEventParams params = new EventParams();
	params.putParam("user", new SerializableObject<IUser>(this));
	notifyObservers(new UserEvent(UserEvent.USER_VARIABLE_UPDATE, params));
    }
    
    @Override
    public void removeVariable(String key)
    {
	variables.remove(key);
	setChanged();
	IEventParams params = new EventParams();
	params.putParam("user", new SerializableObject<IUser>(this));
	notifyObservers(new UserEvent(UserEvent.USER_VARIABLE_UPDATE, params));
    }
    
    @Override
    public Set<IVariable> getVariables()
    {
	Set<IVariable> vars = variables.keySet().stream().map(key -> new Variable(key, variables.get(key)))
		.collect(Collectors.toSet());
	return vars;
    }
}
