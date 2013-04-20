package com.bitwaffle.guts.graphics.render.render3d;

import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.bitwaffle.guts.Game;

public class Camera3D {
	private Vector3 location;
	
	private Quaternion rotation;
	
	private float scale;
	
	public Camera3D(){
		location = new Vector3(0.0f, 0.0f, 0.0f);
		rotation = new Quaternion(0.0f, 0.0f, 0.0f, 1.0f);
		scale = 0.5f;
		rotation.idt();
	}
	
	public void update(float timeStep){
		if(Game.renderer.render2D.camera != null){
			Vector2 camLoc = Game.renderer.render2D.camera.getLocation();
			this.location.x = camLoc.x;
			this.location.y = camLoc.y;
			this.scale = Game.renderer.render2D.camera.getZoom();
		}
	}
	
	public Vector3 getLocation(){
		return location;
	}
	
	public Quaternion getRotation(){
		return rotation;
	}
	
	public float getScale(){
		return scale;
	}
}
