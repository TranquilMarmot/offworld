package com.bitwaffle.offworld.renderers;

import org.lwjgl.util.vector.Vector3f;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.entities.Entity;
import com.bitwaffle.guts.graphics.render.EntityRenderer;
import com.bitwaffle.guts.graphics.render.Render2D;
import com.bitwaffle.guts.util.MathHelper;

/**
 * Used for rendering the background image
 * 
 * @author TranquilMarmot
 */
public class BackdropRenderer implements EntityRenderer{
	private float windowWidth = 0.0f, windowHeight = 0.0f;
	private float worldSizeX, worldSizeY;
	
	public void render(Render2D renderer, Entity ent, boolean renderDebug){
		if(windowWidth != Game.windowWidth || windowHeight != Game.windowHeight){
			windowWidth = Game.windowWidth;
			windowHeight = Game.windowHeight;
			Vector2 worldSize = MathHelper.toWorldSpace(windowWidth / 2, windowHeight / 2);
			worldSizeX = worldSize.x;
			worldSizeY = worldSize.y;
		}
		
		renderer.program.setUniform("vColor", 1.0f, 1.0f, 1.0f, 1.0f);
		
		renderer.modelview.setIdentity();
		renderer.modelview.translate(new Vector3f(worldSizeX, worldSizeY, 0.0f));
		renderer.sendModelViewToShader();
		
		Game.resources.textures.bindTexture("background");
		renderer.quad.draw(worldSizeX, worldSizeY, true, true);
	}
}