package com.bitwaffle.moguts.graphics.render.renderers;

import android.opengl.Matrix;

import com.bitwaffle.moguts.entities.BoxEntity;
import com.bitwaffle.moguts.entities.Entity;
import com.bitwaffle.moguts.graphics.render.Render2D;
import com.bitwaffle.offworld.Game;
import com.bitwaffle.offworld.entities.Player;


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

class BoxRenderer implements EntityRenderer{
	public void render(Render2D renderer, Entity ent) {
		BoxEntity box = (BoxEntity) ent;
		Game.resources.textures.bindTexture("box");
		renderer.program.setUniform("vColor", box.color[0], box.color[1], box.color[2], box.color[3]);
		renderer.quad.draw(renderer, box.getWidth(), box.getHeight());
	}
}

class PlayerRenderer implements EntityRenderer{
	public void render(Render2D renderer, Entity ent){
		Player player = (Player) ent;
		renderer.program.setUniform("vColor", player.color[0], player.color[1], player.color[2], player.color[3]);
		player.animation.renderCurrentFrame(renderer, player.getWidth(), player.getHeight(), player.isFacingRight(), false);
	}
}

class BackgroundRenderer implements EntityRenderer{
	public void render(Render2D renderer, Entity ent){
		// FIXME why the fuck is it so hard to render a background?!
		float width = Game.windowWidth / 75.0f;
		float height = Game.windowHeight / 75.0f;
		Matrix.translateM(renderer.modelview, 0, width - (width / 4.5f), height - (height / 10.0f), 0);
		renderer.program.setUniformMatrix4f("ModelView", renderer.modelview);
		
		Game.resources.textures.bindTexture("background");
		renderer.quad.draw(renderer, width, height, false, false);
	}
}
