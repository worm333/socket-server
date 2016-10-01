package com.eugeniuparvan.multiplayer.client;

import com.eugeniuparvan.multiplayer.client.observers.OnJoinRoom;
import com.eugeniuparvan.multiplayer.client.observers.OnRoomCreated;
import com.eugeniuparvan.multiplayer.core.entity.IRoom;
import com.eugeniuparvan.multiplayer.core.event.IEvent;
import org.junit.Assert;
import org.junit.Test;

public class ClientEventTest extends TestBase {

    private IRoom defaultRoom;
    private IRoom helloWorldRoom;

    @Test
    public void clientEventTest() throws Exception {
        startServer();

        Client client_0 = getClient();
        client_0.addObserver(new OnJoinRoom() {
            @Override
            public void onEvent(IEvent event) {
                defaultRoom = (IRoom) event.getParams().getParam("room").getObject();
                client_0.deleteObserver(this);
            }
        });
        client_0.addObserver(new OnRoomCreated() {
            @Override
            public void onEvent(IEvent event) {
                IRoom room = (IRoom) event.getParams().getParam("room").getObject();
                switch (room.getName()) {
                    case "HelloWorld":
                        helloWorldRoom = room;
                        client_0.deleteObserver(this);
                        break;
                }
            }
        });

        pleaseWait();//waiting onConnected event
        pleaseWait();//waiting onJoin event to default room
        client_0.createRoom("HelloWorld", "123");
        pleaseWait();//waiting onRoomCreated event
        client_0.getRooms();
        pleaseWait();//waiting onRoomList event
        Assert.assertEquals(rooms.size(), 2);

        //Create second user
        Client client_1 = getClient();
        pleaseWait();//waiting onConnected event
        pleaseWait();//waiting onJoin event to default room
        pleaseWait();//waiting onUserEnterRoom event for client_0

        client_1.getUsers(defaultRoom.getId());
        pleaseWait();
        Assert.assertEquals(users.size(), 2);

        //Create third user
        Client client_2 = getClient();
        pleaseWait();//waiting onConnected event
        pleaseWait();//waiting onJoin event to default room
        pleaseWait();//waiting onUserEnterRoom event for client_0
        pleaseWait();//waiting onUserEnterRoom event for client_1

        client_0.getUsers(defaultRoom.getId());
        pleaseWait();//waiting onUserList event
        Assert.assertEquals(users.size(), 3);

        client_1.getUsers(defaultRoom.getId());
        pleaseWait();//waiting onUserList event
        Assert.assertEquals(users.size(), 3);

        client_2.getUsers(defaultRoom.getId());
        pleaseWait();//waiting onUserList event
        Assert.assertEquals(users.size(), 3);

        client_0.joinRoom(helloWorldRoom.getId(), "123");
        pleaseWait();//waiting onJoin event
        client_0.getJoinedRooms();
        pleaseWait();
        Assert.assertEquals(joinedRooms.size(), 2);

        //Check user count in helloWorldRoom and Default room
        client_0.getUsers(helloWorldRoom.getId());
        pleaseWait();//waiting onUserList event in helloWorldRoom
        Assert.assertEquals(users.size(), 1);

        client_1.getUsers(helloWorldRoom.getId());
        pleaseWait();//waiting onUserList event in helloWorldRoom
        Assert.assertEquals(users.size(), 0);

        client_2.getUsers(helloWorldRoom.getId());
        pleaseWait();//waiting onUserList event in helloWorldRoom
        Assert.assertEquals(users.size(), 0);

        client_0.getUsers(defaultRoom.getId());
        pleaseWait();//waiting onUserList event in defaultRoom
        Assert.assertEquals(users.size(), 3);

        client_1.getUsers(defaultRoom.getId());
        pleaseWait();//waiting onUserList event in defaultRoom
        Assert.assertEquals(users.size(), 3);

        client_2.getUsers(defaultRoom.getId());
        pleaseWait();//waiting onUserList event in defaultRoom
        Assert.assertEquals(users.size(), 3);


        client_1.joinRoom(helloWorldRoom.getId(), "123");
        pleaseWait();//waiting onJoin event to helloWorld room
        pleaseWait();//waiting onUserEnterRoom event for client_0

        client_0.getUsers(helloWorldRoom.getId());
        pleaseWait();
        Assert.assertEquals(users.size(), 2);

        client_1.getUsers(helloWorldRoom.getId());
        pleaseWait();
        Assert.assertEquals(users.size(), 2);

        client_2.getUsers(helloWorldRoom.getId());
        pleaseWait();
        Assert.assertEquals(users.size(), 0);

        client_2.joinRoom(helloWorldRoom.getId(), "123");
        pleaseWait();//waiting onJoin event to helloWorld room
        pleaseWait();//waiting onUserEnterRoom event for client_0
        pleaseWait();//waiting onUserEnterRoom event for client_1

        client_0.getUsers(helloWorldRoom.getId());
        pleaseWait();
        Assert.assertEquals(users.size(), 3);

        client_1.getUsers(helloWorldRoom.getId());
        pleaseWait();
        Assert.assertEquals(users.size(), 3);

        client_2.getUsers(helloWorldRoom.getId());
        pleaseWait();
        Assert.assertEquals(users.size(), 3);

        client_0.exitRoom(helloWorldRoom.getId());
        pleaseWait();//waiting onUserExitRoom event for client_1
        pleaseWait();//waiting onUserExitRoom event for client_2

        client_0.getJoinedRooms();
        pleaseWait();
        Assert.assertEquals(joinedRooms.size(), 1);

        client_0.exitRoom(defaultRoom.getId());//does not send any event(can't exit from default room)
        client_0.getJoinedRooms();
        pleaseWait();
        Assert.assertEquals(joinedRooms.size(), 1);

        client_2.sendPublicMessage("Hi all!_2");
        pleaseWait();
        client_1.sendPublicMessage("Hi all!_1");
        pleaseWait();
        client_0.sendPublicMessage("Hi all!_0");
        pleaseWait();

        client_0.sendPrivateMessage(2L, "Private message");
    }

}