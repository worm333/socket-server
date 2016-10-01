# socket-server
This is "for fun" project to which I return from time to time.

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
