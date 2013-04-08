package com.bitwaffle.guts.net;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.bitwaffle.guts.Game;
import com.bitwaffle.offworld.entities.player.Player;

public class Client {
	private static final int DEFAULT_PORT = 42042;
	private static final com.badlogic.gdx.Net.Protocol PROTOCOL = com.badlogic.gdx.Net.Protocol.TCP;
	
	public Socket socket;
	
	public Client(String host){
		SocketHints hints = new SocketHints();
		//hints.connectTimeout = 50000; ???
		hints.keepAlive = true;
		hints.linger = true;
		hints.lingerDuration = 10;
		hints.performancePrefLatency = 2;
		hints.performancePrefBandwidth = 1;
		hints.performancePrefConnectionTime = 0;
		//hints.receiveBufferSize = 8 ???
		//hints.sendBufferSize = 8; ???
		hints.tcpNoDelay = true;
		//hints.trafficClass ??
		
		socket = Gdx.net.newClientSocket(PROTOCOL, host, DEFAULT_PORT, hints);
	}
	
	public void send(String message){
		try {
			socket.getOutputStream().write(message.getBytes());
			socket.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendPlayerData(int playerNumber){
		try {
			JSONObject playerInfo = new JSONObject();
			Player player = Game.players[playerNumber];
			playerInfo.put("n", playerNumber);
			playerInfo.put("l", player.isMovingLeft() ? 1 : 0);
			playerInfo.put("r", player.isMovingRight() ? 1 : 0);
			playerInfo.put("aX", (double)player.getCurrentTarget().x);
			playerInfo.put("aY", (double)player.getCurrentTarget().y);
			playerInfo.put("j", player.jetpack.isEnabled() ? 1 : 0);
			playerInfo.put("x", (double)player.body.getPosition().x);
			playerInfo.put("y", (double)player.body.getPosition().y);
			
			JSONObject test = new JSONObject();
			test.put("player", playerInfo);
			send(test.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void update(){
		if(socket.isConnected()){
			sendPlayerData(0);
			
		}
	}
}
