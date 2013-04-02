package com.bitwaffle.guts.input.listeners;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.camera.Camera;
import com.bitwaffle.guts.util.MathHelper;

public class CameraInputListener implements GestureListener, InputProcessor {
	/** Camera this listener is controlling */
	private Camera camera;
	
	/** How sensitive zoom is- the higher the value, the less sensitive */
	private final float ZOOM_SENSITIVITY = 1500.0f;

	/** How much two fingers have to move from each other before zooming occurs */
	private final float ZOOM_THRESHOLD = 3.0f;
	
	public CameraInputListener(Camera camera){
		this.camera = camera;
	}
	
	@Override
	public boolean zoom(float prevSpacing, float spacing) {
		if(camera.currentMode() == Camera.Modes.FREE){
			float ds = spacing - prevSpacing;

			// only zoom if the amount is in the threshold
			if(ds > ZOOM_THRESHOLD || ds < -ZOOM_THRESHOLD){
				float zoom = camera.getZoom();

				zoom += (ds * zoom) / ZOOM_SENSITIVITY;

				camera.setZoom(zoom);
			}
		}
		return false;
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {

		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {

		return false;
	}

	@Override
	public boolean longPress(float x, float y) {

		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {

		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		if(camera.currentMode() == Camera.Modes.FREE){
			Vector2 current = MathHelper.toWorldSpace(x + deltaX, y + deltaY, camera);
			Vector2 previous = MathHelper.toWorldSpace(x, y, camera);

			float dx = current.x - previous.x;
			float dy = current.y - previous.y;

			Vector2 camLoc = camera.getLocation();
			camLoc.x += dx;
			camLoc.y += dy;
			camera.setLocation(camLoc);
		}

		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
	
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
		switch(keycode){
		case Input.Keys.C:
			if(camera.currentMode() == Camera.Modes.FOLLOW)
				camera.setMode(Camera.Modes.FREE);
			else if(camera.currentMode() == Camera.Modes.FREE)
				camera.setMode(Camera.Modes.FOLLOW);
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
		if(!Game.gui.console.isOn())
			camera.setZoom(camera.getZoom() - (amount / 250.0f));
		
		return false;
	}
}