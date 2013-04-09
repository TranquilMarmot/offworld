package com.bitwaffle.guts.net.server;

import java.io.IOException;

import com.bitwaffle.guts.net.NetRegistrar;
import com.esotericsoftware.kryonet.Server;

public class GameServer {
	/** Ports to use */
	private static final int 
			DEFAULT_UDP_PORT = 42042,
			DEFAULT_TCP_PORT = 42024;
	
	/** The actual server */
	private Server server;
	
	public GameServer(){
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
		
	}
}