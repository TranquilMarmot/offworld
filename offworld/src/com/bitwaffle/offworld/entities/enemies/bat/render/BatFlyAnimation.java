package com.bitwaffle.offworld.entities.enemies.bat.render;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.Render2D;
import com.bitwaffle.guts.graphics.animation.Animation;
import com.bitwaffle.offworld.entities.enemies.bat.Bat;

public class BatFlyAnimation extends Animation {
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
	
	private Bat bat;

	public BatFlyAnimation(Bat bat) {
		super(Game.resources.textures.getAnimation("bat-fly"));
		this.bat = bat;
	}
	
	@Override
	public void renderCurrentFrame(Render2D renderer, boolean flipHorizontal, boolean flipVertical){
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
		
		super.renderCurrentFrame(renderer, flipHorizontal, flipVertical);
	}

}
