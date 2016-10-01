# socket-server
This is "for fun" project to which I return from time to time.
# Usage
Create server
```
   server = new Server(port);
   server.start();
```

Create client
```
   client = new Client();
   client.start(host, port);
```
add observers to client
```
       client.addObserver(new OnPublicMessage() {
            @Override
            public void onEvent(IEvent event) {
                String message = (String) event.getParams().getParam("message").getObject();
                IUser fromUser = (IUser) event.getParams().getParam("user").getObject();
                System.out.println("Public message from " + fromUser.getName() + ": " + message);
            }
        });
        client.addObserver(new OnPrivateMessage() {
            @Override
            public void onEvent(IEvent event) {
                String message = (String) event.getParams().getParam("message").getObject();
                IUser fromUser = (IUser) event.getParams().getParam("user").getObject();
                System.out.println("Private message from " + fromUser.getName() + ": " + message);
            }
        });
        client.addObserver(new OnUserEnteredRoom() {
            @Override
            public void onEvent(IEvent event) {
                IRoom room = (IRoom) event.getParams().getParam("room").getObject();
                IUser user = (IUser) event.getParams().getParam("user").getObject();
                System.out.println("User " + user.getName() + " has been entered the room " + room.getName());
            }
        });
```
basically you can add any observer declared in package `com.eugeniuparvan.multiplayer.client.observers`.

# Sample project

Consists of server and client parts.

The client can send the following commands:
  * getRooms
  * getUsers
  * sendPrivateMessage
  * sendPublicMessage
  * getJoinedRooms
  * createRoom
  * joinRoom
  * exitRoom
  * stop

Build the project:
```
git clone https://github.com/worm333/socket-server.git 
cd socket-server
mvn clean package
```

Run server part:
```
java -jar example/server/target/com.eugeniuparvan.socketserver.example.server-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```

Run client part:
```
java -jar example/client/target/com.eugeniuparvan.socketserver.example.client-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```

p.s. Open terminal and run the server first, then launch several clients in separate tabs. Have fun ;)
