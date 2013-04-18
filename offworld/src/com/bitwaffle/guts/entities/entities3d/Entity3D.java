package com.bitwaffle.guts.entities.entities3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class Entity3D {
	public Entity3DRenderer renderer;
	
	protected Vector3 location;
	
	protected Quaternion rotation;
	private float rot = 0.0f; // FIXME temp
	
	private Integer hash;
	
	private int layer;
	
	public Entity3D(Entity3DRenderer renderer, int layer, Vector3 location, Quaternion rotation){
		this.renderer = renderer;
		this.layer = layer;
		this.location = location;
		this.rotation = rotation;
	}
	
	public void update(float timeStep) {
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			rotation.setEulerAngles(0.0f, rot, 0.0f);
			rot -= 0.85f;
		}
	}
	
	public void cleanup(){}
	
	public Vector3 location(){
		//location.z -= 0.5f;
		System.out.println(location);
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
