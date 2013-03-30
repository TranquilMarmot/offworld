package com.bitwaffle.guts.input.listeners;

import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.graphics.Render2D;
import com.bitwaffle.guts.graphics.camera.Camera;

public class CameraGestureListener implements GestureListener {
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
}
