package com.bitwaffle.guts.net;

import com.bitwaffle.guts.net.client.GameClient;
import com.bitwaffle.guts.net.server.GameServer;

public abstract class Net {
	public GameServer server;
	
	public GameClient client;
	
	public abstract void startServer();
	
	public abstract void startClient(String host);
	
	public void update(float timeStep){
		if(server != null)
			server.update(timeStep);
		if(client != null)
			client.update(timeStep);
	}
}
