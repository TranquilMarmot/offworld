package com.bitwaffle.guts.net;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;

/**
 * Client that communicates with servers
 * 
 * @author TranquilMarmot
 */
public class NetClient {
	/** Default timeout when connecting, in milliseconds */
	private static int DEFAULT_TIMEOUT = 5000;
	
	/** Actual client (this is basically a wrapper class) */
	private Client client;
	
	/** URL that this client is connected to */
	private String url;
	
	/** TCP and UDP ports client is using */
	private int tcpPort, udpPort;
	
	/**
	 * Create a new client (there can only be one per port!)
	 * @param url URL of server to connect to
	 * @param tcpPort TCP port for to connect with
	 * @param udpPort UDP port for to connect with
	 */
	public NetClient(String url, int tcpPort, int udpPort){
	    client = new Client();
	    this.url = url;
	    this.tcpPort = tcpPort;
	    this.udpPort = udpPort;
	}
	
	/** @return URL of server this client is connected to */
	public String getServerURL(){ return url; }
	
	/** @return Which TCP port is being used  */
	public int getTCPPort(){ return tcpPort; }
	
	/** @return Which UDP port is being used */
	public int getUDPPort(){ return udpPort; }
	
	/** @return ID of client */
	public int getID(){
		return client.getID();
	}
	
	/**
	 * Starts the client with the given settings
	 */
	public void start(){
		new Thread(){
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
				} catch (IOException e){
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	/** Closes the connection to the server */
	public void close(){
		client.close();
	}
	
	/** @return Whether or not this client is connected */
	public boolean isConnected(){
		return client.isConnected();
	}
	
	/**
	 * Attempts to reconnect to server
	 */
	public void reconnect(){
		try {
			client.reconnect();
		} catch (IOException e) {
			System.out.println("Error reconnecting client to " + url + " " + e);
		}
	}
	
	/**
	 * Attempts to reconnect to a server
	 * @param timeoutMillis How long to wait to reconnect, in milliseconds
	 */
	public void reconnect (int timeoutMillis){
		try {
			client.reconnect(timeoutMillis);
		} catch (IOException e) {
			System.out.println("Error reconnecting client to " + url + " " + e);
		}
	}
	
	/** @return Whether or not the connection is idle */
	public boolean isIdle(){ return client.isIdle(); }
	
	/**
	 * @param timeoutMillis How long to discover hosts for
	 * @return List of discovered hosts
	 */
	public java.util.List<java.net.InetAddress> discoverHosts(int timeoutMillis){
		return client.discoverHosts(udpPort, timeoutMillis);
	}
	
	/** @param listener Listener to add to client */
	public void addListener(Listener listener){ client.addListener(listener); }
	
	/** @param listener Listener to remove from client */
	public void removeListener(Listener listener){ client.removeListener(listener); }
	
	/** @param object Object to send to server over TCP */
	public void sendTCP(Object object){ client.sendTCP(object); }
	
	/** @param object Object to send to server over UDP */
	public void sendUDP(Object object){ client.sendUDP(object); }

	/** @return Address of server (from TCP) */
	public InetSocketAddress getRemoteAddressTCP() { return client.getRemoteAddressTCP(); }
	
	/** @return Address of server (from UDP) */
	public InetSocketAddress getRemoteAddressUDP() { return client.getRemoteAddressUDP(); }
}
