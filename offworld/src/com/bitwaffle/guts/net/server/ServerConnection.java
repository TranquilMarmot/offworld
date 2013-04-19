package com.bitwaffle.guts.net.server;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.EntityLayer;
import com.bitwaffle.guts.entities.entities2d.Entity2D;
import com.bitwaffle.guts.net.messages.entity.DynamicEntityUpdateMessage;
import com.bitwaffle.offworld.entities.dynamic.BreakableRock;
import com.esotericsoftware.kryonet.Connection;

public class ServerConnection {
	private Connection connection;
	
	private int playerNumber;
	
	private int updateFrequency = 2;
	
	private int ticksSinceLastUpdate = 0;
	
	public ServerConnection(Connection connection){
		this.connection = connection;
	}
	
	public void setPlayerNumber(int playerNumber){ this.playerNumber = playerNumber; }
	
	public int playerNumber(){ return playerNumber; }
	
	public void setConnection(Connection connection){ this.connection = connection; }
	
	public Connection connection(){ return connection; }
	
	public void update(){
		ticksSinceLastUpdate++;
		if(ticksSinceLastUpdate > updateFrequency){
			for(int l = 0; l < Game.physics.numLayers(); l++){
				EntityLayer layer = Game.physics.getLayer(l);
				
				for(Entity2D ent : layer.entities2D.values()){
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
		}
	}
}
