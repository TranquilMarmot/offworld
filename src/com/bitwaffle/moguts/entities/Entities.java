package com.bitwaffle.moguts.entities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

/**
 * Keeps track of every Entity and DynamicEntity,
 * mostly used to render everything
 * 
 * @author TranquilMarmot
 * @see Entity
 * @see DynamicEntity
 */
public class Entities {
	/** List of entities */
	private ArrayList<Entity> entities;
	
	/** Used to add/remove entities to avoid ConcurrentModificationException */
	private Stack<Entity> toAdd, toRemove;
	
	/**
	 * Initialize entity lists
	 */
	public Entities(){
		entities = new ArrayList<Entity>();
		toAdd = new Stack<Entity>();
		toRemove = new Stack<Entity>();
	}
	
	/**
	 * Add an Entity to the list
	 * @param ent Entity to add
	 */
	public void addEntity(Entity ent){
		toAdd.add(ent);
	}
	
	/**
	 * Remove an Entity from the list
	 * @param ent
	 */
	public void removeEntity(Entity ent){
		toRemove.add(ent);
	}
	
	/**
	 * Get an iterator to go through every entity
	 * @return Iterator of list of entities
	 */
	public Iterator<Entity> getIterator(){
		return entities.iterator();
	}
	
	/**
	 * Update every entity
	 * @param timeStep How much time has passed since last update
	 */
	public void update(float timeStep){
		while(!toRemove.isEmpty()){
			Entity ent = toRemove.pop();
			ent.cleanup();
			entities.remove(ent);
		}
		
		while(!toAdd.isEmpty())
			entities.add(toAdd.pop());
		
		Iterator<Entity> it = getIterator();
		
		while(it.hasNext())
			it.next().update(timeStep);
	}
}
