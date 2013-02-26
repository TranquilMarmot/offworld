package com.bitwaffle.guts.graphics.animation;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.Render2D;

public class AnimationPart {
	/** Handle for the sprite sheet to use for this animation */
	private String sheetName;
	
	/** Array of every frame of the animation */
	private Frame[] frames;
	
	/** Index in frame array of current frame */
	private int currentFrame;
	/** How long this animation has been on the current frame */
	private float timeOnFrame;
	
	/** Scale to render frames at */
	private float xScale, yScale;
	
	public AnimationPart(String sheetName, Frame[] frames, float xScale, float yScale) {
		this.sheetName = sheetName;
		this.frames = frames;
		this.xScale = xScale;
		this.yScale = yScale;
	}
	
	/**
	 * Update an animation.
	 * Goes on to next frame if timeOnFrame >= currentFrame's length
	 * @param timeStep How much time has passed
	 */
	public void update(float timeStep){
		timeOnFrame += timeStep;
		
		if(timeOnFrame >= frames[currentFrame].getLength()){
			if(currentFrame < frames.length - 1){
				currentFrame++; 
				timeOnFrame = 0.0f;
			}
		} else if(timeOnFrame <= -frames[currentFrame].getLength()){
			if(currentFrame > 0){
				currentFrame--; 
				timeOnFrame = 0.0f;
			}
		}
	}
	
	public boolean nextPart(){
		return (this.currentFrame == frames.length - 1) && (this.timeOnFrame >= frames[currentFrame].getLength());
	}
	
	public boolean previousPart(){
		return (this.currentFrame == 0) && (this.timeOnFrame <= -frames[currentFrame].getLength());
	}
	
	/**
	 * Resets this animation part for its next loop
	 */
	public void goToBeginning(){
		this.currentFrame = 0;
		timeOnFrame = 0.0f;
	}
	
	/**
	 * Resets this animation part for its next loop
	 */
	public void goToEnd(){
		this.currentFrame = frames.length - 1;
		timeOnFrame = 0.0f;
	}
	
	
	/**
	 * Draw the current frame of this animation, with optional flipping
	 * @param renderer Renderer to use for drawing
	 * @param width Width of entity
	 * @param height Height of entity
	 */
	public void renderCurrentFrame(Render2D renderer, boolean flipHorizontal, boolean flipVertical){
		Game.resources.textures.bindTexture(sheetName);
		
		
		// for some reason the image always get flipped, so we pass flipped booleans
		renderer.quad.render(
				frames[currentFrame].getRenderWidth() * xScale,
				frames[currentFrame].getRenderHeight() * yScale,
				!flipHorizontal,
				!flipVertical,
				frames[currentFrame].getTexCoordBuffer()
		);
	}

}
