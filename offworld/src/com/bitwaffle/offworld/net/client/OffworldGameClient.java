package com.bitwaffle.offworld.net.client;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.net.client.GameClient;
import com.bitwaffle.offworld.OffworldGame;
import com.bitwaffle.offworld.net.OffworldNetRegistrar;
import com.bitwaffle.offworld.net.messages.PlayerCreateMessage;
import com.bitwaffle.offworld.net.messages.PlayerUpdateMessage;
import com.bitwaffle.offworld.net.messages.PlayerUpdateRequest;
import com.bitwaffle.offworld.net.messages.entity.BreakableRockCreateMessage;
import com.bitwaffle.offworld.net.messages.entity.BreakableRockCreateRequest;
import com.bitwaffle.offworld.physics.OffworldPhysics;
import com.esotericsoftware.kryonet.Connection;


public class OffworldGameClient extends GameClient {
	
	private int playerUpdateSpeed = 2;
	
	private int playerNumber = -1;
	
	private int ticksSinceLastUpdate = 0;
	
	public OffworldGameClient(String host){
		super(host);
		
		OffworldNetRegistrar.registerClasses(client.getKryo());
	}
	
	public void setPlayerNumber(int number){ this.playerNumber = number; }
	public int playerNumber(){ return this.playerNumber; }
	
	@Override
	public void update(float timeStep){
		// temporary, just send player data
		ticksSinceLastUpdate++;
		
		if(ticksSinceLastUpdate > playerUpdateSpeed){
			if (client.isConnected() && playerNumber >= 0) 
				sendPlayerUpdateMessage(client, playerNumber);

			ticksSinceLastUpdate = 0;
		}
	}
	
	/** Updates a player on a remote connection */
	public void sendPlayerUpdateMessage(Connection connection, int playerNumber){
		PlayerUpdateMessage reply = new PlayerUpdateMessage();
		reply.playerNumber = playerNumber;
		com.bitwaffle.offworld.entities.player.Player player = OffworldGame.players[reply.playerNumber];
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
	public void received(Connection connection, Object object){
		
		// TODO fix thisss!!!
		// rather than using a big block of if/else instanceof's, have every method extend some
		// message class that has like onRecieve and onSend methods
		// Then just cast the message to the custom class and call the required method
		
		
		// new player (can set to be controlled by this client)
		if(object instanceof PlayerCreateMessage){
			PlayerCreateMessage msg = (PlayerCreateMessage)object;
			OffworldPhysics.initPlayer(Game.physics, new Vector2(msg.x, msg.y), msg.playerNumber, msg.takeControl);
			if(msg.takeControl)
				setPlayerNumber(msg.playerNumber);
		} 
		
		// updates player location/firing info
		else if(object instanceof PlayerUpdateMessage){
			PlayerUpdateMessage msg = (PlayerUpdateMessage)object;
			Game.physics.addUpdateRequest(new PlayerUpdateRequest(msg));
		} 
		
		// create breakable rock
		else if(object instanceof BreakableRockCreateMessage){
			BreakableRockCreateMessage req = (BreakableRockCreateMessage)object;
			Game.physics.addUpdateRequest(new BreakableRockCreateRequest(req));
		}
		
		else{
			super.received(connection, object);
		}
	}

}
