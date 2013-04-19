package com.bitwaffle.guts.graphics.render.render2d.camera.modes;

import com.bitwaffle.guts.graphics.render.render2d.camera.Camera2D;

/**
 * The camera always has a current mode assigned to it.
 * Each mode has a unique update method, allowing for different camera control.
 * 
 * @author TranquilMarmot
 */
public abstract class CameraMode {
	protected Camera2D camera;
	
	public abstract void update(float timeStep);
	
	public void setCamera(Camera2D camera){ this.camera = camera; }
}
