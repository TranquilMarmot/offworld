package com.bitwaffle.moguts.graphics.render.renderers;

import com.bitwaffle.moguts.entities.BoxEntity;
import com.bitwaffle.moguts.graphics.render.Render2D;
import com.bitwaffle.offworld.Game;

public enum Renderers{
	BOX_RENDERER(new BoxRenderer());
	
	public EntityRenderer<?> renderer;
	private Renderers(EntityRenderer<?> renderer){
		this.renderer = renderer;
	}
}

class BoxRenderer implements EntityRenderer<BoxEntity>{
	public void render(Render2D renderer, BoxEntity ent) {
		Game.resources.textures.bindTexture("box");
		renderer.program.setUniform("vColor", ent.color[0], ent.color[1], ent.color[2], ent.color[3]);
		renderer.quad.draw(renderer, ent.getWidth(), ent.getHeight());
	}
}
