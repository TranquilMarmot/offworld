package com.bitwaffle.guts.net.client;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.net.messages.SomeReply;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

/**
 * Handles receiving/sending objects on a client.
 * 
 * @author TranquilMarmot
 */
public class ClientListener extends Listener {
	
	@Override
	public void connected(Connection connection){
		
	}
	
	@Override
	public void disconnected(Connection connection){
		
	}

	@Override
	public void received(Connection connection, Object object) {
		if (object instanceof SomeReply) {
			SomeReply response = (SomeReply) object;
			Game.out.println(response.wat);
		}
	}
	
	@Override
	public void idle(Connection connection){
		
	}
}
