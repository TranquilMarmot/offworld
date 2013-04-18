package com.bitwaffle.guts.net.client;

import java.io.IOException;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.entities2d.Entity2D;
import com.bitwaffle.guts.entities.entities2d.Entity2DRemoveRequest;
import com.bitwaffle.guts.net.NetRegistrar;
import com.bitwaffle.guts.net.messages.PlayerCreateMessage;
import com.bitwaffle.guts.net.messages.PlayerUpdateMessage;
import com.bitwaffle.guts.net.messages.PlayerUpdateRequest;
import com.bitwaffle.guts.net.messages.entity.BreakableRockCreateMessage;
import com.bitwaffle.guts.net.messages.entity.BreakableRockCreateRequest;
import com.bitwaffle.guts.net.messages.entity.DynamicEntityUpdateMessage;
import com.bitwaffle.guts.net.messages.entity.DynamicEntityUpdateRequest;
import com.bitwaffle.guts.physics.PhysicsHelper;
import com.bitwaffle.offworld.entities.player.Player;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

/**
 * A client that communicates with a server
 * 
 * @author TranquilMarmot
 */
public class GameClient extends Listener {
	/** Ports to use */
	private static final int 
			DEFAULT_UDP_PORT = 42042,
			DEFAULT_TCP_PORT = 42024;

	/** The actual client */
	private Client client;
	
	private int ticksSinceLastUpdate = 0;
	
	private int playerUpdateSpeed = 2;
	
	private int playerNumber = -1;

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
	
	public void setPlayerNumber(int number){ this.playerNumber = number; }
	public int playerNumber(){ return this.playerNumber; }

	public void update() {
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
		
		// create breakable rock
		if(object instanceof BreakableRockCreateMessage){
			BreakableRockCreateMessage req = (BreakableRockCreateMessage)object;
			Game.physics.addUpdateRequest(new BreakableRockCreateRequest(req));
		}
		
		// generic dynamic entity update
		else if(object instanceof DynamicEntityUpdateMessage){
			DynamicEntityUpdateMessage req = (DynamicEntityUpdateMessage)object;
			Game.physics.addUpdateRequest(new DynamicEntityUpdateRequest(req));
		}
		
		// entity removed from game
		else if(object instanceof Entity2DRemoveRequest){
			Entity2DRemoveRequest req = (Entity2DRemoveRequest)object;
			Entity2D ent = Game.physics.get2DEntity(req.layer, req.hash);
			if(ent != null)
				Game.physics.removeEntity(ent, req.hash, true); // FIXME dont know whether or not to remove from room here
		}
		
		// new player (can set to be controlled by this client)
		else if(object instanceof PlayerCreateMessage){
			PlayerCreateMessage msg = (PlayerCreateMessage)object;
			PhysicsHelper.initPlayer(Game.physics, new Vector2(msg.x, msg.y), msg.playerNumber, msg.takeControl);
			if(msg.takeControl)
				setPlayerNumber(msg.playerNumber);
		}
		
		// updates player location/firing info
		else if(object instanceof PlayerUpdateMessage){
			PlayerUpdateMessage msg = (PlayerUpdateMessage)object;
			Game.physics.addUpdateRequest(new PlayerUpdateRequest(msg));
		}
	}
	
	@Override
	public void idle(Connection connection){
		super.idle(connection);
	}
}
