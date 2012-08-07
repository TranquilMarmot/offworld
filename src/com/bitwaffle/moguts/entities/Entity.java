package com.bitwaffle.moguts.entities;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.moguts.graphics.render.Render2D;

/**
 * Base Entity class. Every frame, each Entity that is in an Entities list has its <code>update()</code>
 * method called before having its <code>render()</code> method called.
 * Anything that is in the physics world is a {@link DynamicEntity}, anything that's just rendered and
 * doesn't interact (i.e. a background) is just an Entity.
 * 
 * @author TranquilMarmot
 * @see DynamicEntity
 */
public abstract class Entity {
	/** Current location of entity */
	protected Vector2 location;
	
	/** Current rotation of entity (in radians) */
	protected float angle;
	
	/** If this gets set to true, the entity is removed ASAP */
	public boolean removeFlag = false;
	
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
	
	public abstract void update(float timeStep);
	public abstract void render(Render2D renderer);
	public abstract void cleanup();
}
