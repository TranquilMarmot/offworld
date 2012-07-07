package com.bitwaffle.offworld.moguts.graphics;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.offworld.moguts.entity.Entity;

public class Camera extends Entity {
	
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
