package com.bitwaffle.guts.net.messages;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.Entity;
import com.bitwaffle.guts.physics.EntityUpdateRequest;

public class EntityUpdate implements EntityUpdateRequest {
	
	private EntityUpdateMessage msg;
	
	public EntityUpdate(EntityUpdateMessage msg){
		this.msg = msg;
	}
	
	@Override
	public void updateEntity(){
		Entity ent = Game.physics.getEntity(msg.layer, msg.hash);
		ent.setLocation(new Vector2(msg.x, msg.y));
		ent.setAngle(msg.angle);
	}
}
