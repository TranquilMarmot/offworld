package com.bitwaffle.moguts.graphics.render.renderers;

import android.opengl.Matrix;

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
	CIRCLE(new CircleRenderer()),
	BACKGROUND(new BackgroundRenderer()),
	PLAYER(new PlayerRenderer());
	
	private EntityRenderer renderer;
	private Renderers(EntityRenderer renderer){
		this.renderer = renderer;
	}
	
	public void render(Render2D render2D, Entity ent){
		renderer.render(render2D, ent);
	}
	
	public void renderDebug(Render2D render2D, Entity ent){
		renderer.renderDebug(render2D, ent);
	}
}

/**
 * Interface for entity renderers
 */
abstract interface EntityRenderer {
	/**
	 * Render an entity.
	 * Assume that the renderer's modelview
	 * matrix has already been translated to the entity and 
	 * rotated to the entity's angle (so you can just draw it)
	 * @param renderer Renderer to use for rendering entity
	 * @param ent Entity to render
	 */
	public void render(Render2D renderer, Entity ent);
	
	/**
	 * Render a debug version of an entity
	 * Assume that the renderer's modelview
	 * matrix has already been translated to the entity and 
	 * rotated to the entity's angle
	 * Typically, drawing debug info entails drawing
	 * the entity's shape in the physics world with a different
	 * color and some transparency depending on the entity's state
	 * (i.e. draw in green if the entity is active, red if it isn't)
	 * @param renderer Renderer to use for rendering entity
	 * @param ent Entity to render
	 */
	public void renderDebug(Render2D renderer, Entity ent);
}

/**
 * Used for rendering the background image
 * 
 * @author TranquilMarmot
 */
class BackgroundRenderer implements EntityRenderer{
	public void render(Render2D renderer, Entity ent){
		renderer.program.setUniform("vColor", 1.0f, 1.0f, 1.0f, 1.0f);
		
		// FIXME magic numbers! ahhh!
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
