package com.bitwaffle.offworld.moguts.entity;

import com.badlogic.gdx.math.Vector2;

public class Entity {
	private Vector2 location;
	
	public Entity(){
		location = new Vector2();
	}
	
	public Entity(Vector2 location){
		this.location = location;
	}
	
	public void setLocation(Vector2 newLocation){
		location.set(newLocation);
	}
	
	public Vector2 getLocation(){ return location; }
}
