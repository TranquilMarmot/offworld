package com.bitwaffle.guts.graphics;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.entities2d.Entity;
import com.bitwaffle.guts.entities.entities2d.EntityRenderer;
import com.bitwaffle.guts.graphics.render.Renderer;
import com.bitwaffle.guts.util.MathHelper;

/**
 * Used for rendering an image that takes up the entire screen.
 * 
 * @author TranquilMarmot
 */
public class BackdropRenderer implements EntityRenderer{
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
	
	public void render(Renderer renderer, Entity ent, boolean renderDebug){
		if(windowWidth != Game.windowWidth || windowHeight != Game.windowHeight){
			windowWidth = Game.windowWidth;
			windowHeight = Game.windowHeight;
			MathHelper.toWorldSpace(worldSize, windowWidth / 2, windowHeight / 2);
		}
		
		renderer.render2D.program.setUniform("vColor", color[0], color[1], color[2], color[3]);
		
		renderer.render2D.modelview.idt();
		renderer.render2D.modelview.translate(worldSize.x, worldSize.y, 0.0f);
		renderer.render2D.sendMatrixToShader();
		
		Game.resources.textures.bindTexture(textureName);
		renderer.render2D.quad.render(worldSize.x, worldSize.y, true, true);
	}
}