package com.bitwaffle.offworld.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.Renderer;
import com.bitwaffle.guts.util.MathHelper;
import com.bitwaffle.offworld.OffworldGame;
import com.bitwaffle.offworld.entities.player.Player;

/**
 * Automatically renders split-screen for all players
 * 
 * @author TranquilMarmot
 */
public class OffworldRenderer extends Renderer {
	
	/*
	 * TODO
	 * - Make it so that it only renders local players- remote player need not be rendered
	 * - Maybe even have a seperate class for a remote player? Need less data, after all
	 * - Make this work better... no need to multiply so many numbers every frame!
	 * - Maybe just render each player and use which split screen section they control
	 *   as a way to know how to set up the camera
	 */
	
	@Override
	public void renderScene(){
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		int oldWindowWidth = Game.windowWidth, oldWindowHeight = Game.windowHeight;
		for(int i = 0; i < OffworldGame.players.length; i++){
			Player player = OffworldGame.players[i];
			if(player != null){
				int xOffset = 0, yOffset = 0;
				switch(player.controlInfo.screenSection){
				case FULL:
					break;
				case TOP_HALF:
					Game.windowHeight /= 2;
					yOffset = Game.windowHeight;
					break;
				case BOTTOM_HALF:
					Game.windowHeight /= 2;
					break;
				case TOP_LEFT_QUARTER:
					Game.windowHeight /= 2;
					Game.windowWidth /= 2;
					yOffset = Game.windowHeight;
					break;
				case BOTTOM_LEFT_QUARTER:
					Game.windowHeight /= 2;
					Game.windowWidth /= 2;
					break;
				case BOTTOM_RIGHT_QUARTER:
					Game.windowHeight /= 2;
					Game.windowWidth /= 2;
					xOffset = Game.windowWidth;
					break;
				case TOP_RIGHT_QUARTER:
					Game.windowHeight /= 2;
					Game.windowWidth /= 2;
					xOffset = Game.windowWidth;
					yOffset = Game.windowHeight;
					break;
				}
				
				Game.aspect = (float) Game.windowWidth / (float) Game.windowHeight;
				Gdx.gl.glViewport(xOffset, yOffset, Game.windowWidth, Game.windowHeight);
				MathHelper.orthoM(projection, 0, Game.aspect, 0, 1, -1, 1000);
				this.camera = player.getCamera();
				super.renderEntities();
				
				Game.windowWidth = oldWindowWidth;
				Game.windowHeight = oldWindowHeight;
			}
		}
		
		Gdx.gl.glViewport(0, 0, Game.windowWidth, Game.windowHeight);
		super.renderGUI();
	}	
}
