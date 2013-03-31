package com.bitwaffle.guts.input.listeners;

import com.badlogic.gdx.InputProcessor;
import com.bitwaffle.guts.Game;

public class ConsoleInputListener implements InputProcessor {

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
		if(!Game.gui.console.isOn()){
			// special check for / key
			if(c == '/'){
				Game.gui.console.openWithSlash();
				return true;
			} else
				return false;
		} else if(Game.gui.console.isOn()){
			// print character to console if it's on
			if (!Character.isIdentifierIgnorable(c) &&
					!(c == '`') && !(c == '\n') && !(c == '\r')){
				Game.gui.console.putCharacter(c);
				return true;
			} else
				return false;
		}
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
