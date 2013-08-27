package com.bitwaffle.offworld.entities.enemies.bat.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.ai.path.DebugPathRenderer;
import com.bitwaffle.guts.graphics.Renderer;
import com.bitwaffle.guts.graphics.graphics2d.ObjectRenderer2D;
import com.bitwaffle.offworld.entities.enemies.bat.Bat;

public class BatRenderer implements ObjectRenderer2D {

	@Override
	public void render(Renderer renderer, Object ent) {
		Bat bat = (Bat) ent;
		
		if(renderer.renderDebug)
			renderDebug(renderer, bat);
		else{
			renderer.r2D.setColor(1.0f, 1.0f, 1.0f, 1.0f);
			Gdx.gl20.glEnable(GL20.GL_BLEND);
			Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			
			if(bat.ai.currentState() == bat.sleepState)
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
		DebugPathRenderer.renderDebug(renderer, bat.attackState.pathfinder);
	}

}
