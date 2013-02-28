package com.bitwaffle.offworld.entities.player;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.graphics.animation.Animation;

public class PlayerBodyAnimation extends Animation {
	// TODO maybe only catch these on keyframes and then interpolate the values
	/** 
	 * Location of the right arm's shoulder on each frame,
	 * in world coordinates. The location corresponds to the distance
	 * from the middle of the player's body the shoulder is at.
	 */
	private static final float[][] rShoulderLocations = {
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
	
	/** Offset of gun from right arm's shoulder */
	private static final Vector2 gunOffset = new Vector2(0.85f, -0.5f);
	
	/** Offset of left arm from right arm's shoulder*/
	private static final Vector2 lArmOffset = new Vector2(0.1f, -0.05f);
	
	/**
	 * @param animation Source animation
	 */
	public PlayerBodyAnimation(Animation animation) {
		super(animation);
	}
	
	/**
	 * @return Location of the player's right shoulder on the current frame.
	 */
	public Vector2 getCurrentRShoulderLocation(){
		return new Vector2(rShoulderLocations[currentFrame()][0], rShoulderLocations[currentFrame()][1]);
	}
	
	/**
	 * @return Location of the player's left shoulder on the current frame
	 */
	public Vector2 getCurrentLShoulderLocation(){
		return getCurrentRShoulderLocation().add(lArmOffset);
	}
	
	/**
	 * The offset of the player's gun for the current frame,
	 * in terms of its offset in world coordinates from the player's
	 * right shoulder location
	 * @return Gun location
	 */
	public Vector2 getGunOffset(){
		return gunOffset;
	}
}
