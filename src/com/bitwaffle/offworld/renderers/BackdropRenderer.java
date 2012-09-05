package com.bitwaffle.offworld.renderers;

import android.opengl.Matrix;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.moguts.entities.Entity;
import com.bitwaffle.moguts.graphics.render.EntityRenderer;
import com.bitwaffle.moguts.graphics.render.Render2D;
import com.bitwaffle.moguts.util.MathHelper;
import com.bitwaffle.offworld.Game;

/**
 * Used for rendering the background image
 * 
 * @author TranquilMarmot
 */
public class BackdropRenderer implements EntityRenderer{
	public void render(Render2D renderer, Entity ent, boolean renderDebug){
		renderer.program.setUniform("vColor", 1.0f, 1.0f, 1.0f, 1.0f);

		Vector2 worldSize = MathHelper.toWorldSpace(Game.windowWidth / 2, Game.windowHeight / 2);
		
		Matrix.setIdentityM(renderer.modelview, 0);
		Matrix.translateM(renderer.modelview, 0, worldSize.x, worldSize.y, 0.0f);
		renderer.sendModelViewToShader();
		
		Game.resources.textures.bindTexture("background");
		renderer.quad.draw(worldSize.x, worldSize.y, true, true);
	}
}