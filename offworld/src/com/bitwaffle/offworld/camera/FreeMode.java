package com.bitwaffle.offworld.camera;

import com.bitwaffle.guts.graphics.graphics2d.Camera2D;
import com.bitwaffle.guts.graphics.graphics2d.Camera2DMode;

/**
 * Camera mode that mostly gets controlled by outside forces
 * 
 * @author TranquilMarmot
 */
public class FreeMode extends Camera2DMode {
	
	public FreeMode(Camera2D camera){
		super(camera);
	}
	
	@Override
	public void update(Camera2D camera, float timeStep){
		// do nothing (events handled by touch handler)
		// TODO handle those events here?!
	}
}
