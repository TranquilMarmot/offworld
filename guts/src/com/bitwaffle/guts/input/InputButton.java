package com.bitwaffle.guts.input;


/**
 * A button that can be pressed and released
 * 
 * @author TranquilMarmot
 *
 */
public interface InputButton {
	/** Notify the key that it's been pressed */
	public void press();

	/**
	 * Notify the key that it's been released
	 */
	public void release();

	/**
	 * @return Whether or not the key is being pressed
	 */
	public boolean isPressed();

	/**
	 * @return True if this is the first call to pressedOnce since the key was pressed, else false
	 */
	public boolean pressedOnce();
	
	/**
	 * @return How long the button has been down, in milliseconds
	 */
	public long timeDown();
}