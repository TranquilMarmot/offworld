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
		//this.location.x += 0.0005;
		if(Game.players[0] != null){
			Vector2 camLoc = Game.renderer.render2D.camera.getLocation();
			this.location.x = camLoc.x;
			this.location.y = camLoc.y;
			System.out.println("cam3d loc: " + this.location);
		}
		
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
	
	public float scale(){
		return scale;
	}
}
