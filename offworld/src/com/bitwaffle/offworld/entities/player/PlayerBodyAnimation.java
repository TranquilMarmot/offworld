package com.bitwaffle.offworld.entities.player;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.graphics.animation.Animation;

public class PlayerBodyAnimation extends Animation {
	float[][] shoulderLocations = {
			{-0.21301842f, 0.8287382f},
			{-0.21301842f, 0.82005787f},
			{-0.21301842f, 0.81137705f},
			{-0.21301842f, 0.79401636f},
			{-0.21301842f, 0.79401636f},
			{-0.20433784f, 0.8026967f},
			{-0.16961563f, 0.8287382f},
			{-0.16961563f, 0.8287382f},
			{-0.16961563f, 0.82005787f},
			{-0.16961563f, 0.82005787f},
			{-0.18697667f, 0.83741903f},
			{-0.20433784f, 0.8287382f},
			{-0.21301842f, 0.83741903f},
			{-0.21301842f, 0.8287382f},
			{-0.21301842f, 0.81137705f},
			{-0.19565725f, 0.8026967f},
			{-0.18697667f, 0.79401636f},
			{-0.19565725f, 0.79401636f},
			{-0.19565725f, 0.78533554f},
			{-0.19565725f, 0.7679744f},
			{-0.18697667f, 0.7766552f},
			{-0.18697667f, 0.8026967f},
			{-0.17829621f, 0.8026967f},
			{-0.16961563f, 0.83741903f},
			{-0.16961563f, 0.8460994f},
			{-0.16961563f, 0.83741903f},
			{-0.15225458f, 0.83741903f},
			{-0.16093516f, 0.83741903f},
			{-0.15225458f, 0.83741903f},
			{-0.17829621f, 0.83741903f},
			{-0.18697667f, 0.83741903f},
			{-0.21301842f, 0.82005787f}	
	};
	
	public static final Vector2 gunOffset = new Vector2(0.61631774f, -0.58159732f);
	
	Vector2 lArmOffset = new Vector2(0.1f, -0.1f);
	
	public PlayerBodyAnimation(Animation animation) {
		super(animation);
	}
	
	public Vector2 getCurrentRShoulderLocation(){
		return new Vector2(shoulderLocations[currentFrame()][0], shoulderLocations[currentFrame()][1]);
	}
	
	public Vector2 getCurrentLShoulderLocation(){
		return getCurrentRShoulderLocation().add(lArmOffset);
	}
}
