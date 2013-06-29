package com.bitwaffle.offworld.physics;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.mappings.Ouya;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.physics.Physics;
import com.bitwaffle.offworld.OffworldGame;
import com.bitwaffle.offworld.entities.player.Player;
import com.bitwaffle.offworld.entities.player.input.PlayerInputListener;
import com.bitwaffle.offworld.entities.player.input.controller.OuyaPlayerControllerListener;
import com.bitwaffle.offworld.entities.player.input.controller.XboxPlayerControllerListener;

public class OffworldPhysics extends Physics {

	@Override
	public void clearEntities(){
		super.clearEntities();

		// get rid of the players
		for(int i = 0; i < OffworldGame.players.length; i++)
			OffworldGame.players[i] = null;
	}

	@Override
	protected ContactListener getContactListener() {
		return new OffworldContactListener();
	}

	@Override
	protected ContactFilter getContactFilter() {
		return new OffworldContactFilter();
	}

	/**
	 * Initializes the player
	 * @param physics
	 */
	public static void initPlayer(Physics physics, Vector2 position, int playerNumber, boolean takeControl){
		OffworldGame.players[playerNumber] = new Player(6, position);
		physics.addEntity(OffworldGame.players[playerNumber], false);
	
		// TODO have each player press start
		
		if(takeControl){	
			//CameraModes.follow.follow(OffworldGame.players[playerNumber]);
			//Game.renderer.camera.setMode(CameraModes.follow);
			Game.renderer.camera.setLocation(OffworldGame.players[playerNumber].getLocation());
			
			for(Controller con : Controllers.getControllers()){
				if(con.getName().equals(Ouya.ID)){
					con.addListener(new OuyaPlayerControllerListener(OffworldGame.players[playerNumber]));
				}else if(con.getName().contains("XBOX 360")){
					con.addListener(new XboxPlayerControllerListener(OffworldGame.players[playerNumber]));
				}
			}
			
			// add player control listener
			Game.input.multiplexer.addProcessor(new PlayerInputListener(OffworldGame.players[playerNumber]));
		}
	}
}
