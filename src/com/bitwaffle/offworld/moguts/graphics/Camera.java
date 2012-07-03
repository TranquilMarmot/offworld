package com.bitwaffle.offworld.moguts.graphics;

import org.lwjgl.util.vector.Quaternion;

import com.bitwaffle.offworld.moguts.entity.Entity;

public class Camera extends Entity {

	private Quaternion rotation;
	
	public Camera(){
		super();
		rotation = new Quaternion(0.0f, 0.0f, 0.0f, 1.0f);
	}
	
	public Quaternion getRotation(){
		return rotation;
	}
	
}
