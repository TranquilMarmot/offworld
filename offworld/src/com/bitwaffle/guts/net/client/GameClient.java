package com.bitwaffle.guts.net.client;

import java.io.IOException;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.net.NetRegistrar;
import com.bitwaffle.guts.net.messages.SomeRequest;
import com.esotericsoftware.kryonet.Client;

public class GameClient {
	private static final int DEFAULT_UDP_PORT = 42042, DEFAULT_TCP_PORT = 42042;
	
	private Client client;
	
	public GameClient(String host){
		client = new Client();
		
		NetRegistrar.registerClasses(client.getKryo());
		
		client.addListener(new ClientListener());
		
		client.start();
		try {
			client.connect(5000, host, DEFAULT_TCP_PORT, DEFAULT_UDP_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void send(String message){
		Game.out.println("Sending " + message);
		SomeRequest req = new SomeRequest();
		req.wat = message;
		client.sendTCP(req);
	}
	
	public void update(){
		//if(socket.isConnected()){
			//sendPlayerData(0);
			
		//h}
	}
}
