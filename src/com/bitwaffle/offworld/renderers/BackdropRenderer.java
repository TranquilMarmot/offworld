package com.bitwaffle.offworld.renderers;

import android.opengl.Matrix;

import com.bitwaffle.moguts.entities.Entity;
import com.bitwaffle.moguts.graphics.render.EntityRenderer;
import com.bitwaffle.moguts.graphics.render.Render2D;
import com.bitwaffle.offworld.Game;

/**
 * Used for rendering the background image
 * 
 * @author TranquilMarmot
 */
public class BackdropRenderer implements EntityRenderer{
	public void render(Render2D renderer, Entity ent){
		renderer.program.setUniform("vColor", 1.0f, 1.0f, 1.0f, 1.0f);
		
		// FIXME magic numbers! ahhh! Find a truly screen-size-independent way to do this!!!
		float width = Game.windowWidth / 1300.0f;
		float height = Game.windowHeight / 1300.0f;
		
		Matrix.setIdentityM(renderer.modelview, 0);
		Matrix.translateM(renderer.modelview, 0, width, height, 0.0f);
		renderer.sendModelViewToShader();
		
		Game.resources.textures.bindTexture("background");
		renderer.quad.draw(width, height, true, true);
	}
	public void renderDebug(Render2D renderer, Entity ent){}
}