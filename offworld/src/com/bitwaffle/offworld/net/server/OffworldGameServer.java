package com.bitwaffle.offworld.net.server;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entity.Entity;
import com.bitwaffle.guts.net.server.GameServer;
import com.bitwaffle.guts.net.server.ServerConnection;
import com.bitwaffle.offworld.OffworldGame;
import com.bitwaffle.offworld.entities.BreakableRock;
import com.bitwaffle.offworld.entities.player.Player;
import com.bitwaffle.offworld.net.OffworldNetRegistrar;
import com.bitwaffle.offworld.net.messages.OffworldEntityRoomInfoSender;
import com.bitwaffle.offworld.net.messages.PlayerCreateMessage;
import com.bitwaffle.offworld.net.messages.PlayerUpdateMessage;
import com.bitwaffle.offworld.net.messages.PlayerUpdateRequest;
import com.bitwaffle.offworld.net.messages.entity.BreakableRockCreateMessage;
import com.bitwaffle.offworld.physics.OffworldPhysics;
import com.esotericsoftware.kryonet.Connection;

public class OffworldGameServer extends GameServer {
	private int playerNumber = -1;
	
	private int ticksSinceLastUpdate = 0;
	
	private int playerUpdateSpeed = 2;
	
	public OffworldGameServer(){
		super();
		
		OffworldNetRegistrar.registerClasses(server.getKryo());
		
		// FIXME temp server just takes control of 0th player
		OffworldPhysics.initPlayer(Game.physics, new Vector2(111.58881f, -21.654314f), 0, true);
		playerNumber = 0;
	}
	
	@Override
	public void update(float timeStep){
		super.update(timeStep);
		
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
	
	@Override
	public void connected(Connection connection){
		super.connected(connection);
		
		// sync players with new connection
		playerJoined(connection);
		
		// entities can only safely be iterated during physics loop
		Game.physics.addUpdateRequest(new OffworldEntityRoomInfoSender(connection));
	}
	
	@Override
	protected ServerConnection getServerConnection(Connection connection){
		return new OffworldServerConnection(connection);
	}
	
	@Override
	public void received(Connection connection, Object object){
		if (object instanceof PlayerUpdateMessage) {
			PlayerUpdateMessage reply = (PlayerUpdateMessage) object;
			Game.physics.addUpdateRequest(new PlayerUpdateRequest(reply));
			for(ServerConnection con : connections.values()){
				if(con.connection() != connection)
					con.connection().sendUDP(reply);
			}
		}
	}
	
	// FIXME this should probs be in player class
	public void sendPlayerInfoReply(Connection connection, int playerNumber){
		PlayerUpdateMessage reply = new PlayerUpdateMessage();
		reply.playerNumber = playerNumber;
		Player player = OffworldGame.players[reply.playerNumber];
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

	@Override
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
	
	private void playerJoined(Connection connection){
		// figure out which player is joining
		int playerNum = -1;
		for(int i = 0; i < OffworldGame.players.length; i++){
			// assign to first null slot
			if(OffworldGame.players[i] == null){
				playerNum = i;
				// create the player on the server
				OffworldPhysics.initPlayer(Game.physics, new Vector2(1.0f, 16.0f), playerNum, false);

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
		for(int i = 0; i < OffworldGame.players.length; i++){
			if(OffworldGame.players[i] != null && i != playerNum){
				PlayerCreateMessage msg = new PlayerCreateMessage();
				msg.playerNumber = i;
				Player p = OffworldGame.players[i];
				msg.x = p.body.getPosition().x;
				msg.y = p.body.getPosition().y;
				msg.takeControl = false;
				connection.sendTCP(msg);
			}
		}
	}
}
