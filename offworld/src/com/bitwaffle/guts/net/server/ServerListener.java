package com.bitwaffle.guts.net.server;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.net.messages.PlayerInfoReply;
import com.bitwaffle.guts.net.messages.PlayerInfoRequest;
import com.bitwaffle.guts.net.messages.SomeReply;
import com.bitwaffle.guts.net.messages.SomeRequest;
import com.bitwaffle.offworld.entities.player.Player;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class ServerListener extends Listener {
	
	@Override
	public void connected(Connection connection){
		Game.out.println("got connection, sending info request");
  	  PlayerInfoRequest req = new PlayerInfoRequest();
	  req.playerNumber = 0;
	  connection.sendTCP(req);
	}
	
	@Override
	public void disconnected(Connection connection){
		
	}
	
	@Override
	public void received (Connection connection, Object object) {
		Game.out.println("server got " + object.getClass().getCanonicalName());
	      if (object instanceof SomeRequest) {
	         SomeRequest request = (SomeRequest)object;
	         Game.out.println(request.wat);

	         SomeReply response = new SomeReply();
	         response.wat = "Thanks!";
	         connection.sendTCP(response);
	      } else if(object instanceof PlayerInfoReply){
	    	  Game.out.println("got info reply");
	    	  PlayerInfoReply reply = (PlayerInfoReply)object;
	    	  Player player = Game.players[reply.playerNumber];
	    	  player.body.setTransform(new Vector2(reply.x, reply.y), 0);
	    	  player.setTarget(new Vector2(reply.aimX, reply.aimY));
	    	  
	    	  if(reply.jetpack)
	    		  player.jetpack.enable();
	    	  else
	    		  player.jetpack.disable();
	    	  
	    	  if(reply.shooting)
	    		  player.beginShooting();
	    	  else
	    		  player.endShooting();
	    	  
	    	  Game.out.println("set player values, sending request");
	    	  PlayerInfoRequest req = new PlayerInfoRequest();
	    	  req.playerNumber = reply.playerNumber;
	    	  connection.sendTCP(req);
	      }
	}
	
	@Override
	public void idle(Connection connection){
		
	}
}
