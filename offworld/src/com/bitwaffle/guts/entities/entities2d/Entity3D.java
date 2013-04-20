package com.bitwaffle.guts.entities.entities2d;

import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Entity3D extends Entity2D {
	private float z;
	
	private Quaternion rotation;
	
	public Entity3D(EntityRenderer renderer, int layer){
		super(renderer, layer);
	}
	
	public Vector3 get3DLocation(){
		Vector2 supLoc = super.getLocation();
		return new Vector3(supLoc.x, supLoc.y, z);
	}
	
	public Quaternion getRotation(){
		return rotation;
	}
}
