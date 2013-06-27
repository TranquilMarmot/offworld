package com.bitwaffle.offworld.camera;

import com.bitwaffle.guts.graphics.camera.Camera;
import com.bitwaffle.guts.graphics.camera.Camera2DMode;

/**
 * Camera mode that mostly gets controlled by outside forces
 * 
 * @author TranquilMarmot
 */
public class FreeMode extends Camera2DMode {
	
	public FreeMode(Camera camera){
		super(camera);
	}
	
	@Override
	public void update(float timeStep){
		// do nothing (events handled by touch handler)
		// TODO handle those events here?!
	}
}
