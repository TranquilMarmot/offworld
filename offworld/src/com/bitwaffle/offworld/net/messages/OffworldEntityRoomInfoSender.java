package com.bitwaffle.offworld.net.messages;

import com.bitwaffle.guts.entity.Entity;
import com.bitwaffle.guts.net.messages.EntityRoomInfoSender;
import com.bitwaffle.offworld.entities.BreakableRock;
import com.bitwaffle.offworld.net.messages.entity.BreakableRockCreateMessage;
import com.esotericsoftware.kryonet.Connection;

public class OffworldEntityRoomInfoSender extends EntityRoomInfoSender {

	public OffworldEntityRoomInfoSender(Connection connection) {
		super(connection);
	}

	@Override
	protected void sendEntity(Entity ent) {
		if(ent instanceof BreakableRock){
			BreakableRock rock = (BreakableRock)ent;
			BreakableRockCreateMessage req = new BreakableRockCreateMessage();
			req.layer = rock.getLayer();
			req.hash = rock.hashCode();
			req.name = rock.chosenName;
			req.x = rock.body.getPosition().x;
			req.y = rock.body.getPosition().y;
			req.scale = rock.getScale();
			float[] color = rock.getColor();
			req.r = color[0];
			req.g = color[1];
			req.b = color[2];
			req.a = color[3];
			
			connection.sendTCP(req);
		}
	}

}
