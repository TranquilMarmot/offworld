package com.bitwaffle.guts.net.client;

import java.io.IOException;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.net.NetRegistrar;
import com.bitwaffle.guts.net.messages.PlayerInfoReply;
import com.bitwaffle.guts.net.messages.SomeRequest;
import com.bitwaffle.offworld.entities.player.Player;
import com.esotericsoftware.kryonet.Client;

public class GameClient {
	private static final int DEFAULT_UDP_PORT = 42042,
			DEFAULT_TCP_PORT = 42024;

	private Client client;

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
		if (client.isConnected()) {
			PlayerInfoReply reply = new PlayerInfoReply();
			reply.playerNumber = 0;
			Player player = Game.players[reply.playerNumber];
			reply.x = player.body.getPosition().x;
			reply.y = player.body.getPosition().y;
			reply.aimX = player.getCurrentTarget().x;
			reply.aimY = player.getCurrentTarget().y;
			reply.jetpack = player.jetpack.isEnabled();
			reply.shooting = player.isShooting();

			client.sendUDP(reply);
		}
	}
}
