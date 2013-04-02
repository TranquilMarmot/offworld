package com.bitwaffle.guts.input.listeners;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.gui.GUI;

public class GUIInputListener implements InputProcessor {

	@Override
	public boolean keyDown(int keycode) {
		if(!Game.gui.isCurrentState(GUI.States.NONE)){
			switch(keycode){
			case Input.Keys.RIGHT:
				Game.gui.moveRight();
				break;
			case Input.Keys.LEFT:
				Game.gui.moveLeft();
				break;
			case Input.Keys.UP:
				Game.gui.moveUp();
				break;
			case Input.Keys.DOWN:
				Game.gui.moveDown();
				break;
			case Input.Keys.ENTER:
				Game.gui.selectedButtonDown();
				break;
			}
			return true;
		}
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		
		switch(keycode){
		case Input.Keys.ENTER:
			Game.gui.selectedButtonUp();
		}
		
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		
		return false;
	}

}