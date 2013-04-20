package com.bitwaffle.guts.net.server;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entity.Entity;
import com.bitwaffle.guts.net.NetRegistrar;
import com.bitwaffle.guts.net.messages.EntityRoomInfoSender;
import com.bitwaffle.guts.net.messages.PlayerCreateMessage;
import com.bitwaffle.guts.net.messages.PlayerUpdateMessage;
import com.bitwaffle.guts.net.messages.PlayerUpdateRequest;
import com.bitwaffle.guts.net.messages.entity.BreakableRockCreateMessage;
import com.bitwaffle.guts.physics.Entities.EntityRemoveRequest;
import com.bitwaffle.guts.physics.PhysicsHelper;
import com.bitwaffle.offworld.entities.dynamic.BreakableRock;
import com.bitwaffle.offworld.entities.player.Player;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class GameServer extends Listener {
	/** Ports to use */
	private static final int 
			DEFAULT_UDP_PORT = 42042,
			DEFAULT_TCP_PORT = 42024;
	
	/** The actual server */
	private Server server;
	
	private int playerNumber = -1;
	
	private int ticksSinceLastUpdate = 0;
	
	private int playerUpdateSpeed = 2;
	
	private ConcurrentHashMap<Connection, ServerConnection> connections;
	
	public GameServer(){
		connections = new ConcurrentHashMap<Connection, ServerConnection>();
		server = new Server();
		
		// FIXME temp server just takes control of 0th player
		PhysicsHelper.initPlayer(Game.physics, new Vector2(1.0f, 5.0f), 0, true);
		playerNumber = 0;
		
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
	
	public void update(){
		for(ServerConnection con : connections.values())
			con.update();
		
		if(playerNumber != -1){
			// FIXME temporary? just send player data
			ticksSinceLastUpdate++;
			
			if(ticksSinceLastUpdate > playerUpdateSpeed){
				for(ServerConnection con : connections.values())
					sendPlayerInfoReply(con.connection(), playerNumber);
				ticksSinceLastUpdate = 0;
			}
		}
	}
	
	// FIXME this should probs be in player class
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
		// TODO things other than rocks- maybe method in entity?
		if(ent instanceof BreakableRock){
			BreakableRock rock = (BreakableRock) ent;
			BreakableRockCreateMessage req = new BreakableRockCreateMessage();
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
		// TODO test this on things besides rocks
		EntityRemoveRequest req = new EntityRemoveRequest(ent.getLayer(), ent.hashCode());
		for(ServerConnection con : connections.values())
			con.connection().sendTCP(req);	
	}
	
	@Override
	public void connected(Connection connection) {
		super.connected(connection);
		
		ServerConnection servcon = new ServerConnection(connection);
		
		// sync players with new connection
		playerJoined(connection);
		
		connections.put(connection, servcon);
		
		// entities can only safely be iterated during physics loop
		Game.physics.addUpdateRequest(new EntityRoomInfoSender(connection));
	}
	
	private void playerJoined(Connection connection){
		// figure out which player is joining
		int playerNum = -1;
		for(int i = 0; i < Game.players.length; i++){
			// assign to first null slot
			if(Game.players[i] == null){
				playerNum = i;
				// create the player on the server
				PhysicsHelper.initPlayer(Game.physics, new Vector2(1.0f, 16.0f), playerNum, false);

				// create the player on the client
				PlayerCreateMessage msg = new PlayerCreateMessage();
				msg.playerNumber = playerNum;
				msg.x = 1.0f;
				msg.y = 16.0f;
				msg.takeControl = true; // tells the client to take control of the player
				connection.sendTCP(msg);
				
				// create player on previously connected clients
				for(ServerConnection con : connections.values()){
					msg.takeControl = false;
					con.connection().sendTCP(msg);
				}
				break;
			}
		}
		
		// send any existing players to newly connected player
		for(int i = 0; i < Game.players.length; i++){
			if(Game.players[i] != null && i != playerNum){
				PlayerCreateMessage msg = new PlayerCreateMessage();
				msg.playerNumber = i;
				Player p = Game.players[i];
				msg.x = p.body.getPosition().x;
				msg.y = p.body.getPosition().y;
				msg.takeControl = false;
				connection.sendTCP(msg);
			}
		}
	}

	@Override
	public void disconnected(Connection connection) {
		super.disconnected(connection);
		
		connections.remove(connection);
	}

	@Override
	public void received(Connection connection, Object object) {
		super.received(connection, object);
		
		if (object instanceof PlayerUpdateMessage) {
			PlayerUpdateMessage reply = (PlayerUpdateMessage) object;
			Game.physics.addUpdateRequest(new PlayerUpdateRequest(reply));
			for(ServerConnection con : connections.values()){
				if(con.connection() != connection)
					con.connection().sendUDP(reply);
			}
		}
	}

	@Override
	public void idle(Connection connection) {
		super.idle(connection);
	}
}