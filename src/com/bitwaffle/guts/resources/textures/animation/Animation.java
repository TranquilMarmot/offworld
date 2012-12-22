package com.bitwaffle.guts.resources.textures.animation;

import com.bitwaffle.guts.graphics.render.Render2D;

import android.opengl.GLES20;

/**
 * Moving pictures! Like magic!
 * 
 * @author TranquilMarmot
 * @see Frame
 */
public class Animation {
	/** Handle for the sprite sheet to use for this animation */
	private int sheetHandle;
	
	/** Array of every frame of the animation */
	private Frame[] frames;
	
	/** Index in frame array of current frame */
	private int currentFrame;
	/** How long this animation has been on the current frame */
	private float timeOnFrame;
	
	/**
	 * Create a new animation
	 * This should only be called once for every animation
	 * (i.e. when it gets added to a HashMap of all animations),
	 * when ACTUALLY getting an animation, the copy constructor
	 * should be used
	 * @param frames All of the frames of the animation
	 */
	public Animation(int sheetHandle, Frame[] frames){
		this.frames = frames;
		timeOnFrame = 0.0f;
		currentFrame = 0;
		this.sheetHandle = sheetHandle;
	}
	
	/**
	 * Copy constructor for animations.
	 * Any class handling an animation should be
	 * getting its animation through this, so
	 * that multiple instances of one animation
	 * can be going at once but be on different
	 * frames
	 * @param other Animation to copy
	 */
	public Animation(Animation other){
		this.frames = other.frames;
		timeOnFrame = 0.0f;
		currentFrame = 0;
		this.sheetHandle = other.sheetHandle;
	}
	
	/**
	 * Update an animation.
	 * Goes on to next frame if timeOnFrame >= currentFrame's length
	 * @param timeStep How much time has passed
	 */
	public void updateAnimation(float timeStep){
		timeOnFrame += timeStep;
		
		if(timeOnFrame >= frames[currentFrame].getLength()){
			currentFrame++;
			if(currentFrame == frames.length)
				currentFrame = 0;
			timeOnFrame = 0.0f;
		}
	}
	
	/**
	 * Draw the current frame of this animation
	 * @param renderer Renderer to use for drawing
	 * @param width Width of entity
	 * @param height Height of entity
	 */
	public void renderCurrentFrame(Render2D renderer, float width, float height){
		renderCurrentFrame(renderer, width, height, false, false);
	}
	
	/**
	 * Draw the current frame of this animation, with optional flipping
	 * @param renderer Renderer to use for drawing
	 * @param width Width of entity
	 * @param height Height of entity
	 */
	public void renderCurrentFrame(Render2D renderer, float width, float height, boolean flipHorizontal, boolean flipVertical){
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, sheetHandle);
		
		// for some reason the image always get flipped, so we pass flipped booleans
		renderer.quad.render(width, height, !flipHorizontal, !flipVertical, frames[currentFrame].getTexCoordBuffer());
	}
}
