package com.eugeniuparvan.multiplayer.server;

import org.junit.Assert;
import org.junit.Test;

import com.eugeniuparvan.multiplayer.client.Client;

public class ClientEventTest extends ServerTestBase
{
    @Test
    public void clientEventTest() throws Exception
    {
	startServer();
	
	Client client_0 = getClient();
	pleaseWait();
	client_0.createRoom("HelloWorld", "123");
	pleaseWait();
	client_0.getRooms();
	pleaseWait();
	Assert.assertEquals(rooms.size(), 2);
	
	Client client_1 = getClient();
	pleaseWait();
	client_1.getUsers();
	pleaseWait();
	Assert.assertEquals(users.size(), 2);
	
	Client client_2 = getClient();
	pleaseWait();
	client_0.getUsers();
	pleaseWait();
	Assert.assertEquals(users.size(), 3);
	
	client_1.getUsers();
	pleaseWait();
	Assert.assertEquals(users.size(), 3);
	
	client_2.getUsers();
	pleaseWait();
	Assert.assertEquals(users.size(), 3);
	
	client_0.joinRoom(1L, "123");
	pleaseWait();
	
	client_0.getUsers();
	pleaseWait();
	Assert.assertEquals(users.size(), 1);
	
	client_1.getUsers();
	pleaseWait();
	Assert.assertEquals(users.size(), 2);
	
	client_2.getUsers();
	pleaseWait();
	Assert.assertEquals(users.size(), 2);
	
	client_1.joinRoom(1L, "123");
	pleaseWait();
	
	client_0.getUsers();
	pleaseWait();
	Assert.assertEquals(users.size(), 2);
	
	client_1.getUsers();
	pleaseWait();
	Assert.assertEquals(users.size(), 2);
	
	client_2.getUsers();
	pleaseWait();
	Assert.assertEquals(users.size(), 1);
	
	client_2.joinRoom(1L, "123");
	pleaseWait();
	
	client_0.getUsers();
	pleaseWait();
	Assert.assertEquals(users.size(), 3);
	
	client_1.getUsers();
	pleaseWait();
	Assert.assertEquals(users.size(), 3);
	
	client_2.getUsers();
	pleaseWait();
	Assert.assertEquals(users.size(), 3);
	
	client_2.sendPublicMessage("Hi all!_2");
	pleaseWait();
	client_1.sendPublicMessage("Hi all!_1");
	pleaseWait();
	client_0.sendPublicMessage("Hi all!_0");
	pleaseWait();
	
	client_0.sendPrivateMessage(2L, "Private message");
    }
    
}
