package com.bitwaffle.guts.net.messages.entity;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.Entity;
import com.bitwaffle.guts.physics.PhysicsUpdateRequest;

public class EntityUpdateRequest implements PhysicsUpdateRequest {
	
	private EntityUpdateMessage msg;
	
	public EntityUpdateRequest(EntityUpdateMessage msg){
		this.msg = msg;
	}
	
	@Override
	public void doRequest(){
		Entity ent = Game.physics.getEntity(msg.layer, msg.hash);
		ent.setLocation(new Vector2(msg.x, msg.y));
		ent.setAngle(msg.angle);
	}
}
