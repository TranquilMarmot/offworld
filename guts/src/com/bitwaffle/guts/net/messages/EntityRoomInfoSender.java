package com.bitwaffle.guts.net.messages;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entity.Entity;
import com.bitwaffle.guts.physics.Entities.EntityHashMap;
import com.bitwaffle.guts.physics.PhysicsUpdateRequest;
import com.esotericsoftware.kryonet.Connection;

public abstract class EntityRoomInfoSender implements PhysicsUpdateRequest {
	protected Connection connection;
	
	public EntityRoomInfoSender(Connection connection){
		this.connection = connection;
	}

	@Override
	public void doRequest() {
		for(int l = 0; l < Game.physics.numLayers(); l++){
			EntityHashMap layer = Game.physics.getLayer(l);
			
			for(Entity ent : layer.values())
				sendEntity(ent);
		}
	}
	
	protected abstract void sendEntity(Entity ent);

}
