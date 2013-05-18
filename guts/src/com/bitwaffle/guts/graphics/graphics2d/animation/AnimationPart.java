package com.bitwaffle.guts.graphics.graphics2d.animation;

import com.bitwaffle.guts.graphics.Renderer;

/**
 * Generally, each AnimationPart corresponds to a sprite sheet
 * and a series of frames. An Animation loops through a series of AnimationParts,
 * that way an Animation can happen over a series of images.
 * 
 * @author TranquilMarmot
 */
public class AnimationPart {
	/** Array of every frame of the animation */
	private Frame[] frames;
	
	/** Index in frame array of current frame */
	private int currentFrame;
	/** How long this animation has been on the current frame */
	private float timeOnFrame;
	
	/** Scale to render frames at */
	private float xScale, yScale;
	
	/**
	 * @param frames Array of frames to loop through
	 */
	public AnimationPart(Frame[] frames, float xScale, float yScale) {
		this.frames = frames;
		this.xScale = xScale;
		this.yScale = yScale;
		currentFrame = 0;
		timeOnFrame = 0.0f;
	}
	
	/** Copy constructor */
	public AnimationPart(AnimationPart other){
		this(other.frames, other.xScale, other.yScale);
	}
	
	/**
	 * Goes on to next frame if timeOnFrame >= currentFrame's length,
	 * or the previous frame if timeOnFrame <= -currentFrame's length
	 * @param timeStep How much time to advance animation by. Can be positive or negative (to play an animation backwards).
	 */
	public void update(float timeStep){
		timeOnFrame += timeStep;
		
		// go on to next frame
		if(timeOnFrame >= frames[currentFrame].getLength()){
			if(currentFrame < frames.length - 1){
				currentFrame++; 
				timeOnFrame = 0.0f;
			}
			
		// go to previous frame
		} else if(timeOnFrame <= -frames[currentFrame].getLength()){
			if(currentFrame > 0){
				currentFrame--; 
				timeOnFrame = 0.0f;
			}
		}
	}
	
	/**
	 * The Animation that's using this AnimationPart checks this method every update, so it knows when to go on to the next AnimationPart.
	 * @return Whether or not this animation part is done and it's time to move on to the next animation part in the {@link Animation}
	 */
	public boolean nextPart(){
		return (this.currentFrame == frames.length - 1) && (this.timeOnFrame >= frames[currentFrame].getLength());
	}
	
	/**
	 * So that animations can be played backwards.
	 * The Animation that's using this AnimationPart checks this method every update, so it knows when to go back to the previous AnimationPart.
	 * @return Whether or not this animation part is done and it's time to go back to the previous animation part in the {@link Animation}
	 */
	public boolean previousPart(){
		return (this.currentFrame == 0) && (this.timeOnFrame <= -frames[currentFrame].getLength());
	}
	
	/** Resets this animation part for its next loop, setting the current frame to the first frame. */
	public void goToBeginning(){
		this.currentFrame = 0;
		this.timeOnFrame = 0.0f;
	}
	
	/**Resets this animation part for its next loop, setting the current frame to the final frame. */
	public void goToEnd(){
		this.currentFrame = frames.length - 1;
		this.timeOnFrame = 0.0f;
	}
	
	
	/** Draw the current frame of this animation, with optional flipping */
	public void renderCurrentFrame(Renderer renderer, boolean flipHorizontal, boolean flipVertical){
		frames[currentFrame].render(renderer, xScale, yScale, !flipHorizontal, !flipVertical);
	}
	
	/** @return How many frames this animation part has */
	public int numFrames(){ return frames.length; }
	
	/** @return Current frame this part is on */
	public int currentFrame(){ return currentFrame; }

}
