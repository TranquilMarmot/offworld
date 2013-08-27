package com.bitwaffle.offworld.entities.player.render;

import com.badlogic.gdx.graphics.Color;
import com.bitwaffle.offworld.entities.player.Jetpack;
import com.bitwaffle.offworld.gui.elements.bar.Bar;
import com.bitwaffle.offworld.gui.elements.bar.BarRenderer;

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
	private Color
			backgroundColor = new Color( 0.3f, 0.3f, 0.3f, 0.5f ),
			fillColor = new Color( 0.0f, 0.0f, 1.0f, 0.8f ),
			hoverFillColor = new Color( 1.0f, 1.0f, 0.0f, 0.8f );
	
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
	public float yOffset = 2.35f, scale = 0.025f;
	
	public Jetpack jetpack;

	public JetpackBar() {
		super(0.0f, 0.0f, COLUMNS, ROWS, COLUMN_WIDTH, ROW_HEIGHT,
				new Color(0.0f, 0.0f, 0.0f, 0.0f),
				new Color(0.0f, 0.0f, 0.0f, 0.0f));
		this.setPercent(100.0f);
	}
	
	@Override
	public void update(float timeStep){
		// only fade out when fuel is full
		if(jetpack.remainingFuel() == 100.0f){
			// fade out fill
			if(fillColor.a > 0)
				fillColor.a -= fillFadeOutSpeed;
			// fade out background
			if(backgroundColor.a > 0)
				backgroundColor.a -= backgroundFadeOutSpeed;
		}else{
			// fade in fill
			if(fillColor.a < maxFillAlpha)
				fillColor.a += fillFadeInSpeed;
			// fade in background
			if(backgroundColor.a  < maxBackgroundAlpha)
				backgroundColor.a += backgroundFadeInSpeed;
		}
		
		// only render bar is visible
		if(fillColor.a != 0.0f){
			super.setPercent(jetpack.remainingFuel());
			
			// set fill color based on fuel amount
			if(jetpack.remainingFuel() < jetpack.hoverStartPercent())
				((BarRenderer)this.renderer).setFillColor(hoverFillColor);
			else
				((BarRenderer)this.renderer).setFillColor(fillColor);
			
			((BarRenderer)this.renderer).setBackgroundColor(backgroundColor);
			
			/*
			Gdx.gl20.glEnable(GL20.GL_BLEND);
			
			renderer.modelview.translate(0.0f, yOffset, 0.0f);
			renderer.modelview.scale(scale / Game.renderer.camera.zoom, scale / Game.renderer.camera.zoom, 1.0f);
			renderer.r2D.sendMatrixToShader();
			
			Gdx.gl20.glDisable(GL20.GL_BLEND);	
			*/
		}
	}
}
