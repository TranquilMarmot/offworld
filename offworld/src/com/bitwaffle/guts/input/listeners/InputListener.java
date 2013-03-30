package com.bitwaffle.guts.input.listeners;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.Render2D;
import com.bitwaffle.guts.gui.button.Button;
import com.bitwaffle.guts.input.Keys;
import com.bitwaffle.guts.util.MathHelper;

public class InputListener implements InputProcessor {
	/** 
	 * Array of pointers, gets expanded if necessary (if number of pointers down is > length)
	 * Given a MotionEvent e,  calling e.getPointerId(e.getActionIndex()) will give you the index
	 * in this array of the action's Pointer
	 */
	private LinkedList<Pointer> pointers;
	
	public InputListener(){
		pointers = new LinkedList<Pointer>();
	}
	
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
	public boolean keyTyped(char c) {
		if(!Game.gui.console.isOn()){
			// special check for / key FIXME maybe a better way to do this? Not so bad but pretty hack-y
			if(c == '/'){
				Game.gui.console.openWithSlash();
				return true;
			}
		} else if(Game.gui.console.isOn()){
			// print character to console if it's on
			if (!Character.isIdentifierIgnorable(c) &&
					!(c == '`') && !(c == '\n') && !(c == '\r')){
				Game.gui.console.putCharacter(c);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean touchDown(int pointerX, int pointerY, int pointerID, int button) {
		while(pointers.size() < pointerID + 1)
			pointers.add(new Pointer());
		
		pointers.get(pointerID).down(pointerID, pointerX, pointerY);
		
		return false;
	}

	@Override
	public boolean touchUp(int pointerX, int pointerY, int pointerID, int button) {
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
		if(!Game.gui.console.isOn())
			Render2D.camera.setZoom(Render2D.camera.getZoom() - (amount / 250.0f));
			
		if(Game.gui.console.isOn()){
			Game.gui.console.scroll(-amount);
		}
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// check if the mouse hovers over any buttons and select them
		if(Game.gui != null){
			// check if the mouse went off of the selected button
			if(Game.gui.selectedButton != null && !Game.gui.selectedButton.contains(screenX, screenY)){
				Game.gui.selectedButton.unselect();
				Game.gui.selectedButton = null;
			}
			
			// check every button for presses
			Iterator<Button> it = Game.gui.getButtonIterator();
			while (it.hasNext()) {
				Button b = it.next();
				
				if (b != Game.gui.selectedButton && b.isActive() && b.isVisible() && b.contains(screenX, screenY)) {
					Game.gui.selectedButton = b;
					b.select();
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Screen is being "dragged" to change where the Render2D.camera is looking
	 * @param curX X location being dragged to
	 * @param curY Y location being dragged to
	 * @param prevX Previous X location
	 * @param prevY Previous Y location
	 */
	protected void panEvent(float curX, float curY, float prevX, float prevY) {
		Vector2 current = MathHelper.toWorldSpace(curX, curY, Render2D.camera);
		Vector2 previous = MathHelper.toWorldSpace(prevX, prevY, Render2D.camera);

		float dx = current.x - previous.x;
		float dy = current.y - previous.y;

		Vector2 camLoc = Render2D.camera.getLocation();
		camLoc.x += dx;
		camLoc.y += dy;
		Render2D.camera.setLocation(camLoc);
	}
}
