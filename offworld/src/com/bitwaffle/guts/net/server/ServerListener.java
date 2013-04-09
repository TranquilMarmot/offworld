package com.bitwaffle.guts.net.server;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.net.messages.PlayerInfoReply;
import com.bitwaffle.guts.net.messages.PlayerInfoRequest;
import com.bitwaffle.guts.net.messages.SomeReply;
import com.bitwaffle.guts.net.messages.SomeRequest;
import com.bitwaffle.guts.physics.EntityUpdateRequest;
import com.bitwaffle.offworld.entities.player.Player;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class ServerListener extends Listener {

	@Override
	public void connected(Connection connection) {
		super.connected(connection);
		PlayerInfoRequest req = new PlayerInfoRequest();
		req.playerNumber = 0;
		connection.sendTCP(req);
	}

	@Override
	public void disconnected(Connection connection) {
		super.disconnected(connection);
	}

	@Override
	public void received(Connection connection, Object object) {
		super.received(connection, object);
		if (object instanceof SomeRequest) {
			SomeRequest request = (SomeRequest) object;
			Game.out.println(request.wat);

			SomeReply response = new SomeReply();
			response.wat = "Thanks!";
			connection.sendTCP(response);
		} else if (object instanceof PlayerInfoReply) {
			PlayerInfoReply reply = (PlayerInfoReply) object;

			Game.physics.addEntityUpdateRequest(new PlayerUpdateRequest(reply));

			//Game.out.println("set player values, sending request");
			PlayerInfoRequest req = new PlayerInfoRequest();
			req.playerNumber = reply.playerNumber;
			//connection.sendTCP(req);
		}
	}

	private class PlayerUpdateRequest implements EntityUpdateRequest {
		private PlayerInfoReply reply;

		public PlayerUpdateRequest(PlayerInfoReply reply) {
			this.reply = reply;
		}

		@Override
		public void updateEntity() {
			Player player = Game.players[reply.playerNumber];
			player.body.setTransform(new Vector2(reply.x, reply.y), 0);
			player.body.setLinearVelocity(reply.dx, reply.dy);
			player.setTarget(new Vector2(reply.aimX, reply.aimY));

			if (reply.jetpack)
				player.jetpack.enable();
			else
				player.jetpack.disable();

			if (reply.shooting)
				player.beginShooting();
			else
				player.endShooting();
		}

	}

	@Override
	public void idle(Connection connection) {
		super.idle(connection);
	}
}
