package com.bitwaffle.guts.net.client;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.Entity;
import com.bitwaffle.guts.entities.EntityRemoveRequest;
import com.bitwaffle.guts.net.messages.PlayerCreateMessage;
import com.bitwaffle.guts.net.messages.PlayerUpdateMessage;
import com.bitwaffle.guts.net.messages.PlayerUpdateRequest;
import com.bitwaffle.guts.net.messages.SomeReply;
import com.bitwaffle.guts.net.messages.entity.BreakableRockCreateRequest;
import com.bitwaffle.guts.net.messages.entity.BreakableRockCreator;
import com.bitwaffle.guts.net.messages.entity.DynamicEntityUpdate;
import com.bitwaffle.guts.net.messages.entity.DynamicEntityUpdateMessage;
import com.bitwaffle.guts.physics.PhysicsHelper;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

/**
 * Handles receiving/sending objects on a client.
 * 
 * @author TranquilMarmot
 */
public class ClientListener extends Listener {
	
	private GameClient client;
	
	public ClientListener(GameClient client){
		this.client = client;
	}
	
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
			Game.physics.addUpdateRequest(new BreakableRockCreator(req));
		} else if(object instanceof DynamicEntityUpdateMessage){
			DynamicEntityUpdateMessage req = (DynamicEntityUpdateMessage)object;
			Game.physics.addUpdateRequest(new DynamicEntityUpdate(req));
		} else if(object instanceof EntityRemoveRequest){
			EntityRemoveRequest req = (EntityRemoveRequest)object;
			Entity ent = Game.physics.getEntity(req.layer, req.hash);
			Game.physics.removeEntity(ent, req.hash, true); // FIXME dont know whether or not to remove from room here
		} else if(object instanceof PlayerCreateMessage){
			PlayerCreateMessage msg = (PlayerCreateMessage)object;
			PhysicsHelper.initPlayer(Game.physics, new Vector2(msg.x, msg.y), msg.playerNumber, msg.takeControl);
			if(msg.takeControl)
				client.setPlayerNumber(msg.playerNumber);
		} else if(object instanceof PlayerUpdateMessage){
			PlayerUpdateMessage msg = (PlayerUpdateMessage)object;
			Game.physics.addUpdateRequest(new PlayerUpdateRequest(msg));
		}
	}
	
	@Override
	public void idle(Connection connection){
		super.idle(connection);
	}
}
