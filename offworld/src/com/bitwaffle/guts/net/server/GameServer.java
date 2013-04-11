package com.bitwaffle.guts.net.server;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.Entity;
import com.bitwaffle.guts.entities.EntityRemoveRequest;
import com.bitwaffle.guts.net.NetRegistrar;
import com.bitwaffle.guts.net.messages.PlayerUpdateMessage;
import com.bitwaffle.guts.net.messages.entity.BreakableRockCreateRequest;
import com.bitwaffle.guts.physics.PhysicsHelper;
import com.bitwaffle.offworld.entities.dynamic.BreakableRock;
import com.bitwaffle.offworld.entities.player.Player;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

public class GameServer {
	/** Ports to use */
	private static final int 
			DEFAULT_UDP_PORT = 42042,
			DEFAULT_TCP_PORT = 42024;
	
	/** The actual server */
	private Server server;
	
	private int playerNumber = -1;
	
	private int ticksSinceLastUpdate = 0;
	
	private int playerUpdateSpeed = 2;
	
	protected ConcurrentHashMap<Connection, ServerConnection> connections;
	
	public GameServer(){
		connections = new ConcurrentHashMap<Connection, ServerConnection>();
		server = new Server();
		
		// FIXME temp
		PhysicsHelper.initPlayer(Game.physics, new Vector2(1.0f, 5.0f), 0, true);
		playerNumber = 0;
		
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
		
		if(playerNumber != -1){
			// temporary, just send player data
			ticksSinceLastUpdate++;
			
			if(ticksSinceLastUpdate > playerUpdateSpeed){
				for(ServerConnection con : connections.values())
					sendPlayerInfoReply(con.connection(), playerNumber);
				ticksSinceLastUpdate = 0;
			}
		}
	}
	
	public void sendPlayerInfoReply(Connection connection, int playerNumber){
		PlayerUpdateMessage reply = new PlayerUpdateMessage();
		reply.playerNumber = playerNumber;
		Player player = Game.players[reply.playerNumber];
		reply.x = player.body.getPosition().x;
		reply.y = player.body.getPosition().y;
		reply.dx = player.body.getLinearVelocity().x;
		reply.dy = player.body.getLinearVelocity().y;
		reply.aimX = player.getCurrentTarget().x;
		reply.aimY = player.getCurrentTarget().y;
		reply.jetpack = player.jetpack.isEnabled();
		reply.shooting = player.isShooting();
		
		connection.sendUDP(reply);
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