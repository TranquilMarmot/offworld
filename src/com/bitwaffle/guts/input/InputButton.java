package com.bitwaffle.guts.input;


/**
 * This interface is used in {@link Keys} and {@link Buttons}, so that they can both be easily referenced in {@link KeyBindings}
 * @author TranquilMarmot
 * @see Keys
 * @see Buttons
 * @see KeyBindings
 *
 */
public interface InputButton {
	/**
	 * Notify the key that it's been pressed
	 */
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
}