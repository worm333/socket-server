package com.eugeniuparvan.multiplayer.server;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Set;

import org.apache.log4j.Logger;

import com.eugeniuparvan.multiplayer.core.Observable;
import com.eugeniuparvan.multiplayer.core.Observer;
import com.eugeniuparvan.multiplayer.core.entity.IRoom;
import com.eugeniuparvan.multiplayer.core.entity.IUser;
import com.eugeniuparvan.multiplayer.core.entity.Room;
import com.eugeniuparvan.multiplayer.core.entity.SerializableObject;
import com.eugeniuparvan.multiplayer.core.entity.User;
import com.eugeniuparvan.multiplayer.core.event.EventParams;
import com.eugeniuparvan.multiplayer.core.event.IEvent;
import com.eugeniuparvan.multiplayer.core.event.IEventParams;
import com.eugeniuparvan.multiplayer.core.event.client.ClientEvent;
import com.eugeniuparvan.multiplayer.core.event.server.ServerEvent;
import com.eugeniuparvan.multiplayer.core.event.server.internal.RoomEvent;
import com.eugeniuparvan.multiplayer.core.event.server.internal.UserEvent;

public class Connection implements IConnection, Observer
{
    private static final long serialVersionUID = -1109457151804748892L;
    
    private final Logger logger = Logger.getLogger(Connection.class);
    
    private final Socket socket;
    private final IServer server;
    private final IUser user;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    
    public Connection(Socket socket, IServer server) throws IOException
    {
	this.socket = socket;
	this.server = server;
	this.user = new User(server.generateUserId(), server.getDefaultRoom());
	this.out = new ObjectOutputStream(socket.getOutputStream());
	this.in = new ObjectInputStream(socket.getInputStream());
	
	((Observable) user).addObserver(this);
    }
    
    @Override
    public void run()
    {
	boolean keepGoing = true;
	IEventParams params;
	params = new EventParams();
	params.putParam("user", new SerializableObject<IUser>(user));
	sendToClient(new UserEvent(UserEvent.USER_CONNECTED, params));
	
	while (keepGoing)
	{
	    IEvent event;
	    try
	    {
		event = (IEvent) in.readObject();
		
		switch (event.getType())
		{
		    case ClientEvent.CREATE_ROOM:
		    {
			String roomName = (String) event.getParams().getParam("name").getObject();
			String password = (String) event.getParams().getParam("password").getObject();
			IRoom room = server.createRoom(roomName, password);
			
			params = new EventParams();
			params.putParam("room", new SerializableObject<Room>((Room) room));
			sendToClient(new ServerEvent(ServerEvent.ON_ROOM_CREATED, params));
			
			break;
		    }
		    case ClientEvent.GET_ROOM_LIST:
		    {
			params = new EventParams();
			params.putParam("rooms",
				new SerializableObject<Serializable>((Serializable) server.getRooms()));
			sendToClient(new ServerEvent(ServerEvent.ON_ROOM_LIST, params));
			break;
		    }
		    case ClientEvent.GET_USER_LIST:
		    {
			params = new EventParams();
			params.putParam("users",
				new SerializableObject<Serializable>((Serializable) user.getCurrentRoom().getUsers()));
			sendToClient(new ServerEvent(ServerEvent.ON_USER_LIST, params));
			break;
		    }
		    case ClientEvent.JOIN_ROOM:
		    {
			Long roomId = (Long) event.getParams().getParam("roomId").getObject();
			String roomPassword = (String) event.getParams().getParam("password").getObject();
			IRoom room = server.joinRoom(roomId, user, roomPassword);
			
			params = new EventParams();
			params.putParam("room", new SerializableObject<IRoom>(room));
			if (room != null)
			    sendToClient(new ServerEvent(ServerEvent.ON_JOIN_ROOM, params));
			else
			    sendToClient(new ServerEvent(ServerEvent.ON_JOIN_ROOM_ERROR, params));
			break;
		    }
		    case ClientEvent.SEND_PRIVATE_MESSAGE:
		    {
			Long userId = (Long) event.getParams().getParam("userId").getObject();
			String message = (String) event.getParams().getParam("message").getObject();
			Boolean isPrivate = (Boolean) event.getParams().getParam("isPrivate").getObject();
			
			params = new EventParams();
			params.putParam("message", new SerializableObject<String>(message));
			params.putParam("isPrivate", new SerializableObject<Boolean>(isPrivate));
			
			IConnection connection = server.getConnection(userId);
			connection.sendToClient(new ServerEvent(ServerEvent.ON_MESSAGE, params));
			
			break;
		    }
		    case ClientEvent.SEND_PUBLIC_MESSAGE:
		    {
			String message = (String) event.getParams().getParam("message").getObject();
			Boolean isPrivate = (Boolean) event.getParams().getParam("isPrivate").getObject();
			
			params = new EventParams();
			params.putParam("message", new SerializableObject<String>(message));
			params.putParam("isPrivate", new SerializableObject<Boolean>(isPrivate));
			
			Set<IUser> users = user.getCurrentRoom().getUsers();
			for (IUser u : users)
			{
			    if (u.equals(user))
				continue;
			    IConnection connection = server.getConnection(u.getId());
			    connection.sendToClient(new ServerEvent(ServerEvent.ON_MESSAGE, params));
			}
			break;
		    }
		}
	    }
	    catch (IOException e)
	    {
		logger.fatal("TODO: nice message", e);// TODO: nice message
		break;
	    }
	    catch (ClassNotFoundException e)
	    {
		logger.error("TODO: nice message", e);// TODO: nice message
	    }
	}
	releaseResources();
    }
    
    @Override
    public void update(Observable observable, Object argument)
    {
	IEvent event = (IEvent) argument;
	IUser u = null;
	IRoom r = null;
	switch (event.getType())
	{
	    case RoomEvent.USER_ENTERED_ROOM:
		u = (IUser) event.getParams().getParam("user").getObject();
		r = (IRoom) event.getParams().getParam("room").getObject();
		logger.info("User: " + u.getId() + " entered in the room: " + r.getId());
		break;
	    case RoomEvent.USER_EXITED_ROOM:
		u = (IUser) event.getParams().getParam("user").getObject();
		r = (IRoom) event.getParams().getParam("room").getObject();
		logger.info("User: " + u.getId() + " has been exited from the room: " + r.getId());
		break;
	    case RoomEvent.ROOM_VAIABLE_UPDATE:
		r = (IRoom) event.getParams().getParam("room").getObject();
		logger.info("Room: " + r.getId() + " variable has been updated.");
		break;
	    case UserEvent.USER_VARIABLE_UPDATE:
		u = (IUser) event.getParams().getParam("user").getObject();
		logger.info("User: " + u.getId() + " variable has been updated.");
		break;
	}
    }
    
    @Override
    public Socket getSocket()
    {
	return socket;
    }
    
    @Override
    public ObjectInputStream getInputStream()
    {
	return in;
    }
    
    @Override
    public ObjectOutputStream getOutputStream()
    {
	return out;
    }
    
    @Override
    public IUser getUser()
    {
	return user;
    }
    
    @Override
    public IServer gerServer()
    {
	return server;
    }
    
    private void releaseResources()
    {
	// TODO: Test this!
	((Observable) user).deleteObservers();
	((Observable) user.getCurrentRoom()).deleteObserver((Observer) user);
	
	server.removeConnection(this);
	
	close(out);
	close(in);
	close(socket);
    }
    
    private void close(Closeable closeable)
    {
	try
	{
	    closeable.close();
	}
	catch (IOException e)
	{
	    logger.error("TODO: nice message", e);// TODO: nice message
	}
    }
    
    @Override
    public void sendToClient(IEvent event)
    {
	try
	{
	    out.writeObject(event);
	}
	catch (IOException e)
	{
	    logger.error("Sending event to client error", e);
	}
	
    }
}
