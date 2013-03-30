package com.bitwaffle.guts.input;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.Render2D;
import com.bitwaffle.guts.graphics.camera.Camera;
import com.bitwaffle.guts.util.MathHelper;

public class InputHandler implements InputProcessor {
	/** 
	 * Array of pointers, gets expanded if necessary (if number of pointers down is > length)
	 * Given a MotionEvent e,  calling e.getPointerId(e.getActionIndex()) will give you the index
	 * in this array of the action's Pointer
	 */
	private ArrayList<Pointer> pointers;
	
	/** How many pointers are currently down */
	protected int pointerCount;
	
	private boolean mouseButton0, mouseButton1;
	
	private Vector2 mouseWorldLoc;
	
	public InputHandler(){
		pointers = new ArrayList<Pointer>(3);
		pointerCount = 0;
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
		pointerCount++;
		while(pointers.size() < pointerCount)
			pointers.add(new Pointer());
		
		pointers.get(pointerID).down(pointerID, pointerX, pointerY);
		
		if(button == Buttons.LEFT)
			mouseButton0 = true;
		else if(button == Buttons.RIGHT)
			mouseButton1 = true;
		//else if(button == Buttons.MIDDLE)
		//	mouseMiddle = true;
		return false;
	}

	@Override
	public boolean touchUp(int pointerX, int pointerY, int pointerID, int button) {
		pointerCount--;
		pointers.get(pointerID).up(pointerX, pointerY);
		
		if(button == Buttons.LEFT)
			mouseButton0 = false;
		else if(button == Buttons.RIGHT)
			mouseButton1 = false;
		//else if(button == Buttons.MIDDLE)
		//	mouseMiddle = true;
		
		return false;
	}

	@Override
	public boolean touchDragged(int newX, int newY, int pointerID) {
		pointers.get(pointerID).move(newX, newY);
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		if(Game.gui.console.isOn()){
			Game.gui.console.scroll(-amount);
		}
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		if(Render2D.camera.currentMode() == Camera.Modes.FOLLOW && mouseButton1/* && !tempFree*/){
			Render2D.camera.setMode(Camera.Modes.FREE);
		} else if(Render2D.camera.currentMode() == Camera.Modes.FREE && mouseButton1/* && tempFree*/){
			//tempFree = false;
			Render2D.camera.setMode(Camera.Modes.FOLLOW);
		}
		

		
		if(!mouseButton0 && mouseButton1){
			Vector2 previous = new Vector2(mouseWorldLoc); 
			Vector2 current = MathHelper.toWorldSpace(screenX, screenY, Render2D.camera);
			//Vector2 previous = MathHelper.toWorldSpace(prevX, prevY, Render2D.camera);
	
			float dx = current.x - previous.x;
			float dy = current.y - previous.y;
	
			Vector2 camLoc = Render2D.camera.getLocation();
			camLoc.x += dx;
			camLoc.y += dy;
			Render2D.camera.setLocation(camLoc);
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
