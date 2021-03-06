package com.bitwaffle.guts.physics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import com.bitwaffle.guts.entity.Entity;

/**
 * A room represents a set of entities in a physics world. When a room is set
 * as the current room using physics.setCurrentRoom(), all of its entities get added to the current world.
 * When a room is removed as the current room, all of its entities get removed from the current world.
 * 
 * @author TranquilMarmot
 */
public abstract class Room {
	/** List of entities in this room */
	private ArrayList<Entity> entities;
	
	/** Used to avoid ConcurrentModificationException */
	private Stack<Entity> entitiesToRemove, entitiesToAdd;
	
	private ArrayList<Entity> staticGeometry;
	
	private Stack<Entity> geometryToRemove, geometryToAdd;
	
	/** Whether or not this room object is the current room */
	private boolean isCurrentRoom;
	
	/**
	 * Info on the room's location is used to keep the camera
	 * inside of the room (the camera may never leave the room) 
	 * @param roomX X location of room center
	 * @param roomY Y location of room center
	 * @param roomWidth Width of room from center
	 * @param roomHeight Height of room from center
	 */
	public Room(float roomX, float roomY, float roomWidth, float roomHeight){
		entities = new ArrayList<Entity>();
		entitiesToRemove = new Stack<Entity>();
		entitiesToAdd = new Stack<Entity>();
		
		staticGeometry = new ArrayList<Entity>();
		geometryToRemove = new Stack<Entity>();
		geometryToAdd = new Stack<Entity>();
	}
	
	/**
	 * Add/removes any entities that have been added/removed from this room
	 * @param timeStep Unused
	 */
	public void update(float timeStep){
		while(!entitiesToRemove.isEmpty())
			entities.remove(entitiesToRemove.pop());
		while(!entitiesToAdd.isEmpty())
			entities.add(entitiesToAdd.pop());
		while(!geometryToAdd.isEmpty())
			staticGeometry.add(geometryToAdd.pop());
		while(!geometryToRemove.isEmpty())
			staticGeometry.remove(geometryToRemove.pop());
	}
	
	/** Add an entity to this room */
	protected void addEntity(Entity ent){ entitiesToAdd.push(ent); }
	
	/** Remove an entity from this room */
	protected void removeEntity(Entity ent){ entitiesToRemove.push(ent); }
	
	protected void addGeometry(Entity geom){ geometryToAdd.push(geom); }
	
	protected void removeGeometry(Entity geom){ geometryToRemove.push(geom); }
	
	/** Add this room to a world */
	public void addToWorld(Physics physics){
		// clear the toAdd/toRemove stacks
		this.update(1.0f / 60.0f);
		
		Iterator<Entity> it = entities.iterator();
		while(it.hasNext())
			// don't add entities to room since.. well, they're already in here
			physics.addEntity(it.next(), false);
		
		Iterator<Entity> geom = staticGeometry.iterator();
		while(geom.hasNext())
			physics.addEntity(geom.next(), false);
		
		this.isCurrentRoom = true;
		
		onAddToWorld(physics);
	}
	
	/** Remove this room from a world */
	public void removeFromWorld(Physics physics){
		// clear the toAdd/toRemove stacks
		this.update(1.0f / 60.0f);
		
		Iterator<Entity> it2d = entities.iterator();
		while(it2d.hasNext())
			physics.removeEntity(it2d.next(), false);
		
		Iterator<Entity> geom = staticGeometry.iterator();
		while(geom.hasNext())
			physics.removeEntity(geom.next(), false);
		
		this.isCurrentRoom = false;
		
		onRemoveFromWorld(physics);
	}
	
	/** @return Whether or not this room is the current room */
	public boolean isCurrentRoom(){ return isCurrentRoom; }
	
	// TODO maybe just serialize everything here? or should it be left up to the room?
	// perhaps it should just have a write(kryo, out) method that writes everything out!
	/**
	 * Any extra stuff to do when adding this room to the physics world.
	 * All of the entities in the room are added to the world, then this method is called.
	 * @param physics Physics world that room is being added to
	 */
	public abstract void onAddToWorld(Physics physics);
	
	/**
	 * Any extra stuff to do when removing this room from the physics world.
	 * All of the entities in the room are removed from the world, then this method is called.
	 * @param physics Physics world that room is being remvoed from
	 */
	public abstract void onRemoveFromWorld(Physics physics);

	/** @return Number of entities in this room*/
	public int numEntities() { return entities.size(); }
	
	public Iterator<Entity> getStaticGeometryIterator(){ return staticGeometry.iterator(); }
}
