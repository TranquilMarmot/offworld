package com.bitwaffle.moguts.graphics;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.moguts.Game;
import com.bitwaffle.moguts.entities.Entity;

// TODO Make camera's angle work so things can be rotated
/**
 * Describes how a scene should be rendered
 * 
 * @author TranquilMarmot
 */
public class Camera extends Entity {
	/** Current zoom level of camera (smaller it is, the smaller everything will be rendered) */
	private float zoom;
	
	/** Current camera mode */
	private Modes currentMode = Modes.FOLLOW;
	
	/**
	 * Different camera modes
	 */
	public static enum Modes{
		FOLLOW(0),
		FREE(1);
		
		int mode;
		Modes(int mode){
			this.mode = mode;
		}
	}
	
	/**
	 * Create a new camera
	 */
	public Camera(){
		super();
		zoom = 1.0f;
	}

	/**
	 * Create a new camera
	 * @param location Initial location to create camera at
	 */
	public Camera(Vector2 location) {
		super();
		this.setLocation(location);
	}
	
	/**
	 * Create a new camera
	 * @param zoom Initial zoom to put camera at
	 */
	public Camera(float zoom){
		super();
		this.setZoom(zoom);
	}
	
	/**
	 * Create a new camera
	 * @param location Initial location to put camera at
	 * @param zoom Initial zoom to put camera at
	 */
	public Camera(Vector2 location, float zoom){
		super();
		this.setLocation(location);
		this.setZoom(zoom);
	}
	
	/**
	 * @return Current zoom level of this camera
	 */
	public float getZoom(){
		return zoom;
	}
	
	/**
	 * Set the zoom level
	 * @param zoom New zoom level
	 */
	public void setZoom(float zoom){
		this.zoom = zoom;
	}

	@Override
	public void update(float timeStep) {
		switch(currentMode){
		case FOLLOW:
			Vector2 playerLoc = Game.player.getLocation();
			// FIXME make this work more betterer (probably base it on screen size/zoom?)
			this.location.set(-playerLoc.x + 275.0f, -playerLoc.y + 115.0f);
			break;
		case FREE:
			// do nothing (events handles by TouchHandler)
		}
	}
	
	public void setMode(Modes newMode){
		this.currentMode = newMode;
	}
	
	public Modes currentMode(){
		return currentMode;
	}
	
	@Override
	public void render() {}
	@Override
	public void cleanup() {}
}
