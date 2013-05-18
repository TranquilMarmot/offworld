package com.bitwaffle.guts.input.listeners.gui;

import java.util.Iterator;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.gui.button.Button;

/**
 * Each pointer can either be down or not down, and if it is down it's location is given by it's x and y values.
 * Each pointer can grab onto and drag a button object, which it finds in Game.gui.getButtonIterator().
 * 
 * @author TranquilMarmot
 */
public class ButtonPointer {
	/** Location of pointer, if it's down */
	public float x, y;

	/** Previous location of pointer */
	public float prevX, prevY;

	/** Whether or not this pointer is touching the screen right meow */
	public boolean isDown;

	/** Button this pointer is pressing, null if no button */
	public Button buttonDown;

	/** 
	 * ID from pointer from MotionEvent- Given a MotionEvent e, get index with e.findPointerIndex(pointerID).
	 * That index can then be used with e.getX(index) etc. to get updated values for this pointer
	 */
	public int pointerID;
	
	/**
	 * @param handler TouchHandler this pointer originated from
	 */
	public ButtonPointer(){
		x = 0.0f;
		y = 0.0f;
		prevX = 0.0f;
		prevY = 0.0f;
		isDown = false;
	}

	/**
	 * What happens when this pointer is put down on the screen
	 * @param pointerID ID of pointer going down
	 * @param x X location that pointer is put down on
	 * @param y Y location that pointer is put down on
	 */
	public void down(int pointerID, float x, float y){
		this.pointerID = pointerID;
		this.prevX = this.x;
		this.prevY = this.y;
		this.x = x;
		this.y = y;
		this.isDown = true;
		
		checkForButtonPresses();
	}

	/**
	 * Moves this pointer (drags selected button)
	 * @param newX New X location of pointer
	 * @param newY New Y location of pointer
	 */
	public void move(float newX, float newY){
		this.prevX = this.x;
		this.prevY = this.y;
		this.x = newX;
		this.y = newY;

		if(buttonDown != null){
			buttonDown.drag(this.x - this.prevX, this.y - this.prevY);
			checkForButtonPresses();
		}
	}

	/**
	 * Called when this pointer gets lifted from the screen
	 * @param x Last X location of pointer
	 * @param y Last Y location of pointer
	 */
	public void up(float x, float y){
		this.pointerID = -1;
		this.isDown = false;

		if(buttonDown != null){
			if(buttonDown.contains(x, y))
				buttonDown.release();
			else
				buttonDown.slideRelease();

			buttonDown = null;
		}
	}

	/**
	 * Checks whether a button lies underneath the given point and presses it if it does.
	 * Automatically sets buttonDown to a new button if a button is pressed.
	 * If a button is down and the finger slides onto another button,
	 * then buttonDown will change to the new button
	 * @return Whether or not a button was pressed
	 */
	protected boolean checkForButtonPresses() {
		if(Game.gui == null)
			return false;
		else{
			// check every button for presses
			Iterator<Button> it = Game.gui.getButtonIterator();
			while (it.hasNext()) {
				Button b = it.next();

				if (b != buttonDown && b.isActive() && b.isVisible() && b.contains(x, y)) {
					// handle sliding from one button to another
					if(buttonDown != null)
						buttonDown.slideRelease();
					buttonDown = b;
					b.press();
					return true;
				}
			}
			return false;
		}
	}
}