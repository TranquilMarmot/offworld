package com.bitwaffle.guts.graphics.camera.modes;

import com.bitwaffle.guts.graphics.camera.Camera;

/**
 * The camera always has a current mode assigned to it.
 * Each mode has a unique update method, allowing for different camera control.
 * 
 * @author TranquilMarmot
 */
public abstract class CameraMode {
	protected Camera camera;
	
	public abstract void update(float timeStep);
	
	public void setCamera(Camera camera){ this.camera = camera; }
}
