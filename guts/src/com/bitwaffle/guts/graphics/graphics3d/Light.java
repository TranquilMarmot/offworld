package com.bitwaffle.guts.graphics.graphics3d;

import com.badlogic.gdx.math.Vector3;

/**
 * A light used for 3D rendering
 * 
 * @author TranquilMarmot
 */
public class Light {
	private Vector3 location, intensity;
	
	public Light(Vector3 location, Vector3 intensity){
		this.location = location;
		this.intensity = intensity;
	}
	
	public Vector3 location(){ return location; }
	
	public Vector3 intensity(){ return intensity; }
}
