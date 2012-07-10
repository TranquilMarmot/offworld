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
	private boolean active, visible;
	
	/** Whether or not the button is being held down */
	private boolean isDown;
	
	
	/**
	 * Create a new button
	 * @param x X location of button
	 * @param y Y location of button
	 */
	public Button(float x, float y) {
		super(x, y);
		active = true;
		visible = true;
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
	
	public boolean isActive(){ return active; }
	public void deactivate(){ active = false; }
	public void activate(){ active = true; }
	
	public boolean isVisible(){ return visible; }
	public void hide(){ visible = false; }
	public void show(){ visible = true; }
	
	public boolean isDown(){ return isDown;}
}
