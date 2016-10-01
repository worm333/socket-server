package com.eugeniuparvan.socketserver.example.client;

import com.eugeniuparvan.multiplayer.client.Client;
import com.eugeniuparvan.multiplayer.client.observers.*;
import com.eugeniuparvan.multiplayer.core.Observer;
import com.eugeniuparvan.multiplayer.core.entity.IRoom;
import com.eugeniuparvan.multiplayer.core.entity.IUser;
import com.eugeniuparvan.multiplayer.core.event.IEvent;

import java.util.Scanner;
import java.util.Set;

/**
 * Created by eugeniuparvan on 10/1/16.
 */
public class Main {

    private static int port;

    private static String host;

    private static Client client;

    private static Scanner input;

    public static void main(String[] args) {
        input = new Scanner(System.in);

        //... Read port number from the console.
        System.out.print("Enter server host: ");
        host = input.nextLine();

        System.out.print("Enter server port: ");
        port = input.nextInt();

        client = new Client();
        client.start(host, port);
        addObservers();
        System.out.println("The client has been started. Type: help, to see available commands");

        while (true) {
            try {
                String command = input.nextLine();
                switch (command) {
                    case "help": {
                        help();
                        break;
                    }
                    case "getRooms": {
                        getRooms();
                        break;
                    }
                    case "getUsers": {
                        getUsers();
                        break;
                    }
                    case "sendPrivateMessage": {
                        sendPrivateMessage();
                        break;
                    }
                    case "sendPublicMessage": {
                        sendPublicMessage();
                        break;
                    }
                    case "getJoinedRooms": {
                        getJoinedRooms();
                        break;
                    }
                    case "createRoom": {
                        createRoom();
                        break;
                    }
                    case "joinRoom": {
                        joinRoom();
                        break;
                    }
                    case "exitRoom": {
                        exitRoom();
                        break;
                    }
                    case "stop": {
                        stop();
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void help() {
        System.out.println("- getRooms");
        System.out.println("- getUsers");
        System.out.println("- sendPrivateMessage");
        System.out.println("- sendPublicMessage");
        System.out.println("- getJoinedRooms");
        System.out.println("- createRoom");
        System.out.println("- joinRoom");
        System.out.println("- exitRoom");
        System.out.println("- stop");
    }

    private static void getRooms() {
        client.addObserver(new OnRoomList() {
            @Override
            public void onEvent(IEvent event) {
                client.deleteObserver(this);

                Set<IRoom> rooms = (Set<IRoom>) event.getParams().getParam("rooms").getObject();
                System.out.println("Available rooms:");
                for (IRoom room : rooms) {
                    System.out.println("\tRoomName: " + room.getName() + "; RoomId: " + room.getId() + ";");
                }
            }
        });
        client.getRooms();
    }

    private static void getUsers() {
        System.out.println("What is the room id you want to select users from?");
        long roomId = input.nextLong();
        input.nextLine();

        client.addObserver(new OnUserList() {
            @Override
            public void onEvent(IEvent event) {
                client.deleteObserver(this);

                Set<IUser> users = (Set<IUser>) event.getParams().getParam("users").getObject();
                System.out.println("All users in the room with id: " + roomId);
                for (IUser user : users) {
                    System.out.println("\tUserName: " + user.getName() + "; UserId: " + user.getId() + ";");
                }
            }
        });
        client.getUsers(roomId);
    }

    private static void sendPrivateMessage() {
        System.out.println("Type userId you want to send private message to");
        Long userId = input.nextLong();
        input.nextLine();

        System.out.println("Type the message:");
        String message = input.nextLine();

        client.sendPrivateMessage(userId, message);
    }

    private static void sendPublicMessage() {
        System.out.println("Type the message:");
        String message = input.nextLine();

        client.sendPublicMessage(message);
    }

    private static void getJoinedRooms() {
        client.addObserver(new OnUserJoinedRooms() {
            @Override
            public void onEvent(IEvent event) {
                client.deleteObserver(this);

                Set<IRoom> rooms = (Set<IRoom>) event.getParams().getParam("rooms").getObject();
                System.out.println("Here is the rooms you've been joined in:");
                for (IRoom room : rooms) {
                    System.out.println("\tRoomName: " + room.getName() + "; RoomId: " + room.getId() + ";");
                }
            }
        });
        client.getJoinedRooms();
    }

    private static void createRoom() {
        System.out.println("What is the room name?");
        String roomName = input.nextLine();

        System.out.println("Write the room password, if you want to make a private room or null, if not");
        String roomPassword = input.nextLine();

        client.addObserver(new OnRoomCreated() {
            @Override
            public void onEvent(IEvent event) {
                client.deleteObserver(this);

                IRoom room = (IRoom) event.getParams().getParam("room").getObject();
                System.out.println("You created new room: ");
                System.out.println("\tRoomName: " + room.getName() + "; RoomId: " + room.getId() + ";");
            }
        });

        //TODO: also need to handle here RoomCreationError event

        client.createRoom(roomName, roomPassword);
    }

    private static void joinRoom() {
        System.out.println("What room do you like to join? Type the room id:");
        long roomId = input.nextLong();
        input.nextLine();

        System.out.println("Does the room has password? Type the room password or null:");
        String roomPassword = input.nextLine();

        OnJoinRoomImpl onJoinRoom = new OnJoinRoomImpl();
        Observer onJoinRoomError = new OnJoinRoomError() {
            @Override
            public void onEvent(IEvent event) {
                client.deleteObserver(this);
                client.deleteObserver(onJoinRoom);
                IRoom room = (IRoom) event.getParams().getParam("room").getObject();
                System.out.println("Can't join the room with id: " + roomId);
            }
        };
        onJoinRoom.setOnJoinRoomError(onJoinRoomError);

        client.addObserver(onJoinRoom);
        client.addObserver(onJoinRoomError);

        client.joinRoom(roomId, roomPassword);
    }

    private static void exitRoom() {
        System.out.println("What is the roomId yon want to exit?");
        Long roomId = input.nextLong();
        input.nextLine();

        client.exitRoom(roomId);
        System.out.println("You've been exit from room with id: " + roomId);
    }

    private static void stop() {
        client.stop();
    }

    private static void addObservers() {
        client.addObserver(new OnPublicMessage() {
            @Override
            public void onEvent(IEvent event) {
                String message = (String) event.getParams().getParam("message").getObject();
                IUser fromUser = (IUser) event.getParams().getParam("user").getObject();
                System.out.println("Public message.");
                System.out.println("\t from " + fromUser.getName() + ": " + message);
            }
        });
        client.addObserver(new OnPrivateMessage() {
            @Override
            public void onEvent(IEvent event) {
                String message = (String) event.getParams().getParam("message").getObject();
                IUser fromUser = (IUser) event.getParams().getParam("user").getObject();
                System.out.println("Private message.");
                System.out.println("\tfrom " + fromUser.getName() + ": " + message);
            }
        });
        client.addObserver(new OnUserEnteredRoom() {
            @Override
            public void onEvent(IEvent event) {
                IRoom room = (IRoom) event.getParams().getParam("room").getObject();
                IUser user = (IUser) event.getParams().getParam("user").getObject();
                System.out.println("User entered to room:");
                System.out.println("\t" + user.getName() + " has been entered the room " + room.getName());
            }
        });
        client.addObserver(new OnUserExitedRoom() {
            @Override
            public void onEvent(IEvent event) {
                IRoom room = (IRoom) event.getParams().getParam("room").getObject();
                IUser user = (IUser) event.getParams().getParam("user").getObject();
                System.out.println("User exited from room:");
                System.out.println("\t" + user.getName() + " has been exited from room " + room.getName());
            }
        });
        client.addObserver(new OnUserDisconnected() {
            @Override
            public void onEvent(IEvent event) {
                IUser user = (IUser) event.getParams().getParam("user").getObject();
                System.out.println("User disconnected.");
                System.out.println("\t" + user.getName() + " has been disconnected");
            }
        });
    }

    private static class OnJoinRoomImpl extends OnJoinRoom {

        private Observer onJoinRoomError;

        @Override
        public void onEvent(IEvent event) {
            client.deleteObserver(this);
            client.deleteObserver(onJoinRoomError);
            IRoom room = (IRoom) event.getParams().getParam("room").getObject();
            System.out.println("You've been successfully joined the room: ");
            System.out.println("\tRoomName: " + room.getName() + "; RoomId: " + room.getId() + ";");
        }

        public void setOnJoinRoomError(Observer onJoinRoomError) {
            this.onJoinRoomError = onJoinRoomError;
        }
    }
}
