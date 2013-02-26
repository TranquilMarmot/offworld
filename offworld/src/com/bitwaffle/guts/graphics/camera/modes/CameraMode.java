package com.bitwaffle.guts.graphics.camera.modes;

import com.bitwaffle.guts.graphics.camera.Camera;

public abstract class CameraMode {
	protected Camera camera;
	
	public abstract void update(float timeStep);
	
	public void setCamera(Camera camera){
		this.camera = camera;
	}
}
