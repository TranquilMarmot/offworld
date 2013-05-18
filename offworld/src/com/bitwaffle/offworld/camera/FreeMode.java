package com.bitwaffle.offworld.camera;

import com.bitwaffle.guts.graphics.render.render2d.camera.CameraMode;

/**
 * Camera mode that mostly gets controlled by outside forces
 * 
 * @author TranquilMarmot
 */
public class FreeMode extends CameraMode {
	@Override
	public void update(float timeStep){
		// do nothing (events handled by touch handler)
		// TODO handle those events here?!
	}
}
