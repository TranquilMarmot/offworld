package com.bitwaffle.moguts.gui.button;

import com.bitwaffle.moguts.gui.GUIObject;

/**
 * A button that can be pressed
 * 
 * @author TranquilMarmot
 * @see GUI
 */
public abstract class Button extends GUIObject{
	/** Whether or not the button is active/visible (can only be pressed if both are true) */
	protected boolean isActive, isVisible;
	
	/** Whether or not the button is being held down */
	protected boolean isDown, wasDown;
	
	
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

	/** What to do when the button is pressed */
	public abstract void actionPerformed();
	
	/**
	 * Check if a button has been pressed
	 * @param x
	 * @param y
	 * @return
	 */
	public abstract boolean checkForPress(float x, float y);
	
	public abstract void press();
	
	public abstract void release();
	
	public boolean isActive(){ return isActive; }
	public void deactivate(){ isActive = false; }
	public void activate(){ isActive = true; }
	
	public boolean isVisible(){ return isVisible; }
	public void hide(){ isVisible = false; }
	public void show(){ isVisible = true; }
	
	public boolean isDown(){ return isDown;}
}
