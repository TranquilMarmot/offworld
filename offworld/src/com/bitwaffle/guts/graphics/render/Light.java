package com.bitwaffle.guts.graphics.render;

import com.badlogic.gdx.math.Vector3;

public class Light {
	private Vector3 location;
	private Vector3 intensity;
	
	public Light(Vector3 location, Vector3 intensity){
		this.location = location;
		this.intensity = intensity;
	}
	
	public Vector3 location(){
		return location;
	}
	
	public Vector3 intensity(){
		return intensity;
	}
}
