package com.bitwaffle.guts.graphics.camera;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.physics.Room;

public abstract class Camera2DMode {
	/** Camera this mode belongs to */
	protected Camera camera;
	
	/** Current zoom level of camera (smaller it is, the smaller everything will be rendered) */
	public float zoom = 0.008f;
	
	/** Minimum and maximum zoom values */
	public float minZoom = 0.005f, maxZoom = 0.15f;

	public boolean
		/** Whether or not to interpolate the values of the camera */
		interpolate = true,
		/** Whether or not to keep the camera within the given bounds */
		boundsCheck = false;
	
	public Vector2
		/** Target that camera interpolates towards (if interpolate is true) */
		target = new Vector2(0.0f, 0.0f),
		/** Offsets for rendering */
		offset = new Vector2(0.0f, 0.0f);

	public Camera2DMode(Camera camera){
		this.camera = camera;
	}
	
	public abstract void update(float timeStep);

}
