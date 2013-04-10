package com.bitwaffle.guts.net.server;

import com.esotericsoftware.kryonet.Connection;

public class ServerConnection {
	private Connection connection;
	
	private int playerNumber;
	
	public ServerConnection(Connection connection){
		this.connection = connection;
	}
	
	public void setPlayerNumber(int playerNumber){
		this.playerNumber = playerNumber;
	}
	
	public int playerNumber(){
		return playerNumber;
	}
	
	public void setConnection(Connection connection){
		this.connection = connection;
	}
	
	public Connection connection(){
		return connection;
	}
}
