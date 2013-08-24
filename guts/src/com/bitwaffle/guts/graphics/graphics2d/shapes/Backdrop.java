package com.bitwaffle.guts.graphics.graphics2d.shapes;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entity.Entity;
import com.bitwaffle.guts.graphics.Renderer;
import com.bitwaffle.guts.graphics.graphics2d.EntityRenderer2D;
import com.bitwaffle.guts.util.MathHelper;

/**
 * Used for rendering an image that takes up the entire screen.
 * 
 * @author TranquilMarmot
 */
public class Backdrop implements EntityRenderer2D{
	/** Size of window (saved so that world size is only calculated when it changes) */
	private float renderWidth = 0.0f, renderHeight = 0.0f;
	
	/** How big the world is */
	private Vector2 worldSize;
	
	/** Name of texture to bind */
	private String textureName;
	
	/** Color to render backdrop as */
	private float[] color;
	
	public Backdrop(String textureName, float[] color){
		worldSize = new Vector2();
		this.textureName = textureName;
		this.color = color;
	}
	
	public void render(Renderer renderer, Entity ent, boolean renderDebug){
		if(renderWidth != Game.renderWidth || renderHeight != Game.renderHeight){
			renderWidth = Game.renderWidth;
			renderHeight = Game.renderHeight;
			MathHelper.toWorldSpace(worldSize, renderWidth / 2, renderHeight / 2);
		}
		
		renderer.r2D.setColor(color);
		
		renderer.modelview.idt();
		renderer.modelview.translate(worldSize.x, worldSize.y, 0.0f);
		renderer.r2D.sendMatrixToShader();
		
		Game.resources.textures.bindTexture(textureName);
		renderer.r2D.quad.render(worldSize.x, worldSize.y, true, true);
	}
}