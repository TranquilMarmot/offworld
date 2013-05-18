package com.bitwaffle.guts.net.server;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entity.Entity;
import com.bitwaffle.guts.physics.Entities.EntityHashMap;
import com.esotericsoftware.kryonet.Connection;

public abstract class ServerConnection {
	protected Connection connection;
	
	private int updateFrequency = 2;
	
	private int ticksSinceLastUpdate = 0;
	
	public ServerConnection(Connection connection){
		this.connection = connection;
	}
	
	public void setConnection(Connection connection){ this.connection = connection; }
	
	public Connection connection(){ return connection; }
	
	public void update(float timeStep){
		ticksSinceLastUpdate++;
		if(ticksSinceLastUpdate > updateFrequency){
			for(int l = 0; l < Game.physics.numLayers(); l++){
				EntityHashMap layer = Game.physics.getLayer(l);
				
				for(Entity ent : layer.values()){
					sendEntity(ent);
				}
			}
		}
	}
	
	protected abstract void sendEntity(Entity ent);
}
