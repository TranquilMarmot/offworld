package com.bitwaffle.guts.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.bitwaffle.guts.gui.console.Console;

/**
 * Grabs keys being typed and sends them to a console
 * 
 * @author TranquilMarmot
 */
public class ConsoleInputListener implements InputProcessor {
	
	/** Console this listener is controlling */
	private Console console;
	
	public ConsoleInputListener(Console console){
		this.console = console;
	}

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Input.Keys.GRAVE){
				console.toggle();
				return true;
		} else if(keycode == Input.Keys.BACKSPACE){
			if(console.isOn()){
				console.backspace();
				return true;
			}
		} else if(keycode == Input.Keys.ENTER){
			if(console.isOn()){
				console.submit();
				return true;	
			}
		} else if(keycode == Input.Keys.UP){
			if(console.isOn()){
				console.scrollHistory(-1);
				return true;
			}
		} else if(keycode == Input.Keys.DOWN){
			if(console.isOn()){
				console.scrollHistory(1);
				return true;
			}
		}
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		
		return false;
	}

	@Override
	public boolean keyTyped(char c) {
		if(!console.isOn()){
			if(c == 't'){
				console.show();
				console.autoClose = true;
				return true;
			}
			// special check for / key
			if(c == '/'){
				console.openWithSlash();
				return true;
			} else
				return false;
		} else if(console.isOn()){
			// print character to console if it's on
			if (!Character.isIdentifierIgnorable(c) &&
					!(c == '`') && !(c == '\n') && !(c == '\r')){
				console.putCharacter(c);
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
		if(console.isOn())
			console.scroll(-amount);
		return false;
	}

}
