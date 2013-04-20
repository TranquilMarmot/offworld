package com.bitwaffle.offworld.entities.player.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.render.Renderer;
import com.bitwaffle.guts.gui.hud.Bar;
import com.bitwaffle.offworld.entities.player.Jetpack;

/**
 * Bar that hovers above a player's head and represents how much jetpack is left
 * 
 * @author TranquilMarmot
 */
public class JetpackBar extends Bar {
	/** Default columns/rows for bar */
	private static int COLUMNS = 8, ROWS = 2;
	/** Default column width/row height for bar */
	private static float COLUMN_WIDTH = 8.0f, ROW_HEIGHT = 4.0f;
	
	/** Variables for colors */
	private float[]
			backgroundColor = { 0.3f, 0.3f, 0.3f, 0.5f },
			fillColor = { 0.0f, 0.0f, 1.0f, 0.8f },
			hoverFillColor = { 1.0f, 1.0f, 0.0f, 0.8f };
	
	/** Variables for fill fading */
	private float
			maxFillAlpha = 0.8f,
			fillFadeInSpeed = 0.1f,
			fillFadeOutSpeed = 0.01f;
	
	/** Variables for background fading */
	private float
			maxBackgroundAlpha = 0.5f,
			backgroundFadeInSpeed = 0.1f,
			backgroundFadeOutSpeed = 0.05f;
	
	/** Location and size of bar */
	private float yOffset = 2.35f, scale = 0.0005f;

	public JetpackBar() {
		super(0.0f, 0.0f, COLUMNS, ROWS, COLUMN_WIDTH, ROW_HEIGHT,
				new float[]{0.0f, 0.0f, 0.0f, 0.0f},
				new float[]{0.0f, 0.0f, 0.0f, 0.0f});
		this.setPercent(100.0f);
	}
	
	public void render(Renderer renderer, Jetpack jetpack){
		// only fade out when fuel is full
		if(jetpack.remainingFuel() == 100.0f){
			// fade out fill
			if(fillColor[3] > 0)
				fillColor[3] -= fillFadeOutSpeed;
			// fade out background
			if(backgroundColor[3] > 0)
				backgroundColor[3] -= backgroundFadeOutSpeed;
		}else{
			// fade in fill
			if(fillColor[3] < maxFillAlpha)
				fillColor[3] += fillFadeInSpeed;
			// fade in background
			if(backgroundColor[3] < maxBackgroundAlpha)
				backgroundColor[3] += backgroundFadeInSpeed;
		}
		
		// only render bar is visible
		if(fillColor[3] != 0.0f){
			super.setPercent(jetpack.remainingFuel());
			
			// set fill color based on fuel amount
			if(jetpack.remainingFuel() < jetpack.hoverStartPercent())
				super.setFillColor(hoverFillColor[0], hoverFillColor[1], hoverFillColor[2], hoverFillColor[3]);
			else
				super.setFillColor(fillColor[0], fillColor[1], fillColor[2], fillColor[3]);
			
			super.setBackgroundColor(backgroundColor[0], backgroundColor[1], backgroundColor[2], backgroundColor[3]);
			
			Gdx.gl20.glEnable(GL20.GL_BLEND);
			
			renderer.modelview.translate(0.0f, yOffset, 0.0f);
			renderer.modelview.scale(scale / Game.renderer.r2D.camera.getZoom(), scale / Game.renderer.r2D.camera.getZoom(), 1.0f);
			renderer.r2D.sendMatrixToShader();
			
			super.render(renderer, false, false);
			
			Gdx.gl20.glDisable(GL20.GL_BLEND);	
		}
	}
}
