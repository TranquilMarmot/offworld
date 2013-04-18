package com.bitwaffle.guts.net.messages.entity;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.dynamic.DynamicEntity;
import com.bitwaffle.guts.physics.PhysicsUpdateRequest;

public class DynamicEntityUpdateRequest implements PhysicsUpdateRequest {
	private DynamicEntityUpdateMessage message;
	
	public DynamicEntityUpdateRequest(DynamicEntityUpdateMessage message){
		this.message = message;
	}

	@Override
	public void doRequest() {
		DynamicEntity ent = (DynamicEntity)(Game.physics.get2DEntity(message.layer, message.hash));
		if(ent != null){
			ent.body.setTransform(new Vector2(message.x, message.y), message.angle);
			ent.body.setLinearVelocity(message.dx, message.dy);
			ent.body.setAngularVelocity(message.da);
		}
	}
}
