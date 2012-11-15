package com.bitwaffle.guts.net;

import java.io.IOException;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

/**
 * Server that communicates with clients
 * 
 * @author TranquilMarmot
 */
public class NetServer{
	/** Actual server (this is basically a wrapper class) */
	private Server server;
	
	/** TCP and UDP ports that this server it using */
	int tcpPort, udpPort;
	
	/**
	 * Create a new server (there can only be one per port!)
	 * @param tcpPort TCP port for server to use
	 * @param udpPort UDP port for server to use
	 */
	public NetServer(int tcpPort, int udpPort){
		server = new Server();
		this.tcpPort = tcpPort;
		this.udpPort = udpPort;
		
	}
	
	/**
	 * @return TCP port that server is using
	 */
	public int getTCPPort(){ return tcpPort; }
	
	/**
	 * @return UDP port that server is using
	 */
	public int getUDPPort(){ return udpPort; }
	
	/**
	 * Starts the server
	 */
	public void start(){
		new Thread(){
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
		    	} catch (IOException e){
		    		e.printStackTrace();
		    	}
			}
		}.start();
	}
	
	/** Closes the server */
	public void close(){ server.close(); }
	
	/** @return List of connections to this server */
	public Connection[] getConnections(){ return server.getConnections(); }
	
	/** @param object Object to broadcast across all TCP clients */
	public void broadcastTCP(Object object){ server.sendToAllTCP(object); }
	
	/** @param object Object to broadcast across all UDP clients */
	public void broadcastUDP(Object object){ server.sendToAllUDP(object); }
	
	/**
	 * Send an object to a specific client over UDP
	 * @param client ID of client to send object to
	 * @param object Object to send
	 */
	public void sendToUDP(int client, Object object){ server.sendToUDP(client, object); }
	
	/**
	 * Send an object to a specific client over TCP
	 * @param client ID of client to send object to
	 * @param object Object to send
	 */
	public void sendToTCP(int client, Object object){ server.sendToTCP(client, object); }
	
	/**
	 * Send an object to every client EXCEPT a specific one over UDP
	 * @param client ID of client to NOT send object to
	 * @param object Object to send
	 */
	public void sendToAllExceptUDP(int client, Object object){ server.sendToAllExceptUDP(client, object); }
	
	/**
	 * Send an object to every client EXCEPT a specific one over TCP
	 * @param client ID of client to NOT send object to
	 * @param object Object to send
	 */
	public void sendToAllExceptTCP(int client, Object object){ server.sendToAllExceptTCP(client, object); }
}
