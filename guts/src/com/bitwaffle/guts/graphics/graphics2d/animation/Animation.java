package com.bitwaffle.guts.graphics.graphics2d.animation;

import com.bitwaffle.guts.graphics.Renderer;

/**
 * Moving pictures! Like magic!
 * 
 * @author TranquilMarmot
 */
public class Animation {
	/** 
	 * All of the parts of this animation
	 * Generally, each part corresponds to a sprite sheet
	 * and a series of frames. Each frame corresponds to a section of a sprite sheet
	 * and a length for how long to stay on that frame.
	 */
	private AnimationPart[] parts;
	
	/** Current part that animation is on */
	private int currentPart;
	
	/**
	 * Create a new animation.
	 * NOTE: Each animation should only be created once!
	 * Whenever you want an animation, get it from Game.resources
	 * This way, multiple entities can use the same animation but
	 * be at different places in the animation
	 * @param parts Parts to use for animation, gets iterated through in order
	 */
	public Animation(AnimationPart[] parts){
		this.parts = parts;
		this.currentPart = 0;
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
		this(copyParts(other));
	}
	
	/** Copy parts from one animation into a new parts array */
	private static AnimationPart[] copyParts(Animation src){
		AnimationPart[] copy = new AnimationPart[src.parts.length];
		for(int i = 0; i < copy.length; i++)
			copy[i] = new AnimationPart(src.parts[i]);
		return copy;
	}
	
	/** @return Total number of frames in this animation */
	public int totalFrames(){
		// sum of all frames in all parts
		int count = 0;
		for(AnimationPart part : parts)
			count += part.numFrames();
		return count;
	}
	
	/** @return Current frame this animation is on, out of totalFrames() frame */
	public int currentFrame(){
		int count = 0;
		for(int i = 0; i < currentPart; i++)
			count += parts[i].numFrames();
		return count + parts[currentPart].currentFrame();
	}
	
	/** @param timeStep Time to advance animation by, in seconds */
	public void update(float timeStep){
		// update the current part
		parts[currentPart].update(timeStep);
		
		// check if the part is done and it's time to move on to the next one
		if(parts[currentPart].nextPart()){
			currentPart++;
			
			if(currentPart == parts.length)
				currentPart = 0;
			parts[currentPart].goToBeginning();
			
		// check if part is done and it's time to move to the previous part
		} else if(parts[currentPart].previousPart()){
			currentPart--;
			
			if(currentPart < 0)
				currentPart = parts.length - 1;
			parts[currentPart].goToEnd();
		}
	}
	
	/** Draw the current frame of this animation */
	public void renderCurrentFrame(Renderer renderer){
		parts[currentPart].renderCurrentFrame(renderer, false, false);
	}
	
	/**
	 * Draw the current frame of this animation, with optional flipping
	 * @param renderer Renderer to use for drawing
	 * @param width Width of entity
	 * @param height Height of entity
	 */
	public void renderCurrentFrame(Renderer renderer, boolean flipHorizontal, boolean flipVertical){
		parts[currentPart].renderCurrentFrame(renderer, flipHorizontal, flipVertical);
	}
}
