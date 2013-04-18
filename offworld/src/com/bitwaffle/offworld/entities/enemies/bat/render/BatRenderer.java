package com.bitwaffle.offworld.entities.enemies.bat.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.bitwaffle.guts.entities.entities2d.Entity2D;
import com.bitwaffle.guts.entities.entities2d.Entity2DRenderer;
import com.bitwaffle.guts.graphics.render.Render2D;
import com.bitwaffle.offworld.entities.enemies.bat.Bat;

public class BatRenderer implements Entity2DRenderer {

	@Override
	public void render(Render2D renderer, Entity2D ent, boolean renderDebug) {
		Bat bat = (Bat) ent;
		
		renderer.program.setUniform("vColor", 1.0f, 1.0f, 1.0f, 1.0f);
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		if(bat.isSleeping())
			bat.getSleepAnimation().renderCurrentFrame(renderer);
		else
			bat.getFlyAnimation().renderCurrentFrame(renderer, false, false);
		
		Gdx.gl20.glDisable(GL20.GL_BLEND);
	}

}
