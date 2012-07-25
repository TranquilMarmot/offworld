package com.bitwaffle.moguts.gui.button;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.moguts.gui.GUIObject;

/**
 * A button that can be pressed
 * 
 * @author TranquilMarmot
 * @see GUI
 */
public abstract class Button extends GUIObject{
	/** Whether or not the button is active/visible (can only be pressed if both are true) */
	private boolean isActive, isVisible;
	
	/** Whether or not the button is being held down */
	private boolean isDown;
	
	/**
	 * Create a new button
	 * @param x X location of button
	 * @param y Y location of button
	 */
	public Button(float x, float y) {
		super(x, y);
		isActive = true;
		isVisible = true;
	}
	
	/**
	 * Check if a point lies within a button.
	 * This needs to be overriden by any implementing class to reflect the shape of the button.
	 * @param x X value of point, in screen coordinates
	 * @param y Y value of point, in screen coordinates
	 * @return Whether or not the button contains the point
	 */
	public abstract boolean contains(float x, float y);
	
	/**
	 * Check if a point lies within a button
	 * @param point Point to check for
	 * @return Whether or not the given point lies within the button
	 */
	public boolean contains(Vector2 point){
		return this.contains(point.x, point.y);
	}
	
	/** Press the button! This sets isDown to true. */
	public void press(){
		this.isDown = true;
		onPress();
	}
	
	/** What to do when the button is pushed down (generally, change color/image) */
	protected abstract void onPress();
	
	/** Release the button! This sets isDown to false. */
	public void release(){
		this.isDown = false;
		onRelease();
	}
	
	/** What to do when the button is released after being pressed (generally, take some sort of action) */
	protected abstract void onRelease();
	
	/** Called instead of release() when a button is pressed but then the pointer slides off of it */
	public void slideRelease(){
		this.isDown = false;
		onSlideRelease();
	}
	
	/** What to do when a finger pressed the button down, but then slid off of it, rather than just releasing (generally, nothing) */
	protected abstract void onSlideRelease();
	
	// FIXME these are pretty much useless unless they get honored by press() and release()
	public boolean isActive(){ return isActive; }
	public void deactivate(){ isActive = false; }
	public void activate(){ isActive = true; }
	
	public boolean isVisible(){ return isVisible; }
	public void hide(){ isVisible = false; }
	public void show(){ isVisible = true; }
	
	public boolean isDown(){ return isDown;}
}
