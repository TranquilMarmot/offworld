package com.bitwaffle.guts.input.listeners.button;

import java.util.LinkedList;

import com.badlogic.gdx.InputProcessor;
import com.bitwaffle.guts.Game;

public class ButtonInputListener implements InputProcessor {
	/** 
	 * Array of pointers, gets expanded if necessary (if number of pointers down is > length)
	 * Given a MotionEvent e,  calling e.getPointerId(e.getActionIndex()) will give you the index
	 * in this array of the action's Pointer
	 */
	private LinkedList<ButtonPointer> pointers;
	
	public ButtonInputListener(){
		pointers = new LinkedList<ButtonPointer>();
	}
	
	@Override
	public boolean keyDown(int keycode) {

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {

		return false;
	}

	@Override
	public boolean keyTyped(char c) {
		return false;
	}

	@Override
	public boolean touchDown(int pointerX, int pointerY, int pointerID, int button) {
		while(pointers.size() < pointerID + 1)
			pointers.add(new ButtonPointer());
		
		pointers.get(pointerID).down(pointerID, pointerX, pointerY);
		
		return false;
	}

	@Override
	public boolean touchUp(int pointerX, int pointerY, int pointerID, int button) {
		while(pointers.size() < pointerID + 1)
			pointers.add(new ButtonPointer());
		
		pointers.get(pointerID).up(pointerX, pointerY);
		
		return false;
	}

	@Override
	public boolean touchDragged(int newX, int newY, int pointerID) {
		pointers.get(pointerID).move(newX, newY);
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
			
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// check if the mouse hovers over any buttons and select them
		if(Game.gui != null)
			Game.gui.checkForButtonSelection(screenX, screenY);
		
		return false;
	}
}
