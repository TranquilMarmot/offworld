package com.bitwaffle.moguts.graphics.render.renderers;

import com.bitwaffle.moguts.entities.Entity;
import com.bitwaffle.moguts.graphics.render.Render2D;

public enum Renderers {
	BOX_RENDERER(new BoxRenderer());
	
	private EntityRenderer entRenderer;
	private Renderers(EntityRenderer renderer){
		this.entRenderer = renderer;
	}
	
	public void render(Render2D renderer, Entity ent){
		entRenderer.render(renderer, ent);
	}
}

class BoxRenderer implements EntityRenderer{
	public void render(Render2D renderer, Entity ent) {
		
	}
}
