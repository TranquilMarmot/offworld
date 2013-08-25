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
		
		int oldRenderWidth = Game.renderWidth, oldRenderHeight = Game.renderHeight;
		
		for(int i = 0; i < OffworldGame.players.length; i++){
			Player player = OffworldGame.players[i];
			if(player != null){
				int xOffset = 0, yOffset = 0;
				switch(player.controlInfo.screenSection){
				case FULL:
					break;
				case TOP_HALF:
					Game.renderHeight = Game.windowHeight / 2;
					yOffset = Game.renderHeight;
					break;
				case BOTTOM_HALF:
					Game.renderHeight = Game.windowHeight / 2;
					break;
				case TOP_LEFT_QUARTER:
					Game.renderHeight = Game.windowHeight / 2;
					Game.renderWidth = Game.windowWidth / 2;
					yOffset = Game.renderHeight;
					break;
				case BOTTOM_LEFT_QUARTER:
					Game.renderHeight = Game.windowHeight / 2;
					Game.renderWidth = Game.windowWidth / 2;
					break;
				case BOTTOM_RIGHT_QUARTER:
					Game.renderHeight = Game.windowHeight / 2;
					Game.renderWidth = Game.windowWidth / 2;
					xOffset = Game.renderWidth;
					break;
				case TOP_RIGHT_QUARTER:
					Game.renderHeight = Game.windowHeight / 2;
					Game.renderWidth = Game.windowWidth / 2;
					xOffset = Game.renderWidth;
					yOffset = Game.renderHeight;
					break;
				}
				
				Game.renderAspect = (float) Game.renderWidth / (float) Game.renderHeight;
				Gdx.gl.glViewport(xOffset, yOffset, Game.renderWidth, Game.renderHeight);
				MathHelper.orthoM(projection, 0, Game.renderAspect, 0, 1, -1, 1000);
				this.camera = player.getCamera();
				super.renderEntities();
			}
		}
		
		Game.renderWidth = oldRenderWidth;
		Game.renderHeight = oldRenderHeight;
		
		Gdx.gl.glViewport(0, 0, Game.windowWidth, Game.windowHeight);
		super.renderGUI();
	}	
}
