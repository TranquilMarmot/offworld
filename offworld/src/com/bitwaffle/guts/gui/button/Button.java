package com.bitwaffle.guts.gui.button;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.gui.GUIObject;

/**
 * A button that can be pressed
 * 
 * @author TranquilMarmot
 * @see GUI
 */
public abstract class Button extends GUIObject{
	/** Whether or not the button is active/visible (can only be pressed if both are true) */
	private boolean isActive;
	
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
		if(this.isActive){
			this.isDown = true;
			onPress();
		}
	}
	
	/** What to do when the button is pushed down (generally, change color/image) */
	protected abstract void onPress();
	
	/** Release the button! This sets isDown to false. */
	public void release(){
		if(this.isActive){
			this.isDown = false;
			onRelease();
		}
	}
	
	/** What to do when the button is released after being pressed (generally, take some sort of action) */
	protected abstract void onRelease();
	
	/** Called instead of release() when a button is pressed but then the pointer slides off of it */
	public void slideRelease(){
		if(this.isActive){
			this.isDown = false;
			onSlideRelease();
		}
	}
	
	/** What to do when a finger pressed the button down, but then slid off of it, rather than just releasing (generally, nothing) */
	protected abstract void onSlideRelease();
	
	/**
	 * @return Whether or not a button is currently being held down
	 */
	public boolean isDown(){ return isDown;}
	
	/**
	 * Deactivate a button
	 */
	public void deactivate(){
		isActive = false;
	}
	
	/**
	 * Activate a button
	 */
	public void activate() {
		isActive = true;
	}
	
	/**
	 * @return Whether or not a button is active
	 */
	public boolean isActive(){ return isActive; }
	

	@Override
	public void hide(){ 
		super.hide();
		deactivate();
	}
	
	@Override
	public void show(){
		super.show();
		activate();
	}
	
	
	/**
	 * Notify a button that it's being dragged
	 * @param dx Amount of drag on X axis
	 * @param dy Amount of drag on Y axis
	 */
	public void drag(float dx, float dy) {
		this.onDrag(dx, dy);
	}
	
	/**
	 * What to do when a button is dragged
	 * @param dx Length of drag on X axis
	 * @param dy Length of drag on Y axis
	 */
	protected abstract void onDrag(float dx, float dy);
}
