package com.bitwaffle.guts.input.listeners;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.Render2D;
import com.bitwaffle.guts.graphics.camera.Camera;
import com.bitwaffle.guts.util.MathHelper;

public class CameraGestureListener implements GestureListener, InputProcessor {
	/** How sensitive zoom is- the higher the value, the less sensitive */
	public final float ZOOM_SENSITIVITY = 1500.0f;

	/** How much two fingers have to move from each other before zooming occurs */
	public final float ZOOM_THRESHOLD = 3.0f;
	
	@Override
	public boolean zoom(float prevSpacing, float spacing) {
		if(Render2D.camera.currentMode() == Camera.Modes.FREE){
			float ds = spacing - prevSpacing;

			// only zoom if the amount is in the threshold
			if(ds > ZOOM_THRESHOLD || ds < -ZOOM_THRESHOLD){
				float zoom = Render2D.camera.getZoom();

				zoom += (ds * zoom) / ZOOM_SENSITIVITY;

				Render2D.camera.setZoom(zoom);
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

		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {

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
	
	// TODO move the camera around when it's in free mode

	@Override
	public boolean keyDown(int keycode) {
		
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
			Render2D.camera.setZoom(Render2D.camera.getZoom() - (amount / 250.0f));
		
		return false;
	}
}
