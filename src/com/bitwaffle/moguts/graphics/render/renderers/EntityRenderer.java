package com.bitwaffle.moguts.graphics.render.renderers;

import com.bitwaffle.moguts.entities.Entity;
import com.bitwaffle.moguts.graphics.render.Render2D;

public interface EntityRenderer<T extends Entity> {
	public void render(Render2D renderer, T ent);
}
