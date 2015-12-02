package com.eugeniuparvan.multiplayer.server;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;

import com.eugeniuparvan.multiplayer.core.entity.IRoom;
import com.eugeniuparvan.multiplayer.core.entity.IUser;
import com.eugeniuparvan.multiplayer.core.entity.Room;

public class Server implements IServer
{
    private static final Logger logger = Logger.getLogger(Server.class);
    
    private final int port;
    private boolean isRunning;
    private ConcurrentMap<IConnection, Boolean> connectionMap;
    private ConcurrentMap<Long, IConnection> userIdConnectionMap;
    private ConcurrentMap<IRoom, String> roomPassMap;
    private ConcurrentMap<Long, IRoom> idRoomMap;
    private final IRoom defaultRoom;
    private AtomicLong connectionsIndex;
    private AtomicLong roomIndex;
    
    public Server(int port)
    {
	this.port = port;
	this.connectionMap = new ConcurrentHashMap<IConnection, Boolean>();
	this.userIdConnectionMap = new ConcurrentHashMap<Long, IConnection>();
	this.roomPassMap = new ConcurrentHashMap<IRoom, String>();
	this.idRoomMap = new ConcurrentHashMap<Long, IRoom>();
	this.connectionsIndex = new AtomicLong(-1);
	this.roomIndex = new AtomicLong(-1);
	this.defaultRoom = new Room(generateRoomId(), Room.DEFAULT_NAME, false, false);
	roomPassMap.putIfAbsent(defaultRoom, "");
	idRoomMap.putIfAbsent(defaultRoom.getId(), defaultRoom);
    }
    
    @Override
    public void start()
    {
	isRunning = true;
	ServerSocket serverSocket = null;
	ExecutorService executor = null;
	try
	{
	    serverSocket = new ServerSocket(port);
	    executor = Executors.newCachedThreadPool();
	    while (isRunning)
	    {
		Socket socket = serverSocket.accept();
		if (!isRunning)
		    break;
		IConnection connection = new Connection(socket, this);
		executor.submit(connection);
		connectionMap.putIfAbsent(connection, true);
		userIdConnectionMap.putIfAbsent(connection.getUser().getId(), connection);
	    }
	}
	catch (IOException e)
	{
	    logger.fatal("TODO: nice message", e);// TODO: nice message
	}
	
	releaseResources(serverSocket);
	
	try
	{
	    if (executor.awaitTermination(10, TimeUnit.SECONDS))
		logger.info("Shutting down server: task completed");
	    else
		logger.info("Shutting down server: Forcing shutdown...");
	    executor.shutdownNow();
	}
	catch (InterruptedException e)
	{
	    logger.error("TODO: nice message", e);// TODO: nice message
	}
	logger.info("Server is stopped.");
    }
    
    @Override
    public void stop()
    {
	isRunning = false;
	try
	{
	    Socket socket = new Socket("localhost", port);
	    socket.close();
	}
	catch (Exception e)
	{
	    logger.error("TODO: nice message", e);// TODO: nice message
	}
	
    }
    
    @Override
    public long generateUserId()
    {
	if (connectionsIndex.get() == Long.MAX_VALUE)
	    connectionsIndex.set(-1);
	return connectionsIndex.incrementAndGet();
    }
    
    @Override
    public long generateRoomId()
    {
	if (roomIndex.get() == Long.MAX_VALUE)
	    roomIndex.set(-1);
	return roomIndex.incrementAndGet();
    }
    
    @Override
    public Set<IConnection> getConnections()
    {
	return connectionMap.keySet();
    }
    
    @Override
    public void removeConnection(IConnection connection)
    {
	IUser user = connection.getUser();
	user.getCurrentRoom().removeUser(user);
	userIdConnectionMap.remove(user.getId());
	connectionMap.remove(connection);
    }
    
    @Override
    public IRoom getRoom(Long roomId)
    {
	return idRoomMap.get(roomId);
    }
    
    @Override
    public IConnection getConnection(Long userId)
    {
	return userIdConnectionMap.get(userId);
    }
    
    @Override
    public Set<IRoom> getRooms()
    {
	return roomPassMap.keySet();
    }
    
    @Override
    public IRoom getDefaultRoom()
    {
	return defaultRoom;
    }
    
    @Override
    public IRoom createRoom(String name)
    {
	return null;
    }
    
    @Override
    public IRoom createRoom(String name, String password)
    {
	IRoom room = null;
	if (password == null || "".equals(password))
	{
	    room = new Room(generateRoomId(), name, false, true);
	    roomPassMap.putIfAbsent(room, "");
	}
	else
	{
	    room = new Room(generateRoomId(), name, true, true);
	    roomPassMap.putIfAbsent(room, password);
	}
	idRoomMap.putIfAbsent(room.getId(), room);
	return room;
    }
    
    @Override
    public IRoom joinRoom(long roomId, IUser user, String password)
    {
	IRoom room = idRoomMap.get(roomId);
	if (password == null || "".equals(password))
	{
	    user.setCurrentRoom(room);
	    return room;
	}
	else
	{
	    String pass = roomPassMap.get(room);
	    if (password.equals(pass))
	    {
		user.setCurrentRoom(room);
		return room;
	    }
	}
	return null;
    }
    
    private void releaseResources(ServerSocket serverSocket)
    {
	close(serverSocket);
	for (IConnection serverThread : connectionMap.keySet())
	{
	    close(serverThread.getInputStream());
	    close(serverThread.getOutputStream());
	    close(serverThread.getSocket());
	}
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
}
