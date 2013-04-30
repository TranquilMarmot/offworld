package com.bitwaffle.offworld.entities.enemies.bat.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.bitwaffle.guts.entity.Entity;
import com.bitwaffle.guts.graphics.render.Renderer;
import com.bitwaffle.guts.graphics.render.render2d.EntityRenderer;
import com.bitwaffle.offworld.entities.enemies.bat.Bat;

public class BatRenderer implements EntityRenderer {

	@Override
	public void render(Renderer renderer, Entity ent, boolean renderDebug) {
		Bat bat = (Bat) ent;
		
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
