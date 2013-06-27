package com.bitwaffle.guts.input.listeners;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.bitwaffle.guts.graphics.camera.Camera;

/**
 * Controls a 2D camera
 * 
 * @author TranquilMarmot
 */
public abstract class CameraInputListener implements GestureListener, InputProcessor {
	/** Camera this listener is controlling */
	protected Camera camera;
	
	public CameraInputListener(Camera camera){
		this.camera = camera;
	}
}
