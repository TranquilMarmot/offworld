package com.bitwaffle.offworld.camera;

import com.bitwaffle.guts.graphics.camera.Camera;
import com.bitwaffle.guts.graphics.camera.CameraMode;

/**
 * Camera mode that mostly gets controlled by outside forces
 * 
 * @author TranquilMarmot
 */
public class FreeMode extends CameraMode {
	
	public FreeMode(Camera camera){
		super(camera);
	}
	
	@Override
	public void update(float timeStep){
		// do nothing (events handled by touch handler)
		// TODO handle those events here?!
	}
}
