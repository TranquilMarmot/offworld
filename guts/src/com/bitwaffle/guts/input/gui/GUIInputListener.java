package com.bitwaffle.guts.input.gui;

import java.util.LinkedList;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.gui.GUI;

public class GUIInputListener implements InputProcessor {
	
	/** Array of pointers, gets expanded if necessary (if number of pointers down is > length) */
	private LinkedList<ButtonPointer> pointers;
	
	/** GUI this listener is controlling */
	protected GUI gui;
	
	public GUIInputListener(GUI gui){
		this.gui = gui;
		pointers = new LinkedList<ButtonPointer>();
	}

	@Override
	public boolean keyDown(int keycode) {
		switch(keycode){
		case Input.Keys.RIGHT:
			gui.moveRight();
			break;
		case Input.Keys.LEFT:
			gui.moveLeft();
			break;
		case Input.Keys.UP:
			gui.moveUp();
			break;
		case Input.Keys.DOWN:
			gui.moveDown();
			break;
		case Input.Keys.ENTER:
			gui.selectedButtonDown();
			break;
		case Input.Keys.BACKSPACE:
			gui.goToPreviousState();
			break;
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		
		switch(keycode){
		case Input.Keys.ENTER:
			gui.selectedButtonUp();
			return true;
		}
		
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		
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
	public boolean mouseMoved(int screenX, int screenY) {
		// check if the mouse hovers over any buttons and select them
		if(Game.gui != null)
			Game.gui.checkForButtonSelection(screenX, screenY);
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		
		return false;
	}

}
