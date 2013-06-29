package com.bitwaffle.offworld.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.Renderer;
import com.bitwaffle.offworld.OffworldGame;
import com.bitwaffle.offworld.entities.player.Player;

public class OffworldRenderer extends Renderer {
	
	@Override
	public void renderScene(){
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		int numPlayers = 0;
		for(int i = 0; i < OffworldGame.players.length; i++)
			if(OffworldGame.players[i] != null)
				numPlayers++;
		if(numPlayers == 2){
			Player player1 = null, player2 = null;
			for(int i = 0; i < OffworldGame.players.length; i++){
				if(OffworldGame.players[i] != null){
					if(player1 == null)
						player1 = OffworldGame.players[i];
					else if(player2 == null)
						player2 = OffworldGame.players[i];
					else
						break;
				}
			}
			render2Player(player1, player2);
		} else if(numPlayers == 3){
			
		} else if(numPlayers == 4){
			
		} else {
			super.renderScene();
		}
		
		/*
		if(OffworldGame.players[0] != null && OffworldGame.players[0].getCamera() != null)
			this.camera = OffworldGame.players[0].getCamera();
		// TODO do glViewPort for each player and render the scene with that player's camera
		super.renderScene();
		*/
	}
	
	private void render2Player(Player player1, Player player2){
		Game.windowHeight /= 2.0f;
		Game.aspect = (float) Game.windowWidth / (float) Game.windowHeight;
		
		Game.out.println("p1 " + player1.getLocation());
		
		Gdx.gl.glViewport(0, Game.windowHeight, Game.windowWidth, Game.windowHeight);
		this.camera = player1.getCamera();
		Game.out.println(camera.getLocation());
		super.renderEntities();
		
		Gdx.gl.glViewport(0, 0, Game.windowWidth, Game.windowHeight);
		this.camera = player2.getCamera();
		super.renderEntities();
		
		
		Game.windowHeight *= 2.0f;
	}
}
