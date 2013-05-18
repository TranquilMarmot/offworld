package com.bitwaffle.offworld.net;

import com.bitwaffle.guts.net.Net;
import com.bitwaffle.offworld.net.client.OffworldGameClient;
import com.bitwaffle.offworld.net.server.OffworldGameServer;

public class OffworldNet extends Net {
	
	public OffworldNet(){
		super();
	}

	@Override
	public void startServer(){
		if(server == null)
			server = new OffworldGameServer();
	}
	
	@Override
	public void startClient(String host){
		if(client == null)
			client = new OffworldGameClient(host);
	}
}
