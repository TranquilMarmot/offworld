package com.bitwaffle.offworld.entities.enemies.bat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.bitwaffle.guts.entities.Entity;
import com.bitwaffle.guts.graphics.EntityRenderer;
import com.bitwaffle.guts.graphics.Render2D;

public class BatRenderer implements EntityRenderer {

	@Override
	public void render(Render2D renderer, Entity ent, boolean renderDebug) {
		Bat bat = (Bat) ent;
		
		renderer.program.setUniform("vColor", 1.0f, 1.0f, 1.0f, 1.0f);
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		if(bat.isSleeping())
			bat.sleepAnimation.renderCurrentFrame(renderer);
		else
			bat.flyAnimation.renderCurrentFrame(renderer);
		
		Gdx.gl20.glDisable(GL20.GL_BLEND);
	}

}
