package com.eugeniuparvan.multiplayer.server;

import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.eugeniuparvan.multiplayer.client.Client;
import com.eugeniuparvan.multiplayer.client.observers.OnJoinRoom;
import com.eugeniuparvan.multiplayer.client.observers.OnJoinRoomError;
import com.eugeniuparvan.multiplayer.client.observers.OnMessage;
import com.eugeniuparvan.multiplayer.client.observers.OnRoomCreated;
import com.eugeniuparvan.multiplayer.client.observers.OnRoomList;
import com.eugeniuparvan.multiplayer.client.observers.OnUserConnected;
import com.eugeniuparvan.multiplayer.client.observers.OnUserList;
import com.eugeniuparvan.multiplayer.core.entity.IRoom;
import com.eugeniuparvan.multiplayer.core.entity.IUser;
import com.eugeniuparvan.multiplayer.core.event.IEvent;
import com.eugeniuparvan.multiplayer.server.IServer;
import com.eugeniuparvan.multiplayer.server.Server;

public class ServerTestBase
{
    private volatile boolean next = false;
    private ExecutorService executor = Executors.newCachedThreadPool();
    
    private final int port = 9090;
    private final String host = "localhost";
    
    protected Set<IRoom> rooms;
    protected Set<IUser> users;
    
    protected void startServer() throws Exception
    {
	executor.submit(new ServerThread());
	Thread.sleep(1000);
    }
    
    protected Client getClient() throws Exception
    {
	Future<Client> future = executor.submit(new ClientThread());
	return future.get();
    }
    
    protected void pleaseWait()
    {
	while (!next)
	{
	}
	next = false;
    }
    
    private class ClientThread implements Callable<Client>
    {
	
	@SuppressWarnings("serial")
	@Override
	public Client call() throws Exception
	{
	    Client client = new Client();
	    client.start(host, port);
	    client.addObserver(new OnUserConnected()
	    {
		@Override
		public void onEvent(IEvent event)
		{
		    client.setUser((IUser) event.getParams().getParam("user").getObject());
		    next = true;
		}
	    });
	    client.addObserver(new OnRoomCreated()
	    {
		@Override
		public void onEvent(IEvent event)
		{
		    next = true;
		}
	    });
	    client.addObserver(new OnJoinRoom()
	    {
		@Override
		public void onEvent(IEvent event)
		{
		    next = true;
		}
	    });
	    client.addObserver(new OnJoinRoomError()
	    {
		@Override
		public void onEvent(IEvent event)
		{
		    next = true;
		}
	    });
	    client.addObserver(new OnRoomList()
	    {
		@SuppressWarnings("unchecked")
		@Override
		public void onEvent(IEvent event)
		{
		    rooms = (Set<IRoom>) event.getParams().getParam("rooms").getObject();
		    next = true;
		}
	    });
	    client.addObserver(new OnUserList()
	    {
		@SuppressWarnings("unchecked")
		@Override
		public void onEvent(IEvent event)
		{
		    users = (Set<IUser>) event.getParams().getParam("users").getObject();
		    next = true;
		}
	    });
	    client.addObserver(new OnMessage()
	    {
		@Override
		public void onEvent(IEvent event)
		{
		    String message = (String) event.getParams().getParam("message").getObject();
		    logger.info("User: " + client.getUser().getId() + " received message: '" + message + "'");
		    next = true;
		}
	    });
	    return client;
	}
	
    }
    
    private class ServerThread extends Thread
    {
	public void run()
	{
	    IServer server = new Server(port);
	    server.start();
	}
    }
}
