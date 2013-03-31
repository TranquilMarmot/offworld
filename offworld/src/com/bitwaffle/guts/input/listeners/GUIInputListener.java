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
				Game.gui.selectRight();
				break;
			case Input.Keys.LEFT:
				Game.gui.selectLeft();
				break;
			case Input.Keys.UP:
				Game.gui.selectUp();
				break;
			case Input.Keys.DOWN:
				Game.gui.selectDown();
				break;
			case Input.Keys.ENTER:
				if(Game.gui.selectedButton != null){
					Game.gui.selectedButton.press();
					Game.gui.selectedButton.release();
				}
				break;
			}
			return true;
		}
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		
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
