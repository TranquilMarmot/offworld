package com.bitwaffle.guts.net.server;

import java.io.IOException;

import com.bitwaffle.guts.net.NetRegistrar;
import com.bitwaffle.guts.net.messages.PlayerInfoRequest;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

public class GameServer {
	private static final int DEFAULT_UDP_PORT = 42042, DEFAULT_TCP_PORT = 42042;
	
	private Server server;
	
	public GameServer(){
		//connections = new ArrayList<ServerConnection>();
		startServer();
	}
	
	public void startServer(){
		server = new Server();
		
		NetRegistrar.registerClasses(server.getKryo());
		
		server.addListener(new ServerListener());
		
		server.start();
		try {
			server.bind(DEFAULT_TCP_PORT, DEFAULT_UDP_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void endServer(){
		server.close();
		server = null;
	}
	
	public void update(){
		for(Connection connection : server.getConnections()){
			PlayerInfoRequest req = new PlayerInfoRequest();
			req.playerNumber = 0;
			connection.sendTCP(req);
		}
	}
}