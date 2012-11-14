package com.bitwaffle.guts.net;

import java.io.IOException;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class OffworldServer{
	Server server;
	int tcpPort, udpPort;
	
	public OffworldServer(int tcpPort, int udpPort){
		server = new Server();
		this.tcpPort = tcpPort;
		this.udpPort = udpPort;
		
	}
	
	public void start(){
		Thread t = new Thread(){
			@Override
			public void run(){
				try{
					server.start();
			    	server.bind(tcpPort, udpPort);
		
			    	server.addListener(new Listener() {
		    		   public void received (Connection connection, Object object) {
		    			   if(!(object instanceof FrameworkMessage.KeepAlive)){
		    				   System.out.println("Server recieved: " + object);
		    			   }	
		    		   }
			    	});
		    	
		    	
			    	//InetAddress address = client.discoverHost(42024, 42042);
			    	//System.out.println(address);
		    	} catch (IOException e){
		    		e.printStackTrace();
		    	}
			}
		};
	
		t.run();
	}
}
