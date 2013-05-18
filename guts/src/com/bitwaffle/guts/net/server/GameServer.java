package com.bitwaffle.guts.net.server;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import com.bitwaffle.guts.entity.Entity;
import com.bitwaffle.guts.net.NetRegistrar;
import com.bitwaffle.guts.physics.Entities.EntityRemoveRequest;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public abstract class GameServer extends Listener {
	/** Ports to use */
	protected static final int 
			DEFAULT_UDP_PORT = 42042,
			DEFAULT_TCP_PORT = 42024;
	
	/** The actual server */
	protected Server server;
	
	protected ConcurrentHashMap<Connection, ServerConnection> connections;
	
	public GameServer(){
		connections = new ConcurrentHashMap<Connection, ServerConnection>();
		server = new Server();
		
		NetRegistrar.registerClasses(server.getKryo());
		
		server.addListener(this);
		server.start();
		
		try {
			server.bind(DEFAULT_TCP_PORT, DEFAULT_UDP_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void endServer(){
		server.close();
		server = null;
	}
	
	public void update(float timeStep){
		for(ServerConnection con : connections.values())
			con.update(timeStep);
	}

	public abstract void entityAddedNotification(Entity ent);

	public void entityRemovedNotification(Entity ent) {
		// TODO test this on things besides rocks
		EntityRemoveRequest req = new EntityRemoveRequest(ent.getLayer(), ent.hashCode());
		for(ServerConnection con : connections.values())
			con.connection().sendTCP(req);	
	}
	
	@Override
	public void connected(Connection connection) {
		super.connected(connection);
	
		connections.put(connection, getServerConnection(connection));
	}
	
	protected abstract ServerConnection getServerConnection(Connection connection);

	@Override
	public void disconnected(Connection connection) {
		super.disconnected(connection);
		
		connections.remove(connection);
	}

	@Override
	public void received(Connection connection, Object object) {
		super.received(connection, object);
	}

	@Override
	public void idle(Connection connection) {
		super.idle(connection);
	}
}