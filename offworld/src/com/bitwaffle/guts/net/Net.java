package com.bitwaffle.guts.net;

import com.bitwaffle.guts.net.client.GameClient;
import com.bitwaffle.guts.net.server.GameServer;

public class Net {
	public GameServer server;
	
	public GameClient client;
	
	public void startServer(){
		if(server == null)
			server = new GameServer();
	}
	
	public void startClient(String host){
		if(client == null)
			client = new GameClient(host);
	}
	
	public void update(float timeStep){
		if(server != null)
			server.update(timeStep);
		if(client != null)
			client.update(timeStep);
	}
}
