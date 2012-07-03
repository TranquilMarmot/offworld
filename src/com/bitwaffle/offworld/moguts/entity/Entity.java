package com.bitwaffle.offworld.moguts.entity;

import org.lwjgl.util.vector.Vector2f;

public class Entity {
	private Vector2f location;
	
	public Entity(){
		location = new Vector2f();
	}
	
	public Entity(Vector2f location){
		this.location = location;
	}
	
	public Vector2f getLocation(){ return location; }
}
