package com.bitwaffle.offworld.net.server;

import com.bitwaffle.guts.entity.Entity;
import com.bitwaffle.guts.net.messages.entity.DynamicEntityUpdateMessage;
import com.bitwaffle.guts.net.server.ServerConnection;
import com.bitwaffle.offworld.entities.BreakableRock;
import com.esotericsoftware.kryonet.Connection;

public class OffworldServerConnection extends ServerConnection {
	
	private int playerNumber;

	public OffworldServerConnection(Connection connection) {
		super(connection);
	}
	
	public void setPlayerNumber(int playerNumber){ this.playerNumber = playerNumber; }
	
	public int playerNumber(){ return playerNumber; }

	@Override
	protected void sendEntity(Entity ent){
		// TODO send other entities, too- make method in entity class?!
		if(ent instanceof BreakableRock){
			BreakableRock rock = (BreakableRock) ent;
			if(rock.body != null){
				DynamicEntityUpdateMessage msg = new DynamicEntityUpdateMessage();
				msg.angle = rock.body.getAngle();
				msg.da = rock.body.getAngularVelocity();
				msg.x = rock.body.getPosition().x;
				msg.y = rock.body.getPosition().y;
				msg.dx = rock.body.getLinearVelocity().x;
				msg.dy = rock.body.getLinearVelocity().y;
				msg.hash = rock.hashCode();
				msg.layer = rock.getLayer();
				connection.sendUDP(msg);
			}
		}
	}
}
