package com.bitwaffle.guts.net.client;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.net.messages.PlayerInfoReply;
import com.bitwaffle.guts.net.messages.PlayerInfoRequest;
import com.bitwaffle.guts.net.messages.SomeReply;
import com.bitwaffle.offworld.entities.player.Player;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

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
		} else if(object instanceof PlayerInfoRequest){
			Game.out.println("got info request, sending reply");
			PlayerInfoRequest req = (PlayerInfoRequest) object;
			sendPlayerInfoReply(connection, req.playerNumber);
		}
	}
	
	public void sendPlayerInfoReply(Connection connection, int playerNumber){
		PlayerInfoReply reply = new PlayerInfoReply();
		reply.playerNumber = playerNumber;
		Player player = Game.players[reply.playerNumber];
		reply.x = player.body.getPosition().x;
		reply.y = player.body.getPosition().y;
		reply.dx = player.body.getLinearVelocity().x;
		reply.dy = player.body.getLinearVelocity().y;
		reply.aimX = player.getCurrentTarget().x;
		reply.aimY = player.getCurrentTarget().y;
		reply.jetpack = player.jetpack.isEnabled();
		reply.shooting = player.isShooting();
		
		connection.sendUDP(reply);
	}
	
	@Override
	public void idle(Connection connection){
		
	}
}
