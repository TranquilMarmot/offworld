package com.bitwaffle.guts.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.bitwaffle.guts.Game;
import com.bitwaffle.offworld.entities.player.Player;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class Server {
	private static final int DEFAULT_PORT = 42042;
	private static final com.badlogic.gdx.Net.Protocol PROTOCOL = com.badlogic.gdx.Net.Protocol.TCP;
	
	private ServerSocket socket;
	
	private ArrayList<ServerConnection> connections;
	
	private ConcurrentLinkedQueue<Socket> newConnections;
	
	private volatile boolean acceptingConnections;
	
	private Kryo kryo;
	private Output output;
	private Input input;
	
	public Server(){
		connections = new ArrayList<ServerConnection>();
		newConnections = new ConcurrentLinkedQueue<Socket>();
		
		startServer();
		beginAcceptingConnections();
	}
	
	public void startServer(){
		ServerSocketHints hints = new ServerSocketHints();
		hints.acceptTimeout = 0; // infinite wait
		hints.backlog = 0;       // if 0, it uses system default
		hints.performancePrefLatency = 2; // prefer low latency over bandwidth
		hints.performancePrefBandwidth = 1;
		hints.performancePrefConnectionTime = 0;
		// hints.receiveBufferSize = 8; ???
		hints.reuseAddress = true; // ??
		
		socket = Gdx.net.newServerSocket(PROTOCOL, DEFAULT_PORT, hints);
	}
	
	public void beginAcceptingConnections(){
		acceptingConnections = true;
		new Thread(){
			@Override
			public void run(){
				while(acceptingConnections){
					SocketHints clientHints = new SocketHints();
					//hints.connectTimeout = 50000; ???
					clientHints.keepAlive = true;
					clientHints.linger = true;
					clientHints.lingerDuration = 10;
					clientHints.performancePrefLatency = 2;
					clientHints.performancePrefBandwidth = 1;
					clientHints.performancePrefConnectionTime = 0;
					//hints.receiveBufferSize = 8 ???
					//hints.sendBufferSize = 8; ???
					clientHints.tcpNoDelay = true;
					//hints.trafficClass ??
					Socket clientSocket = socket.accept(clientHints);
					newConnections.add(clientSocket);	
				}
			}
		}.start();
	}
	
	public void stopAcceptingConnections(){
		acceptingConnections = false;
	}
	
	public void endServer(){
		if(acceptingConnections)
			stopAcceptingConnections();
		socket.dispose();
	}
	
	public void update(){
		while(!newConnections.isEmpty()){
			Game.out.println("New connection!");
			connections.add(new ServerConnection(newConnections.poll()));
		}
		
		for(int i = 0; i < connections.size(); i++){
			ServerConnection con = connections.get(i);
			try {
				if(con.input.available() > 0){
					int playerNum = con.input.readInt();
					boolean left = con.input.readBoolean();
					boolean right = con.input.readBoolean();
					float aimX = con.input.readFloat();
					float aimY = con.input.readFloat();
					boolean jetpack = con.input.readBoolean();
					float x = con.input.readFloat();
					float y = con.input.readFloat();
					Player p = Game.players[playerNum];
					
					if(left)
						p.moveLeft();
					else
						p.stopMovingLeft();
					
					if(right)
						p.moveRight();
					else
						p.stopMovingRight();
					
					p.setTarget(new Vector2(aimX, aimY));
					
					if(jetpack)
						p.jetpack.enable();
					else
						p.jetpack.disable();
					
					p.body.setTransform(new Vector2(x, y), 0.0f);
				}
			} catch (KryoException | IOException e) {
				e.printStackTrace();
			}
		}
		
		/*
		for(int i = 0; i < connections.size(); i++){
			String read = readString(connections.get(i));
			if(!read.equals("")){
				try {
					JSONObject obj = new JSONObject(read);
					JSONObject playerInfo = obj.getJSONObject("player");
					int playerNum = playerInfo.getInt("n");
					boolean left = playerInfo.getInt("l") == 1;
					boolean right = playerInfo.getInt("r") == 1;
					float aimX = (float)playerInfo.getDouble("aX");
					float aimY = (float)playerInfo.getDouble("aY");
					boolean jetpack = playerInfo.getInt("j") == 1;
					float x = (float)playerInfo.getDouble("x");
					float y = (float)playerInfo.getDouble("y");
					
					Player p = Game.players[playerNum];
					if(left)
						p.moveLeft();
					else
						p.stopMovingLeft();
					
					if(right)
						p.moveRight();
					else
						p.stopMovingRight();
					
					p.setTarget(new Vector2(aimX, aimY));
					
					if(jetpack)
						p.jetpack.enable();
					else
						p.jetpack.disable();
					
					p.body.setTransform(new Vector2(x, y), 0.0f);
				} catch (JSONException e) {
					//e.printStackTrace();
					Game.out.println("didn't get full json object");
				}	
			}
		}
		*/
	}
	
	private String readString(Socket con){
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String read = "";
		try{
			while(in.ready())
				read += (char)in.read();
		} catch(IOException e){
			e.printStackTrace();
		}
		return read;
	}
	
	
}