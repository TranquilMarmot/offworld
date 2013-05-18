package com.bitwaffle.guts.net.client;

import java.io.IOException;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entity.Entity;
import com.bitwaffle.guts.net.NetRegistrar;
import com.bitwaffle.guts.net.messages.entity.DynamicEntityUpdateMessage;
import com.bitwaffle.guts.net.messages.entity.DynamicEntityUpdateRequest;
import com.bitwaffle.guts.physics.Entities.EntityRemoveRequest;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

/**
 * A client that communicates with a server
 * 
 * @author TranquilMarmot
 */
public abstract class GameClient extends Listener {
	/** Ports to use */
	private static final int 
			DEFAULT_UDP_PORT = 42042,
			DEFAULT_TCP_PORT = 42024;

	/** The actual client */
	protected Client client;


	/**
	 * @param host Host to connect to
	 */
	public GameClient(String host) {
		client = new Client();

		NetRegistrar.registerClasses(client.getKryo());
		
		client.addListener(this);
		client.start();
		try {
			client.connect(12000, host, DEFAULT_TCP_PORT, DEFAULT_UDP_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public abstract void update(float timeStep);
	

	
	@Override
	public void connected(Connection connection){
		super.connected(connection);
	}
	
	@Override
	public void disconnected(Connection connection){
		super.disconnected(connection);
	}

	@Override
	public void received(Connection connection, Object object) {
		super.received(connection, object);
		
		// generic dynamic entity update
		if(object instanceof DynamicEntityUpdateMessage){
			DynamicEntityUpdateMessage req = (DynamicEntityUpdateMessage)object;
			Game.physics.addUpdateRequest(new DynamicEntityUpdateRequest(req));
		}
		
		// entity removed from game
		else if(object instanceof EntityRemoveRequest){
			EntityRemoveRequest req = (EntityRemoveRequest)object;
			Entity ent = Game.physics.get2DEntity(req.layer, req.hash);
			if(ent != null)
				Game.physics.removeEntity(ent, req.hash, true); // FIXME dont know whether or not to remove from room here
		}
	}
	
	@Override
	public void idle(Connection connection){
		super.idle(connection);
	}
}
