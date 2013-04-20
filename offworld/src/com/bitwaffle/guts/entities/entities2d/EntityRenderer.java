package com.bitwaffle.guts.entities.entities2d;

import com.bitwaffle.guts.graphics.render.Renderer;

/**Interface for rendering entities */
public interface EntityRenderer {
	/**
	 * Render an entity.
	 * Assume that the renderer's modelview
	 * matrix has already been translated to the entity and 
	 * rotated to the entity's angle (so you can just draw it)
	 * @param renderDebug Whether or not to render debug info with the entity (hit boxes etc.)
	 */
	public void render(Renderer renderer, Entity2D ent, boolean renderDebug);
}
