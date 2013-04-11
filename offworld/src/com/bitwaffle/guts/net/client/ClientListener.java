package com.bitwaffle.guts.net.client;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.Entity;
import com.bitwaffle.guts.entities.EntityRemoveRequest;
import com.bitwaffle.guts.net.messages.SomeReply;
import com.bitwaffle.guts.net.messages.entity.BreakableRockCreateRequest;
import com.bitwaffle.guts.net.messages.entity.BreakableRockCreator;
import com.bitwaffle.guts.net.messages.entity.DynamicEntityUpdate;
import com.bitwaffle.guts.net.messages.entity.DynamicEntityUpdateMessage;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

/**
 * Handles receiving/sending objects on a client.
 * 
 * @author TranquilMarmot
 */
public class ClientListener extends Listener {
	
	@Override
	public void connected(Connection connection){
		super.connected(connection);
	}
	
	@Override
	public void disconnected(Connection connection){
		super.disconnected(connection);
	}

	@Override
	public void received(Connection connection, Object object) {
		super.received(connection, object);
		
		if (object instanceof SomeReply) {
			SomeReply response = (SomeReply) object;
			Game.out.println(response.wat);
		} else if(object instanceof BreakableRockCreateRequest){
			BreakableRockCreateRequest req = (BreakableRockCreateRequest)object;
			Game.physics.addEntityUpdateRequest(new BreakableRockCreator(req));
		} else if(object instanceof DynamicEntityUpdateMessage){
			DynamicEntityUpdateMessage req = (DynamicEntityUpdateMessage)object;
			Game.physics.addEntityUpdateRequest(new DynamicEntityUpdate(req));
		} else if(object instanceof EntityRemoveRequest){
			EntityRemoveRequest req = (EntityRemoveRequest)object;
			Entity ent = Game.physics.getEntity(req.layer, req.hash);
			Game.physics.removeEntity(ent, req.hash, true); // FIXME dont know whether or not to remove from room here
		}
	}
	
	@Override
	public void idle(Connection connection){
		super.idle(connection);
	}
}
