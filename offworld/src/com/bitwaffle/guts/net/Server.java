package com.bitwaffle.guts.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.bitwaffle.guts.Game;

public class Server {
	private static final int DEFAULT_PORT = 42042;
	private static final com.badlogic.gdx.Net.Protocol PROTOCOL = com.badlogic.gdx.Net.Protocol.TCP;
	
	private ServerSocket socket;
	
	private BufferedReader in;
	
	public Server(){
		ServerSocketHints hints = new ServerSocketHints();
		hints.acceptTimeout = 0; // infinite wait
		hints.backlog = 0;       // if 0, it uses system default
		hints.performancePrefLatency = 2; // prefer low latency over bandwidth
		hints.performancePrefBandwidth = 1;
		hints.performancePrefConnectionTime = 0;
		// hints.receiveBufferSize = 8; ???
		hints.reuseAddress = true; // ??
		
		socket = Gdx.net.newServerSocket(PROTOCOL, DEFAULT_PORT, hints);
		
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
		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		
	}
	
	public void update(){
		String wat = "";
		try{
			while(in.ready()){
				char c = (char)in.read();
				wat += c;
			}
			if(!wat.equals(""))
				Game.out.println(wat);
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	
}
