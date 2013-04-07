package com.bitwaffle.guts.net;

public class Net {
	public Server server;
	
	public Client client;
	
	public void startServer(){
		if(server == null)
			server = new Server();
	}
	
	public void startClient(String host){
		if(client == null)
			client = new Client(host);
	}
	
	public void update(){
		if(server != null)
			server.update();
	}
	
	public void send(String msg){
		if(client != null)
			client.send(msg);
	}
}
