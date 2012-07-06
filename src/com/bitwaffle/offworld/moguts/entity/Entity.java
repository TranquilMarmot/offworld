package com.bitwaffle.offworld.moguts.entity;

import com.badlogic.gdx.math.Vector2;

public class Entity {
	protected Vector2 location;
	protected float angle;
	
	public Entity(){
		location = new Vector2();
		angle = 0.0f;
	}
	
	public Entity(Vector2 location){
		this.location = location;
	}
	
	public void setLocation(Vector2 newLocation){
		location.set(newLocation);
	}
	
	public Vector2 getLocation(){ return location; }
	
	public void update() { }
	public void render() { }
}
