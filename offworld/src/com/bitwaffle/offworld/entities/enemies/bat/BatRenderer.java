package com.bitwaffle.offworld.entities.enemies.bat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.Entity;
import com.bitwaffle.guts.graphics.EntityRenderer;
import com.bitwaffle.guts.graphics.Render2D;

public class BatRenderer implements EntityRenderer {
	private static final String
		FLY_LEGS = "bat-fly-legs",
		FLY_LEGS_R = "bat-fly-legs-r",
		FLY_LEGS_L = "bat-fly-legs-l";
	
	private static final float[]
			FLY_LEGS_OFFSET = {0.0f, -1.2f},
			FLY_LEGS_R_OFFSET = {0.0f, -1.2f},
			FLY_LEGS_L_OFFSET = {0.2f, -1.2f};
	
	private static final float
		VELOCITY_THRESHOLD = 0.5f,
		SCALE = 0.4f;

	@Override
	public void render(Render2D renderer, Entity ent, boolean renderDebug) {
		Bat bat = (Bat) ent;
		
		renderer.program.setUniform("vColor", 1.0f, 1.0f, 1.0f, 1.0f);
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		if(bat.isSleeping())
			bat.sleepAnimation.renderCurrentFrame(renderer);
		else {
			Vector2 linVec = bat.body.getLinearVelocity();
			
			if(linVec.x < -VELOCITY_THRESHOLD){
				renderer.modelview.translate(FLY_LEGS_L_OFFSET[0], FLY_LEGS_L_OFFSET[1], 0.0f);
				Game.resources.textures.getSubImage(FLY_LEGS_L).render(renderer, SCALE, SCALE, false, true);
				renderer.modelview.translate(-FLY_LEGS_L_OFFSET[0], -FLY_LEGS_L_OFFSET[1], 0.0f);
			} else if(linVec.x > VELOCITY_THRESHOLD){
				renderer.modelview.translate(FLY_LEGS_R_OFFSET[0], FLY_LEGS_R_OFFSET[1], 0.0f);
				Game.resources.textures.getSubImage(FLY_LEGS_R).render(renderer, SCALE, SCALE, false, true);
				renderer.modelview.translate(-FLY_LEGS_R_OFFSET[0], -FLY_LEGS_R_OFFSET[1], 0.0f);
			} else {
				renderer.modelview.translate(FLY_LEGS_OFFSET[0], FLY_LEGS_OFFSET[1], 0.0f);
				Game.resources.textures.getSubImage(FLY_LEGS).render(renderer, SCALE, SCALE, false, true);
				renderer.modelview.translate(-FLY_LEGS_OFFSET[0], -FLY_LEGS_OFFSET[1], 0.0f);
			}
			
			
			bat.flyAnimation.renderCurrentFrame(renderer);
		}
		
		Gdx.gl20.glDisable(GL20.GL_BLEND);
	}

}
