package com.bitwaffle.moguts.graphics;

import com.badlogic.gdx.math.Vector2;
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
	
	public Camera(){
		super();
		zoom = 1.0f;
	}

	public Camera(Vector2 location) {
		super();
		this.setLocation(location);
	}
	
	public Camera(float zoom){
		super();
		this.setZoom(zoom);
	}
	
	public Camera(Vector2 location, float zoom){
		super();
		this.setLocation(location);
		this.setZoom(zoom);
	}
	
	public float getZoom(){
		return zoom;
	}
	
	public void setZoom(float zoom){
		this.zoom = zoom;
	}
}
