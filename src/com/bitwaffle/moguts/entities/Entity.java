package com.bitwaffle.moguts.entities;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.moguts.entities.dynamic.DynamicEntity;
import com.bitwaffle.moguts.graphics.render.Renderers;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * Base Entity class. Every frame, each Entity that is in an Entities list has its <code>update()</code>
 * method called before having its <code>render()</code> method called.
 * Anything that is in the physics world is a {@link DynamicEntity}, anything that's just rendered and
 * doesn't interact (i.e. a background) is just an Entity.
 * 
 * @author TranquilMarmot
 * @see DynamicEntity
 */
public abstract class Entity implements KryoSerializable{
	/** Renderer used to draw this entity */
	public Renderers renderer;
	
	/** Current location of entity */
	protected Vector2 location;
	
	/** Current rotation of entity (in radians) */
	protected float angle;
	
	/** If this gets set to true, the entity is removed ASAP */
	public boolean removeFlag = false;
	
	/** What layer the entity gets rendered on */
	private int layer = 0;
	
	public Entity(){
		location = new Vector2();
		angle = 0.0f;
	}
	
	public Entity(Renderers renderer){
		this.renderer = renderer;
		location = new Vector2();
		angle = 0.0f;
	}
	
	public Entity(Renderers renderer, Vector2 location){
		this.renderer = renderer;
		this.location = location;
	}
	
	/**
	 * Set an entity's location
	 * @param newLocation New location for entity
	 */
	public void setLocation(Vector2 newLocation){
		location.set(newLocation);
	}
	
	/**
	 * Set an entity'a angle
	 * @param newAngle New angle for entity
	 */
	public void setAngle(float newAngle){
		this.angle = newAngle;
	}
	
	/** @return Current location of entity */
	public Vector2 getLocation(){ return location; }
	/** @return Current angle of entity */
	public float getAngle(){ return angle; }
	
	/**
	 * Updates the entity- this can pretty much do anything and
	 * is called every frame
	 * @param timeStep How much time has passed since last update (in seconds)
	 */
	public abstract void update(float timeStep);
	
	/**
	 * Clean up any resources this entity may have allocated
	 * (called right before the entity gets removed)
	 */
	public abstract void cleanup();
	
	/**
	 * @return Which layer this entity resides on
	 */
	public int getLayer(){
		return layer;
	}
	
	public void read(Kryo kryo, Input input){
		this.renderer = Renderers.values()[input.readInt()];
		this.location.set(kryo.readObject(input, Vector2.class));
		this.angle = input.readFloat();
	}
	
	public void write(Kryo kryo, Output output){
		output.writeInt(renderer.ordinal());
		kryo.writeObject(output, this.location);
		output.writeFloat(this.angle);
	}
}
