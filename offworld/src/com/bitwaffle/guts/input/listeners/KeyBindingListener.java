package com.bitwaffle.guts.input.listeners;

import java.lang.reflect.Field;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.bitwaffle.guts.input.Keys;

/**
 * Presses and releases things in {@link Keys}
 * 
 * @author TranquilMarmot
 */
public class KeyBindingListener implements InputProcessor {
	
	/**
	 * Gets a key from Keys enum from a keycode from LibGDX's Input.Keys
	 * @param keycode Keycode to get key for (from Input.Keys)
	 * @return Value from Keys enum representing given key code
	 */
	private static Keys getKey(int keycode){
		// super java hax galore (aka people need to learn to use enums)
		for (Field f : Input.Keys.class.getDeclaredFields()) {
			try {
				int i = f.getInt(f);
				// this only returns the first key it finds... if two keys have the same key code, we could run into some issues!
				if (i == keycode)
					return Keys.valueOf(f.getName());
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}

	@Override
	public boolean keyDown(int keycode) {
		// just press da key
		Keys key = getKey(keycode);
		key.press();
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// just release da key
		Keys key = getKey(keycode);
		key.release();
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
