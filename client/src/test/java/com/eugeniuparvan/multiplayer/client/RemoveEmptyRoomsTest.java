package com.eugeniuparvan.multiplayer.client;

import com.eugeniuparvan.multiplayer.core.entity.IRoom;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by eugeniuparvan on 2/7/17.
 */
public class RemoveEmptyRoomsTest extends TestBase {

    @Test
    public void removeEmptyRoomsTest() throws Exception {
        //Create first user
        Client client_0 = getClient();
        pleaseWait();//waiting onConnected notification
        pleaseWait();//waiting onJoin notification to default room

        client_0.createRoom("HelloWorld", "123");
        pleaseWait();// waiting onRoomCreated

        //Create second user
        Client client_1 = getClient();
        pleaseWait();//waiting onConnected notification
        pleaseWait();//waiting onJoin notification to default room
        pleaseWait();//waiting onUserEnterRoom notification for client_0

        //Create third user
        Client client_2 = getClient();
        pleaseWait();//waiting onConnected notification
        pleaseWait();//waiting onJoin notification to default room
        pleaseWait();//waiting onUserEnterRoom notification for client_0
        pleaseWait();//waiting onUserEnterRoom notification for client_1

        client_0.getRooms();
        pleaseWait();//waiting onRoomList notification
        IRoom helloWorldRoom = rooms.stream().filter(x -> "HelloWorld".equals(x.getName())).findFirst().orElseThrow(() -> new Exception(""));

        client_0.joinRoom(helloWorldRoom.getId(), "123");
        pleaseWait();//waiting onJoin notification

        client_1.joinRoom(helloWorldRoom.getId(), "123");
        pleaseWait();//waiting onJoin notification
        pleaseWait();//waiting onUserEnterRoom notification for client_0

        client_2.joinRoom(helloWorldRoom.getId(), "123");
        pleaseWait();//waiting onJoin notification
        pleaseWait();//waiting onUserEnterRoom notification for client_0
        pleaseWait();//waiting onUserEnterRoom notification for client_1

        client_0.getRooms();
        pleaseWait();//waiting onRoomList notification
        helloWorldRoom = rooms.stream().filter(x -> "HelloWorld".equals(x.getName())).findFirst().orElseThrow(() -> new Exception(""));
        Assert.assertEquals(3, helloWorldRoom.getUsers().size());

        client_0.exitRoom(helloWorldRoom.getId());
        pleaseWait();//waiting onRoomExit for client_0
        pleaseWait();//waiting onUserExitedRoom for client_1
        pleaseWait();//waiting onUserExitedRoom for client_2

        client_0.getRooms();
        pleaseWait();//waiting onRoomList notification
        helloWorldRoom = rooms.stream().filter(x -> "HelloWorld".equals(x.getName())).findFirst().orElseThrow(() -> new Exception(""));
        Assert.assertEquals(2,helloWorldRoom.getUsers().size());
        Assert.assertEquals(2,rooms.size());//default room and HelloWorld still exists

        client_2.exitRoom(helloWorldRoom.getId());
        pleaseWait();//waiting onRoomExit for client_2
        pleaseWait();//waiting onUserExitedRoom for client_1

        client_0.getRooms();
        pleaseWait();//waiting onRoomList notification
        helloWorldRoom = rooms.stream().filter(x -> "HelloWorld".equals(x.getName())).findFirst().orElseThrow(() -> new Exception(""));
        Assert.assertEquals(1,helloWorldRoom.getUsers().size());
        Assert.assertEquals(2,rooms.size());//default room and HelloWorld still exists

        client_1.exitRoom(helloWorldRoom.getId());
        pleaseWait();//waiting onRoomExit for client_1
        client_0.getRooms();
        pleaseWait();//waiting onRoomList notification
        Assert.assertEquals(1,rooms.size());//default room and HelloWorld still exists
    }
}
