package com.bitwaffle.guts.gui.elements.button;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.gui.elements.GUIObject;

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
	 * Whether or not this button is currently selected.
	 * When a mouse is being used, true when the mouse is hovering over the button.
	 * When a controller is being used, true when this button is the currently selected item.
	 */
	private boolean isSelected;
	
	/**
	 * For when a controller or keyboard is being used to navigate
	 * through buttons. Indicates which button should be selected next
	 * going in the given direction.
	 * If no button exists in a given direction, it will be null.
	 */
	public Button toLeft, toRight, toUp, toDown;
	
	/**
	 * Create a new button
	 * @param x X location of button
	 * @param y Y location of button
	 */
	public Button(float x, float y) {
		super(x, y);
		isActive = true;
		isSelected = false;
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
	public boolean contains(Vector2 point){ return this.contains(point.x, point.y); }
	
	/** @return Width of button */
	public abstract float getWidth();
	
	/** @return Height of button */
	public abstract float getHeight();
	
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
	
	/** @return Whether or not a button is currently being held down */
	public boolean isDown(){ return isDown;}
	
	/** What to do when this button is selected */
	protected abstract void onSelect();
	
	/** Selects a button (generally, changes button rendering to tell if it's selected) */
	public void select(){
		if(this.isActive){
			this.isSelected = true;
			onSelect();
		}
	}
	
	/** What to do when a button is un-seleceted */
	protected abstract void onUnselect();
	
	/** Unselects a button */
	public void unselect(){
		if(this.isActive){
			this.isSelected = false;
			onUnselect();
		}
	}
	
	public boolean isSelected(){ return isSelected; }
	
	/** Deactivate a button */
	public void deactivate(){ isActive = false; }
	
	/** Activate a button */
	public void activate() { isActive = true; }
	
	/** @return Whether or not a button is active */
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
	
	
	/** Notify a button that it's being dragged */
	public void drag(float dx, float dy) { this.onDrag(dx, dy); }
	
	/** What to do when a button is dragged */
	protected abstract void onDrag(float dx, float dy);
}
