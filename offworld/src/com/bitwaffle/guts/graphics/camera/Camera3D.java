package com.bitwaffle.guts.graphics.camera;

import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class Camera3D {
	private Vector3 location;
	
	private Quaternion rotation;
	
	public Camera3D(){
		location = new Vector3(0.0f, 0.0f, 0.0f);
		rotation = new Quaternion(0.0f, 0.0f, 0.0f, 1.0f);
	}
	
	public Vector3 location(){
		return location;
	}
	
	public Quaternion rotation(){
		return rotation;
	}
}
