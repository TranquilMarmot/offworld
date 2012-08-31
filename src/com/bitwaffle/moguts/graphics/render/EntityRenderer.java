package com.bitwaffle.moguts.graphics.render;

import com.bitwaffle.moguts.entities.Entity;

/**
 * Interface for entity renderers
 */
public interface EntityRenderer {
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
