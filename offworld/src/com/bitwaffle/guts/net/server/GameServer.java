package com.bitwaffle.guts.net.server;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import com.bitwaffle.guts.entities.Entity;
import com.bitwaffle.guts.entities.EntityRemoveRequest;
import com.bitwaffle.guts.net.NetRegistrar;
import com.bitwaffle.guts.net.messages.entity.BreakableRockCreateRequest;
import com.bitwaffle.offworld.entities.dynamic.BreakableRock;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

public class GameServer {
	/** Ports to use */
	private static final int 
			DEFAULT_UDP_PORT = 42042,
			DEFAULT_TCP_PORT = 42024;
	
	/** The actual server */
	private Server server;
	
	protected ConcurrentHashMap<Connection, ServerConnection> connections;
	
	public GameServer(){
		connections = new ConcurrentHashMap<Connection, ServerConnection>();
		server = new Server();
		
		NetRegistrar.registerClasses(server.getKryo());
		
		server.addListener(new ServerListener(this));
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
	
	public void update(){
		for(ServerConnection con : connections.values())
			con.update();
	}

	public void entityAddedNotification(Entity ent) {
		if(ent instanceof BreakableRock){
			BreakableRock rock = (BreakableRock) ent;
			BreakableRockCreateRequest req = new BreakableRockCreateRequest();
			req.x = rock.body.getPosition().x;
			req.y = rock.body.getPosition().y;
			req.name = rock.chosenName;
			req.hash = rock.hashCode();
			req.layer = rock.getLayer();
			float[] color = rock.getColor();
			req.r = color[0];
			req.g = color[1];
			req.b = color[2];
			req.a = color[3];
			req.scale = rock.getScale();
			for(ServerConnection con : connections.values())
				con.connection().sendTCP(req);
		}
	}

	public void entityRemovedNotification(Entity ent) {
		if(ent instanceof BreakableRock){
			EntityRemoveRequest req = new EntityRemoveRequest();
			req.layer = ent.getLayer();
			req.hash = ent.hashCode();
			for(ServerConnection con : connections.values())
				con.connection().sendTCP(req);	
		}
		
	}
}