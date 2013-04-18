package com.bitwaffle.guts.physics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import com.bitwaffle.guts.entities.entities2d.Entity2D;
import com.bitwaffle.guts.entities.entities3d.Entity3D;

/**
 * A room represents a set of entities in a physics world. When a room is set
 * as the current room using physics.setCurrentRoom(), all of its entities get added to the current world.
 * When a room is removed as the current room, all of its entities get removed from the current world.
 * 
 * @author TranquilMarmot
 */
public abstract class Room {
	/** Location of center of room */
	private float roomX, roomY;
	
	/** Size of room. Camera will not be allowed outside of these bounds. */
	private float roomWidth, roomHeight;
	
	/** List of entities in this room */
	private ArrayList<Entity2D> entities2D;
	
	private ArrayList<Entity3D> entities3D;
	
	/** Used to avoid ConcurrentModificationException */
	private Stack<Entity2D> entitiesToRemove2D, entitiesToAdd2D;
	
	private Stack<Entity3D> entitiesToRemove3D, entitiesToAdd3D;
	
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
		this.roomX = roomX;
		this.roomY = roomY;
		
		this.roomWidth = roomWidth;
		this.roomHeight = roomHeight;
		
		entities2D = new ArrayList<Entity2D>();
		entitiesToRemove2D = new Stack<Entity2D>();
		entitiesToAdd2D = new Stack<Entity2D>();
		
		entities3D = new ArrayList<Entity3D>();
		entitiesToRemove3D = new Stack<Entity3D>();
		entitiesToAdd3D = new Stack<Entity3D>();
	}
	
	/**
	 * Add/removes any entities that have been added/removed from this room
	 * @param timeStep Unused
	 */
	public void update(float timeStep){
		while(!entitiesToRemove2D.isEmpty())
			entities2D.remove(entitiesToRemove2D.pop());
		while(!entitiesToAdd2D.isEmpty())
			entities2D.add(entitiesToAdd2D.pop());
		
		while(!entitiesToRemove3D.isEmpty())
			entities3D.remove(entitiesToRemove3D.pop());
		while(!entitiesToAdd3D.isEmpty())
			entities3D.add(entitiesToAdd3D.pop());
	}
	
	/** Add an entity to this room */
	protected void addEntity(Entity2D ent){ entitiesToAdd2D.push(ent); }
	
	/** Remove an entity from this room */
	protected void removeEntity(Entity2D ent){ entitiesToRemove2D.push(ent); }
	
	/** Add an entity to this room */
	protected void addEntity(Entity3D ent){ entitiesToAdd3D.push(ent); }
	
	/** Remove an entity from this room */
	protected void removeEntity(Entity3D ent){ entitiesToRemove3D.push(ent); }
	
	/** Add this room to a world */
	public void addToWorld(Physics physics){
		// clear the toAdd/toRemove stacks
		this.update(1.0f / 60.0f);
		
		Iterator<Entity2D> it2d = entities2D.iterator();
		while(it2d.hasNext())
			// don't add entities to room since.. well, they're already in here
			physics.addEntity(it2d.next(), false);
		
		Iterator<Entity3D> it3d = entities3D.iterator();
		while(it3d.hasNext())
			// don't add entities to room since.. well, they're already in here
			physics.addEntity(it3d.next(), false);
		
		this.isCurrentRoom = true;
		
		onAddToWorld(physics);
	}
	
	/** Remove this room from a world */
	public void removeFromWorld(Physics physics){
		// clear the toAdd/toRemove stacks
		this.update(1.0f / 60.0f);
		
		Iterator<Entity2D> it2d = entities2D.iterator();
		while(it2d.hasNext())
			physics.removeEntity(it2d.next(), false);
		
		Iterator<Entity3D> it3d = entities3D.iterator();
		while(it3d.hasNext())
			physics.removeEntity(it3d.next(), false);
		
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
	
	/** @return X location of room's center */
	public float getRoomX(){ return roomX; }
	/** @return Y location of room's center */
	public float getRoomY(){ return roomY; }
	/** @return  Width of room, from center */
	public float getRoomWidth(){ return roomWidth; }
	/** @return Height of room, from center */
	public float getRoomHeight(){ return roomHeight; }

	/** @return Number of entities in this room*/
	public int numEntities() { return entities2D.size(); }
}
