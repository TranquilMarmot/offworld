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
	 * @param renderDebug Whether or not to render debug info with the entity (hit boxes etc.)
	 */
	public void render(Render2D renderer, Entity ent, boolean renderDebug);
}
