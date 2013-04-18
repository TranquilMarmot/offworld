package com.bitwaffle.guts.graphics.render.camera;

import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class Camera3D {
	private Vector3 location;
	
	private Quaternion rotation;
	
	private float rot;
	
	public Camera3D(){
		location = new Vector3(0.0f, 0.0f, -3.0f);
		rotation = new Quaternion(0.0f, 0.0f, 0.0f, 1.0f);
		rotation.idt();
	}
	
	public Vector3 location(){
		location.z -= 0.5f;
		return location;
	}
	
	public Quaternion rotation(){
		return rotation;
	}
}
