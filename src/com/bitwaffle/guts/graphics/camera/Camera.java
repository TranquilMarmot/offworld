package com.bitwaffle.guts.graphics.camera;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.entities.Entity;
import com.bitwaffle.guts.entities.dynamic.DynamicEntity;
import com.bitwaffle.guts.graphics.camera.modes.CameraMode;
import com.bitwaffle.guts.graphics.camera.modes.FollowMode;
import com.bitwaffle.guts.graphics.camera.modes.FreeMode;

/**
 * Describes how a scene should be rendered and handles following a given entity
 * 
 * @author TranquilMarmot
 */
public class Camera extends Entity {
	/** Initial values for camera */
	private static final float DEFAULT_CAMX = 0.0f, DEFAULT_CAMY = -25.0f, DEFAULT_CAMZ = 0.05f;
	
	/** Current zoom level of camera (smaller it is, the smaller everything will be rendered) */
	private float zoom;
	
	/** Minimum and maximum zoom values */
	private float minZoom = 0.008F, maxZoom = 0.08F;
	
	/** Current camera mode */
	private Modes currentMode = Modes.FOLLOW;
	
	/**
	 * Different camera modes
	 */
	public static enum Modes{
		FOLLOW(new FollowMode(100.0f, 0.0f, -0.3f, 1.0f, 1.5f)),
		FREE(new FreeMode());
		
		private CameraMode mode;
		private Modes(CameraMode mode){
			this.mode = mode;
		}
		
		public void update(float timeStep){
			mode.update(timeStep);
		}
		
		public void setCamera(Camera camera){
			mode.setCamera(camera);
		}
		
		public CameraMode getMode(){
			return mode;
		}
	}
	
	/**
	 * Create a new camera
	 * @param location Initial location to put camera at
	 * @param zoom Initial zoom to put camera at
	 */
	public Camera(){
		super();
		this.setLocation(new Vector2(DEFAULT_CAMX, DEFAULT_CAMY));
		this.setZoom(DEFAULT_CAMZ);
		
		for(Modes mode : Modes.values())
			mode.setCamera(this);
	}
	
	@Override
	public void update(float timeStep) {
		currentMode.update(timeStep);
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
		if(zoom > maxZoom)
			this.zoom = maxZoom;
		else if(zoom < minZoom)
			this.zoom = minZoom;
		else
			this.zoom = zoom;
	}
	
	
	/**
	 * Set the mode of the camera
	 * @param newMode Mode from Camera.Modes
	 */
	public void setMode(Modes newMode){
		this.currentMode = newMode;
	}
	/**
	 * @return Current mode of camera, from Camera.Modes
	 */
	public Modes currentMode(){
		return currentMode;
	}
	
	/**
	 * Set the camera to follow a new entity
	 * @param entity New target to follow
	 */
	public void setTarget(DynamicEntity entity){
		((FollowMode)Modes.FOLLOW.getMode()).setTarget(entity);
	}
	
	/**
	 * @return What this camera is currently following
	 */
	public DynamicEntity getTarget() {
		return ((FollowMode)Modes.FOLLOW.getMode()).getTarget();
	}
	
	@Override
	public void cleanup() {}
}