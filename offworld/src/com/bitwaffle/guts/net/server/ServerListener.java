package com.bitwaffle.guts.net.server;

import java.util.Iterator;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.Entity;
import com.bitwaffle.guts.net.messages.PlayerCreateMessage;
import com.bitwaffle.guts.net.messages.PlayerUpdateMessage;
import com.bitwaffle.guts.net.messages.PlayerUpdateRequest;
import com.bitwaffle.guts.net.messages.SomeReply;
import com.bitwaffle.guts.net.messages.SomeRequest;
import com.bitwaffle.guts.net.messages.entity.BreakableRockCreateRequest;
import com.bitwaffle.guts.physics.PhysicsHelper;
import com.bitwaffle.offworld.entities.dynamic.BreakableRock;
import com.bitwaffle.offworld.entities.player.Player;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

/**
 * Handles sending/receiving objects from the server
 * 
 * @author TranquilMarmot
 */
public class ServerListener extends Listener {
	
	private GameServer server;
	
	public ServerListener(GameServer server){
		this.server = server;
	}

	@Override
	public void connected(Connection connection) {
		super.connected(connection);
		
		ServerConnection servcon = new ServerConnection(connection);
		playerJoined(connection);
		
		server.connections.put(connection, servcon);
		
		sendRoomEntityInfo(connection);
	}
	
	private void playerJoined(Connection connection){
		// figure out which player this is
		int playerNum = -1;
		for(int i = 0; i < Game.players.length; i++){
			// assign to first null slot
			if(Game.players[i] == null){
				playerNum = i;
				// create the player on the server
				PhysicsHelper.initPlayer(Game.physics, new Vector2(1.0f, 16.0f), playerNum, false);

				// create the player on the client
				PlayerCreateMessage msg = new PlayerCreateMessage();
				msg.playerNumber = playerNum;
				msg.x = 1.0f;
				msg.y = 16.0f;
				msg.takeControl = true; // tells the client to take control of the player
				connection.sendTCP(msg);
				
				// create player on previously connected clients
				for(ServerConnection con : server.connections.values()){
					msg.takeControl = false;
					con.connection().sendTCP(msg);
					
					if(con.playerNumber() >= 0){
						msg.playerNumber = con.playerNumber();
						Player p = Game.players[con.playerNumber()];
						msg.x = p.body.getPosition().x;
						msg.y = p.body.getPosition().y;
						connection.sendTCP(msg);
					}
				}
				break;
				
			}
		}
	}
	
	private void sendRoomEntityInfo(Connection connection){
		for(Iterator<Entity> it : Game.physics.getAllIterators()){
			while(it.hasNext()){
				Entity ent = it.next();
				if(ent instanceof BreakableRock){
					BreakableRock rock = (BreakableRock)ent;
					BreakableRockCreateRequest req = new BreakableRockCreateRequest();
					req.layer = rock.getLayer();
					req.hash = rock.hashCode();
					req.name = rock.chosenName;
					req.x = rock.body.getPosition().x;
					req.y = rock.body.getPosition().y;
					req.scale = rock.getScale();
					float[] color = rock.getColor();
					req.r = color[0];
					req.g = color[1];
					req.b = color[2];
					req.a = color[3];
					
					connection.sendTCP(req);
				}
			}
		}
	}

	@Override
	public void disconnected(Connection connection) {
		super.disconnected(connection);
		
		server.connections.remove(connection);
	}

	@Override
	public void received(Connection connection, Object object) {
		super.received(connection, object);
		if (object instanceof SomeRequest) {
			SomeRequest request = (SomeRequest) object;
			Game.out.println(request.wat);

			SomeReply response = new SomeReply();
			response.wat = "Thanks!";
			connection.sendTCP(response);
		} else if (object instanceof PlayerUpdateMessage) {
			PlayerUpdateMessage reply = (PlayerUpdateMessage) object;
			Game.physics.addEntityUpdateRequest(new PlayerUpdateRequest(reply));
			for(ServerConnection con : server.connections.values()){
				if(con.connection() != connection)
					con.connection().sendUDP(reply);
			}
		}
	}

	@Override
	public void idle(Connection connection) {
		super.idle(connection);
	}
}
