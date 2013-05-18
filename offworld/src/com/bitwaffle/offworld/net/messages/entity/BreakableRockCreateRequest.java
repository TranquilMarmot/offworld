package com.bitwaffle.offworld.net.messages.entity;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.physics.PhysicsUpdateRequest;
import com.bitwaffle.offworld.entities.BreakableRock;

public class BreakableRockCreateRequest implements PhysicsUpdateRequest {
	BreakableRockCreateMessage req;

	public BreakableRockCreateRequest(BreakableRockCreateMessage req) {
		this.req = req;
	}

	@Override
	public void doRequest() {
		BreakableRock rock = new BreakableRock(
				req.name,
				new Vector2(req.x, req.y),
				req.scale,
				new float[]{ req.r, req.g, req.b, req.a},
				req.layer);
		Game.physics.addEntity(rock, req.hash, true);
	}

}
