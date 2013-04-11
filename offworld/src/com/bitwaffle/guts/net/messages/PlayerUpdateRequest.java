package com.bitwaffle.guts.net.messages;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.physics.EntityUpdateRequest;
import com.bitwaffle.offworld.entities.player.Player;

/**
 * Updates a player based on some data from a client
 */
public class PlayerUpdateRequest implements EntityUpdateRequest {
	private PlayerUpdateMessage reply;

	public PlayerUpdateRequest(PlayerUpdateMessage reply) {
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
