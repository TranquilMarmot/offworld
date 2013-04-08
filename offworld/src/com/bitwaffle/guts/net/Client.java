package com.bitwaffle.guts.net;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.bitwaffle.guts.Game;
import com.bitwaffle.offworld.entities.player.Player;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class Client {
	private static final int DEFAULT_PORT = 42042;
	private static final com.badlogic.gdx.Net.Protocol PROTOCOL = com.badlogic.gdx.Net.Protocol.TCP;
	
	public Socket socket;
	
	public Input input;
	
	public Output output;
	
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
		input = new Input(socket.getInputStream());
		output = new Output(socket.getOutputStream());
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
		Player player = Game.players[playerNumber];
		output.writeInt(playerNumber);
		//output.writeBoolean(player.isMovingLeft());
		//output.writeBoolean(player.isMovingRight());
		output.writeFloat(player.getCurrentTarget().x);
		output.writeFloat(player.getCurrentTarget().y);
		output.writeBoolean(player.jetpack.isEnabled());
		output.writeFloat(player.body.getPosition().x);
		output.writeFloat(player.body.getPosition().y);
		output.flush();
	}
	
	public void update(){
		if(socket.isConnected()){
			sendPlayerData(0);
			
		}
	}
}
