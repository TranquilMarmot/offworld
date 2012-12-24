package com.bitwaffle.guts.entities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import com.bitwaffle.guts.physics.Physics;

/**
 * A room represents a set of entities in a physics world. When a room is set
 * as the current room using physics.setCurrentRoom(), all of its entities get added to the current world.
 * When a room is removed as the current room, all of its entities get removed from the current world.
 * 
 * @author TranquilMarmot
 */
public abstract class Room {
	/** Bounds of room */
	private float minX, maxX, minY, maxY;
	
	/** List of entities in this room */
	private ArrayList<Entity> entities;
	
	/** Used to avoid ConcurrentModificationException */
	private Stack<Entity> entitiesToRemove, entitiesToAdd;
	
	private boolean isCurrentRoom;
	
	/**
	 * Create a new room.
	 * @param boundsMinX
	 * @param boundsMaxX
	 * @param boundsMinY
	 * @param boundsMaxY
	 */
	public Room(float boundsMinX, float boundsMaxX, float boundsMinY, float boundsMaxY){
		this.minX = boundsMinX;
		this.maxX = boundsMaxX;
		
		this.minY = boundsMinY;
		this.maxY = boundsMaxY;
		
		entities = new ArrayList<Entity>();
		entitiesToRemove = new Stack<Entity>();
		entitiesToAdd = new Stack<Entity>();
	}
	
	public void update(float timeStep){
		while(!entitiesToRemove.isEmpty())
			entities.remove(entitiesToRemove.pop());
		while(!entitiesToAdd.isEmpty())
			entities.add(entitiesToAdd.pop());
	}
	
	/**
	 * Add an entity from this room
	 * NOTE:
	 * This should NEVER be called from anywhere but from in
	 * physics.addEntity!!!! This is basically just so entities can
	 * be added to the current room
	 * @param ent Entity to add
	 */
	public void addEntity(Entity ent){
		entitiesToAdd.push(ent);
	}
	
	/**
	 * Remove an entity from this room
	 * NOTE:
	 * This should NEVER be called from anywhere but from in
	 * physics.removeEntity!!!! This is basically just so entities can
	 * be added to the current room
	 * @param ent Entity to remove
	 */
	public void removeEntity(Entity ent){
		entitiesToRemove.push(ent);
	}
	
	/**
	 * Add a room to the world
	 * @param physics World to add room to
	 */
	public void addToWorld(Physics physics){
		// clear the toAdd/toRemove stacks
		this.update(1.0f / 60.0f);
		
		Iterator<Entity> it = entities.iterator();
		while(it.hasNext())
			// don't add entities to room since.. well, they're already in here
			physics.addEntity(it.next(), false);
		
		this.isCurrentRoom = true;
		
		onAddToWorld(physics);
	}
	
	/**
	 * Remove a room from the world
	 * @param physics World to remove room from
	 */
	public void removeFromWorld(Physics physics){
		// clear the toAdd/toRemove stacks
		this.update(1.0f / 60.0f);
		
		Iterator<Entity> it = entities.iterator();
		while(it.hasNext())
			physics.removeEntity(it.next(), false);
		
		this.isCurrentRoom = false;
		
		onRemoveFromWorld(physics);
	}
	
	/**
	 * @return Whether or not this room is the current room
	 */
	public boolean isCurrentRoom(){
		return isCurrentRoom;
	}
	
	// TODO maybe just serialize everything here? or shoudl it be left up to the room?
	// perhaps it should just have a write(kryo, out) method that writes everything out!
	// in fact... these almost seem useless...
	public abstract void onAddToWorld(Physics physics);
	
	public abstract void onRemoveFromWorld(Physics physics);
	
	public float getBoundsMinX(){ return minX; }
	public float getBoundsMaxX(){ return maxX; }
	public float getBoundsMinY(){ return minY; }
	public float getBoundsMaxY(){ return maxY; }

	public int numEntities() {
		return entities.size();
	}
}
