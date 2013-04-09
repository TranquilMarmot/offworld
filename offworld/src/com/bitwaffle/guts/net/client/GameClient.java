package com.bitwaffle.guts.net.client;

import java.io.IOException;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.net.NetRegistrar;
import com.bitwaffle.guts.net.messages.PlayerInfoReply;
import com.bitwaffle.guts.net.messages.SomeRequest;
import com.bitwaffle.offworld.entities.player.Player;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;

/**
 * A client that communicates with a server
 * 
 * @author TranquilMarmot
 */
public class GameClient {
	/** Ports to use */
	private static final int 
			DEFAULT_UDP_PORT = 42042,
			DEFAULT_TCP_PORT = 42024;

	/** The actual client */
	private Client client;

	/**
	 * @param host Host to connect to
	 */
	public GameClient(String host) {
		client = new Client();

		NetRegistrar.registerClasses(client.getKryo());
		
		client.addListener(new ClientListener());
		client.start();
		try {
			client.connect(12000, host, DEFAULT_TCP_PORT, DEFAULT_UDP_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void send(String message) {
		Game.out.println("Sending " + message);
		SomeRequest req = new SomeRequest();
		req.wat = message;
		client.sendTCP(req);
	}

	public void update() {
		// temporary, just send player data
		if (client.isConnected()) {
			sendPlayerInfoReply(client, 0);
		}
	}
	
	public void sendPlayerInfoReply(Connection connection, int playerNumber){
		PlayerInfoReply reply = new PlayerInfoReply();
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
}
