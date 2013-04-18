package com.bitwaffle.guts.threed;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Quaternion;
import com.bitwaffle.guts.entities.Entity;

public class Entity3D extends Entity {
	public Entity3DRenderer renderer;
	
	protected Vector3 location;
	
	protected Quaternion rotation;
	
	private Integer hash;
	
	public Entity3D(Entity3DRenderer renderer){
		this.renderer = renderer;
	}
	
	public Vector3 location(){
		return location;
	}
	
	public Quaternion rotation(){
		return rotation;
	}
}
