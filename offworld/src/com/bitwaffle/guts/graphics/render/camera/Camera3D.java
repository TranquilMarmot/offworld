package com.bitwaffle.guts.graphics.render.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class Camera3D {
	private Vector3 location;
	
	private Quaternion rotation;
	
	private float rot;
	
	public Camera3D(){
		location = new Vector3(0.0f, 0.0f, -1.0f);
		rotation = new Quaternion(0.0f, 0.0f, 0.0f, 1.0f);
		rotation.idt();
	}
	
	public void update(){

			
	}
	
	public Vector3 location(){
		//location.z -= 0.5f;
		//location.x += 0.000005f;
		//System.out.println(location);
		return location;
	}
	
	public Quaternion rotation(){
		return rotation;
	}
}
