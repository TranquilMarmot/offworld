package com.bitwaffle.guts.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;

public class Client {
	private static final int DEFAULT_PORT = 42042;
	private static final com.badlogic.gdx.Net.Protocol PROTOCOL = com.badlogic.gdx.Net.Protocol.TCP;
	
	public Socket socket;
	
	private PrintWriter out;
	private BufferedReader in;
	
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
		
		out = new PrintWriter(socket.getOutputStream());
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	
	public void send(String message){
		out.print(message);
		out.flush();
	}
}
