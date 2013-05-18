package com.bitwaffle.guts.input.listeners;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.bitwaffle.guts.graphics.render.render2d.camera.Camera2D;

/**
 * Controls a 2D camera
 * 
 * @author TranquilMarmot
 */
public abstract class CameraInputListener implements GestureListener, InputProcessor {
	/** Camera this listener is controlling */
	protected Camera2D camera;
	
	public CameraInputListener(Camera2D camera){
		this.camera = camera;
	}
}
