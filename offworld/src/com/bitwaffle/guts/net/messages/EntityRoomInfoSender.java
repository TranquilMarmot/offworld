package com.bitwaffle.guts.net.messages;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entity.Entity;
import com.bitwaffle.guts.net.messages.entity.BreakableRockCreateMessage;
import com.bitwaffle.guts.physics.PhysicsUpdateRequest;
import com.bitwaffle.guts.physics.Entities.EntityHashMap;
import com.bitwaffle.offworld.entities.dynamic.BreakableRock;
import com.esotericsoftware.kryonet.Connection;

public class EntityRoomInfoSender implements PhysicsUpdateRequest {
	private Connection connection;
	
	public EntityRoomInfoSender(Connection connection){
		this.connection = connection;
	}

	@Override
	public void doRequest() {
		for(int l = 0; l < Game.physics.numLayers(); l++){
			EntityHashMap layer = Game.physics.getLayer(l);
			
			for(Entity ent : layer.values()){
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
	}

}
