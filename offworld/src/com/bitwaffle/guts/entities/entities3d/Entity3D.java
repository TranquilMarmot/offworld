package com.bitwaffle.guts.entities.entities3d;

import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class Entity3D {
	public Entity3DRenderer renderer;
	
	protected Vector3 location;
	
	protected Quaternion rotation;
	
	private Integer hash;
	
	private int layer;
	
	public Entity3D(Entity3DRenderer renderer, int layer, Vector3 location, Quaternion rotation){
		this.renderer = renderer;
		this.layer = layer;
		this.location = location;
		this.rotation = rotation;
	}
	
	public void update(float timeStep) {}
	
	public void cleanup(){}
	
	public Vector3 location(){
		return location;
	}
	
	public Quaternion rotation(){
		return rotation;
	}
	
	public int getLayer(){
		return layer;
	}
	
	public void setHash(int hash){
		this.hash = hash;
	}
	
	@Override
	public int hashCode(){
		if(hash == null)
			return super.hashCode();
		else
			return hash;
	}
}
