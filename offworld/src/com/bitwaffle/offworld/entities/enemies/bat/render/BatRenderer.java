package com.bitwaffle.offworld.entities.enemies.bat.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.ai.path.DebugPathRenderer;
import com.bitwaffle.guts.entity.Entity;
import com.bitwaffle.guts.graphics.render.Renderer;
import com.bitwaffle.guts.graphics.render.render2d.EntityRenderer;
import com.bitwaffle.offworld.entities.enemies.bat.Bat;

public class BatRenderer implements EntityRenderer {

	@Override
	public void render(Renderer renderer, Entity ent, boolean renderDebug) {
		Bat bat = (Bat) ent;
		
		if(renderDebug)
			renderDebug(renderer, bat);
		else{
			renderer.r2D.setColor(1.0f, 1.0f, 1.0f, 1.0f);
			Gdx.gl20.glEnable(GL20.GL_BLEND);
			Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			
			if(bat.isSleeping())
				bat.getSleepAnimation().renderCurrentFrame(renderer);
			else
				bat.getFlyAnimation().renderCurrentFrame(renderer, false, false);
			
			Gdx.gl20.glDisable(GL20.GL_BLEND);
		}
	}
	
	private void renderDebug(Renderer renderer, Bat bat){
		renderer.r2D.setColor(0.0f, 1.0f, 1.0f, 0.5f);
		
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Game.resources.textures.bindTexture("blank");
		renderer.r2D.quad.render(bat.getWidth(), bat.getHeight());
		Gdx.gl20.glDisable(GL20.GL_BLEND);
		
		// render path
		DebugPathRenderer.renderDebug(renderer, bat.follower.pathfinder);
	}

}
