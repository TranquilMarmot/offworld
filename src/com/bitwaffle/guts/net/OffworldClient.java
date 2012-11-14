package com.bitwaffle.guts.net;

import java.io.IOException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;

public class OffworldClient {
	private static int DEFAULT_TIMEOUT = 5000; 
	Client client;
	String url;
	int tcpPort, udpPort;
	
	public OffworldClient(String url, int tcpPort, int udpPort){
	    client = new Client();
	    this.url = url;
	    this.tcpPort = tcpPort;
	    this.udpPort = udpPort;
	}
	
	public void start(){
		Thread t = new Thread(){
			@Override
			public void run(){
				try{
					client.start();
			    	client.connect(DEFAULT_TIMEOUT, url, tcpPort, udpPort);
			
			    	client.addListener(new Listener() {
					   public void received (Connection connection, Object object) {
						   if(!(object instanceof FrameworkMessage.KeepAlive)){
							   System.out.println("Client recieved: " + object);
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
		t.start();
	}
}
