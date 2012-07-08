package com.bitwaffle.moguts.entities;

import com.badlogic.gdx.math.Vector2;

/**
 * Base Entity class. Every frame, each Entity that is in an Entities list has its <code>update()</code>
 * method called before having its <code>render()</code> method called.
 * 
 * @author TranquilMarmot
 */
public class Entity {
	/** Current location of entity */
	protected Vector2 location;
	
	/** Current rotation of entities (in radians) */
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
	
	public void setAngle(float newAngle){
		this.angle = newAngle;
	}
	
	public Vector2 getLocation(){ return location; }
	public float getAngle(){ return angle; }
	
	public void update() { }
	public void render() { }
}
