package com.bitwaffle.guts.graphics.animation;

import com.bitwaffle.guts.graphics.Render2D;

/**
 * Moving pictures! Like magic!
 * 
 * @author TranquilMarmot
 * @see Frame
 */
public class Animation {
	
	private AnimationPart[] parts;
	
	private int currentPart;
	
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
		this(other.parts);
	}
	
	public void update(float timeStep){
		parts[currentPart].update(timeStep);
		
		if(parts[currentPart].nextPart()){
			currentPart++;
			
			if(currentPart == parts.length)
				currentPart = 0;
			parts[currentPart].goToBeginning();
		} else if(parts[currentPart].previousPart()){
			currentPart--;
			
			if(currentPart < 0)
				currentPart = parts.length - 1;
			parts[currentPart].goToEnd();
		}
	}
	
	/**
	 * Draw the current frame of this animation
	 * @param renderer Renderer to use for drawing
	 * @param width Width of entity
	 * @param height Height of entity
	 */
	public void renderCurrentFrame(Render2D renderer){
		parts[currentPart].renderCurrentFrame(renderer, false, false);
	}
	
	/**
	 * Draw the current frame of this animation, with optional flipping
	 * @param renderer Renderer to use for drawing
	 * @param width Width of entity
	 * @param height Height of entity
	 */
	public void renderCurrentFrame(Render2D renderer, boolean flipHorizontal, boolean flipVertical){
		parts[currentPart].renderCurrentFrame(renderer, flipHorizontal, flipVertical);
	}
}
