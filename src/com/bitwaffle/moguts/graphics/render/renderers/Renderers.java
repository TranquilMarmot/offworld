package com.bitwaffle.moguts.graphics.render.renderers;

import android.opengl.Matrix;

import com.bitwaffle.moguts.entities.BoxEntity;
import com.bitwaffle.moguts.entities.Entity;
import com.bitwaffle.moguts.graphics.render.Render2D;
import com.bitwaffle.offworld.Game;

/**
 * Every <@link Entity> has a Renderers assigned to it, which has its
 * <code>render()</code> method called after the camera modifies the modelview matrix
 * to be at the location of the entity (that is, anything done to the modelview will modify
 * it originating at the center of the entity)
 * 
 * @author TranquilMarmot
 */
public enum Renderers{
	BOX(new BoxRenderer()),
	BACKGROUND(new BackgroundRenderer()),
	PLAYER(new PlayerRenderer());
	
	private EntityRenderer renderer;
	private Renderers(EntityRenderer renderer){
		this.renderer = renderer;
	}
	
	public void render(Render2D render2D, Entity ent){
		renderer.render(render2D, ent);
	}
}

abstract interface EntityRenderer { public void render(Render2D renderer, Entity ent); }

/**
 * Used for rendering wooden boxes
 * 
 * @author TranquilMarmot
 */
class BoxRenderer implements EntityRenderer{
	public void render(Render2D renderer, Entity ent) {
		BoxEntity box = (BoxEntity) ent;
		Game.resources.textures.bindTexture("box");
		renderer.program.setUniform("vColor", box.color[0], box.color[1], box.color[2], box.color[3]);
		renderer.quad.draw(box.getWidth(), box.getHeight());
	}
}

/**
 * Used for rendering the background image
 * 
 * @author TranquilMarmot
 */
class BackgroundRenderer implements EntityRenderer{
	public void render(Render2D renderer, Entity ent){
		// FIXME why the fuck is it so hard to render a background?!
		float width = Game.windowWidth / 75.0f;
		float height = Game.windowHeight / 75.0f;
		Matrix.translateM(renderer.modelview, 0, width - (width / 4.5f), height - (height / 10.0f), 0);
		renderer.sendModelViewToShader();
		
		Game.resources.textures.bindTexture("background");
		renderer.quad.draw(width, height, false, false);
	}
}
