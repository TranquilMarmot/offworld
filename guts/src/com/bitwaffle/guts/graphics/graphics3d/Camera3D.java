package com.bitwaffle.guts.graphics.graphics3d;

import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.bitwaffle.guts.Game;

/**
 * A camera with an extra dimension and some rotation
 * 
 * @author TranquilMarmot
 */
public class Camera3D {
	/** Location of camera in 3D space */
	private Vector3 location;
	
	/** Rotation of camera- where it's looking */
	private Quaternion rotation;
	
	/**
	 * Scale that 3D things get rendered at through this camera.
	 * When in orthographic mode, this will have the same effect as the 2D camera's zoom.
	 */
	private float scale;
	
	public Camera3D(){
		location = new Vector3(0.0f, 0.0f, 0.0f);
		rotation = new Quaternion(0.0f, 0.0f, 0.0f, 1.0f);
		scale = 0.5f;
		rotation.idt();
	}
	
	public void update(float timeStep){
		// follow the 2D camera
		if(Game.renderer.r2D.camera != null){
			Vector2 camLoc = Game.renderer.r2D.camera.getLocation();
			this.location.x = camLoc.x;
			this.location.y = camLoc.y;
			this.scale = Game.renderer.r2D.camera.getZoom();
		}
	}
	
	public Vector3 getLocation(){ return location; }
	
	public Quaternion getRotation(){ return rotation; }
	
	public float getScale(){ return scale; }
}
