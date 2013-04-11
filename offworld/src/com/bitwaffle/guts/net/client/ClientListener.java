package com.bitwaffle.guts.net.client;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.net.messages.SomeReply;
import com.bitwaffle.guts.net.messages.entity.BreakableRockCreateRequest;
import com.bitwaffle.guts.net.messages.entity.BreakableRockCreator;
import com.bitwaffle.guts.net.messages.entity.DynamicEntityUpdate;
import com.bitwaffle.guts.net.messages.entity.DynamicEntityUpdateMessage;
import com.bitwaffle.guts.net.messages.entity.EntityUpdateMessage;
import com.bitwaffle.guts.physics.EntityUpdateRequest;
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
		
	}
	
	@Override
	public void disconnected(Connection connection){
		
	}

	@Override
	public void received(Connection connection, Object object) {
		if (object instanceof SomeReply) {
			SomeReply response = (SomeReply) object;
			Game.out.println(response.wat);
		} else if(object instanceof BreakableRockCreateRequest){
			BreakableRockCreateRequest req = (BreakableRockCreateRequest)object;
			Game.physics.addEntityUpdateRequest(new BreakableRockCreator(req));
		} else if(object instanceof DynamicEntityUpdateMessage){
			DynamicEntityUpdateMessage req = (DynamicEntityUpdateMessage)object;
			Game.physics.addEntityUpdateRequest(new DynamicEntityUpdate(req));
		}
	}
	
	@Override
	public void idle(Connection connection){
		
	}
}
