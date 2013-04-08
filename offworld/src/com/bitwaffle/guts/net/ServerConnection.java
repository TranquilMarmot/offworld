package com.bitwaffle.guts.net;

import com.badlogic.gdx.net.Socket;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class ServerConnection {
	public Socket socket;
	
	public Output output;
	
	public Input input;
	
	public ServerConnection(Socket socket){
		this.socket = socket;
		output = new Output();
		output.setOutputStream(socket.getOutputStream());
		input = new Input();
		input.setInputStream(socket.getInputStream());
	}
	
	
}
