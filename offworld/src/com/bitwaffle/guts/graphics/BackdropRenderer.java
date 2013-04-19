package com.bitwaffle.guts.graphics;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.entities2d.Entity2D;
import com.bitwaffle.guts.entities.entities2d.Entity2DRenderer;
import com.bitwaffle.guts.graphics.render.render2d.Render2D;
import com.bitwaffle.guts.util.MathHelper;

/**
 * Used for rendering an image that takes up the entire screen.
 * 
 * @author TranquilMarmot
 */
public class BackdropRenderer implements Entity2DRenderer{
	/** Size of window (saved so that world size is only calculated when it changes) */
	private float windowWidth = 0.0f, windowHeight = 0.0f;
	
	/** How big the world is */
	private Vector2 worldSize;
	
	/** Name of texture to bind */
	private String textureName;
	
	/** Color to render backdrop as */
	private float[] color;
	
	public BackdropRenderer(String textureName, float[] color){
		worldSize = new Vector2();
		this.textureName = textureName;
		this.color = color;
	}
	
	public void render(Render2D renderer, Entity2D ent, boolean renderDebug){
		if(windowWidth != Game.windowWidth || windowHeight != Game.windowHeight){
			windowWidth = Game.windowWidth;
			windowHeight = Game.windowHeight;
			MathHelper.toWorldSpace(worldSize, windowWidth / 2, windowHeight / 2);
		}
		
		renderer.program.setUniform("vColor", color[0], color[1], color[2], color[3]);
		
		renderer.modelview.idt();
		renderer.modelview.translate(worldSize.x, worldSize.y, 0.0f);
		renderer.sendMatrixToShader();
		
		Game.resources.textures.bindTexture(textureName);
		renderer.quad.render(worldSize.x, worldSize.y, true, true);
	}
}