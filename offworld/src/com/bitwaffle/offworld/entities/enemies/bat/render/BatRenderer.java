package com.bitwaffle.offworld.entities.enemies.bat.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.bitwaffle.guts.entities.entities2d.Entity2D;
import com.bitwaffle.guts.entities.entities2d.EntityRenderer;
import com.bitwaffle.guts.graphics.render.Renderer;
import com.bitwaffle.offworld.entities.enemies.bat.Bat;

public class BatRenderer implements EntityRenderer {

	@Override
	public void render(Renderer renderer, Entity2D ent, boolean renderDebug) {
		Bat bat = (Bat) ent;
		
		renderer.render2D.program.setUniform("vColor", 1.0f, 1.0f, 1.0f, 1.0f);
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		if(bat.isSleeping())
			bat.getSleepAnimation().renderCurrentFrame(renderer.render2D);
		else
			bat.getFlyAnimation().renderCurrentFrame(renderer.render2D, false, false);
		
		Gdx.gl20.glDisable(GL20.GL_BLEND);
	}

}
