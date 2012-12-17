package com.bitwaffle.guts.graphics;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.entities.Entity;
import com.bitwaffle.guts.entities.dynamic.DynamicEntity;

/**
 * Describes how a scene should be rendered
 * 
 * @author TranquilMarmot
 */
public class Camera extends Entity {
	/** Current zoom level of camera (smaller it is, the smaller everything will be rendered) */
	private float zoom;
	
	/** Minimum and maximum zoom values */
	private static final float MIN_ZOOM = 0.008F, MAX_ZOOM = 0.08F;
	
	/** Current camera mode */
	private Modes currentMode = Modes.FOLLOW;
	
	/** What this camera's looking at if it's in FOLLOW mode*/
	private DynamicEntity target;
	
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
		super(null, 0);
		this.setLocation(location);
	}
	
	/**
	 * Create a new camera
	 * @param zoom Initial zoom to put camera at
	 */
	public Camera(float zoom){
		super(null, 0);
		this.setZoom(zoom);
	}
	
	/**
	 * Create a new camera
	 * @param location Initial location to put camera at
	 * @param zoom Initial zoom to put camera at
	 */
	public Camera(Vector2 location, float zoom){
		super(null, 0);
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
		if(zoom > MAX_ZOOM)
			this.zoom = MAX_ZOOM;
		else if(zoom < MIN_ZOOM)
			this.zoom = MIN_ZOOM;
		else{
			this.zoom = zoom;
			if(this.currentMode == Modes.FOLLOW && target != null)
				followTarget();
		}
	}

	@Override
	public void update(float timeStep) {
		switch(currentMode){
		case FOLLOW:
			if(target != null)
				followTarget();
			//else
			//	target = Game.player;
			break;
		case FREE:
			// do nothing (events handled by TouchHandler)
		}
	}
	
	private void followTarget(){
		Vector2 targetLoc = target.getLocation();
		//FIXME make this work more betterer (probably base it on screen size/zoom?)
		this.location.set(-targetLoc.x + (0.7f / zoom), -targetLoc.y + (0.35f / zoom));
	}
	
	public void setTarget(DynamicEntity entity){
		this.target = entity;
	}
	
	/**
	 * @return What this camera is currently following
	 */
	public DynamicEntity getTarget() {
		return target;
	}
	
	public void setMode(Modes newMode){
		this.currentMode = newMode;
	}
	
	public Modes currentMode(){
		return currentMode;
	}
	
	@Override
	public void cleanup() {}
}