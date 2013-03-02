package com.bitwaffle.offworld.entities.player;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.graphics.animation.Animation;

public class PlayerBodyAnimation extends Animation {
	/** 
	 * Location of the right arm's shoulder on each frame,
	 * in world coordinates. The location corresponds to the distance
	 * from the middle of the player's body the shoulder is at.
	 */
	private static final float[][] rShoulderLocations = {
			{-0.22111511f, 0.7997613f}, // 0
			{-0.21965f, 0.793903f},
			{-0.218185f, 0.788045f},
			{-0.21672f, 0.782187f},
			{-0.21525574f, 0.7763281f}, // 4
			{-0.206468f, 0.783651f},
			{-0.197681f, 0.790974f},
			{-0.188894f, 0.798297f},
			{-0.18010712f, 0.80561924f}, // 8
			{-0.190359f, 0.811478f},
			{-0.200611f, 0.817336f},
			{-0.210863f, 0.823194f},
			{-0.22111511f, 0.829052f}, // 12
			{-0.216722f, 0.82173f},
			{-0.212328f, 0.814407f},
			{-0.207934f, 0.807084f},
			{-0.2035408f, 0.7997613f}, // 16
			{-0.200612f, 0.792439f},
			{-0.197683f, 0.785116f},
			{-0.194754f, 0.777793f},
			{-0.19182587f, 0.77047014f}, // 20
			{-0.187432f, 0.789509f},
			{-0.183039f, 0.808548f},
			{-0.178645f, 0.827587f},
			{-0.17425156f, 0.84662676f}, // 24
			{-0.171322f, 0.843698f},
			{-0.168392f, 0.840769f},
			{-0.165462f, 0.83784f},
			{-0.1625328f, 0.8349104f}, // 28
			{-0.174249f, 0.829052f},
			{-0.185966f, 0.823194f},
			{-0.20940018f, 0.81147766f} // 31
	};
	
	/** Offset of gun from right arm's shoulder */
	private static final Vector2 gunOffset = new Vector2(0.85f, -0.5f);
	
	/** Offset of left arm from right arm's shoulder*/
	private static final Vector2 lArmOffset = new Vector2(0.02f, 0.04f);
	
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