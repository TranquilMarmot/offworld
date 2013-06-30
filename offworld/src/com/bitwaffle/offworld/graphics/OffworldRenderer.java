package com.bitwaffle.offworld.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.Renderer;
import com.bitwaffle.offworld.OffworldGame;
import com.bitwaffle.offworld.entities.player.Player;

public class OffworldRenderer extends Renderer {
	
	/*
	 * TODO
	 * - Make it so that it only renders local players- remote player need not be rendered
	 * - Maybe even have a seperate class for a remote player? Need less data, after all
	 */
	
	@Override
	public void renderScene(){
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		int numPlayers = 0;
		for(int i = 0; i < OffworldGame.players.length; i++)
			if(OffworldGame.players[i] != null)
				numPlayers++;
		
		if(numPlayers <= 1){
			if(OffworldGame.players[0] != null && OffworldGame.players[0].getCamera() != null)
				this.camera = OffworldGame.players[0].getCamera();
			super.renderScene();
		} else if(numPlayers == 2){
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
			Player player1 = null, player2 = null, player3 = null;
			for(int i = 0; i < OffworldGame.players.length; i++){
				if(OffworldGame.players[i] != null){
					if(player1 == null)
						player1 = OffworldGame.players[i];
					else if(player2 == null)
						player2 = OffworldGame.players[i];
					else if(player3 == null)
						player3 = OffworldGame.players[i];
					else
						break;
				}
			}
			render3Player(player1, player2, player3);
		} else if(numPlayers == 4){
			Player player1 = null, player2 = null, player3 = null, player4 = null;
			for(int i = 0; i < OffworldGame.players.length; i++){
				if(OffworldGame.players[i] != null){
					if(player1 == null)
						player1 = OffworldGame.players[i];
					else if(player2 == null)
						player2 = OffworldGame.players[i];
					else if(player3 == null)
						player3 = OffworldGame.players[i];
					else if(player4 == null)
						player4 = OffworldGame.players[i];
					else
						break;
				}
			}
			render4Player(player1, player2, player3, player4);
		}
		
		/*
		if(OffworldGame.players[0] != null && OffworldGame.players[0].getCamera() != null)
			this.camera = OffworldGame.players[0].getCamera();
		super.renderScene();
		*/
	}
	
	private void render2Player(Player player1, Player player2){
		Game.windowHeight /= 2.0f;
		Game.aspect = (float) Game.windowWidth / (float) Game.windowHeight;
		
		Gdx.gl.glViewport(0, Game.windowHeight, Game.windowWidth, Game.windowHeight);
		this.camera = player1.getCamera();
		super.renderEntities();
		
		Gdx.gl.glViewport(0, 0, Game.windowWidth, Game.windowHeight);
		this.camera = player2.getCamera();
		super.renderEntities();
		
		
		Game.windowHeight *= 2.0f;
		
		Gdx.gl.glViewport(0, 0, Game.windowWidth, Game.windowHeight);
		super.renderGUI();
	}
	
	private void render3Player(Player player1, Player player2, Player player3){
		Game.windowHeight /= 2.0f;
		Game.aspect = (float) Game.windowWidth / (float) Game.windowHeight;
		
		Gdx.gl.glViewport(0, Game.windowHeight, Game.windowWidth, Game.windowHeight);
		this.camera = player1.getCamera();
		this.camera.setZoom(this.camera.zoom);
		super.renderEntities();
		
		Game.windowWidth /= 2.0f;
		Game.aspect = (float) Game.windowWidth / (float) Game.windowHeight;
		
		Gdx.gl.glViewport(0, 0, Game.windowWidth, Game.windowHeight);
		this.camera = player2.getCamera();
		super.renderEntities();
		
		Gdx.gl.glViewport(Game.windowWidth, 0, Game.windowWidth, Game.windowHeight);
		this.camera = player3.getCamera();
		super.renderEntities();
		
		Game.windowWidth *= 2.0f;
		Game.windowHeight *= 2.0f;
		
		Gdx.gl.glViewport(0, 0, Game.windowWidth, Game.windowHeight);
		super.renderGUI();
	}
	
	private void render4Player(Player player1, Player player2, Player player3, Player player4){
		Game.windowHeight /= 2.0f;
		Game.windowWidth /= 2.0f;
		Game.aspect = (float) Game.windowWidth / (float) Game.windowHeight;
		
		Gdx.gl.glViewport(0, Game.windowHeight, Game.windowWidth, Game.windowHeight);
		this.camera = player1.getCamera();
		super.renderEntities();
		
		Gdx.gl.glViewport(Game.windowWidth, Game.windowHeight, Game.windowWidth, Game.windowHeight);
		this.camera = player2.getCamera();
		super.renderEntities();
		
		Gdx.gl.glViewport(0, 0, Game.windowWidth, Game.windowHeight);
		this.camera = player3.getCamera();
		super.renderEntities();
		
		Gdx.gl.glViewport(Game.windowWidth, 0, Game.windowWidth, Game.windowHeight);
		this.camera = player4.getCamera();
		super.renderEntities();
		
		Game.windowWidth *= 2.0f;
		Game.windowHeight *= 2.0f;
		
		Gdx.gl.glViewport(0, 0, Game.windowWidth, Game.windowHeight);
		super.renderGUI();
	}
}
