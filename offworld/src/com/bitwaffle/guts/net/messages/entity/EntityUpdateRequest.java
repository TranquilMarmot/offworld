package com.bitwaffle.guts.net.messages.entity;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.entities2d.Entity2D;
import com.bitwaffle.guts.physics.PhysicsUpdateRequest;

public class EntityUpdateRequest implements PhysicsUpdateRequest {
	
	private EntityUpdateMessage msg;
	
	public EntityUpdateRequest(EntityUpdateMessage msg){
		this.msg = msg;
	}
	
	@Override
	public void doRequest(){
		Entity2D ent = Game.physics.get2DEntity(msg.layer, msg.hash);
		ent.setLocation(new Vector2(msg.x, msg.y));
		ent.setAngle(msg.angle);
	}
}
