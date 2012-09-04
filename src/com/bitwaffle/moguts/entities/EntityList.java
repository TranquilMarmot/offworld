package com.bitwaffle.moguts.entities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

/**
 * A list that avoids any concurrent modification exceptions
 * 
 * @author TranquilMarmot
 *
 * @param <T> Type of <@link Entity> that this list will hold
 */
public class EntityList {
	/** List of entities */
	private ArrayList<Entity> entities;
	
	/** Used to avoid ConcurrentModificationException */
	private Stack<Entity> toAdd, toRemove;
	
	/**
	 * Create a new entity list
	 */
	public EntityList(){
		entities = new ArrayList<Entity>();
		toAdd = new Stack<Entity>();
		toRemove = new Stack<Entity>();
	}
	
	/**
	 * Add an entity to this list
	 * @param add Entity to add
	 */
	public void add(Entity add){
		toAdd.push(add);
	}
	
	/**
	 * Remove an entity from this list
	 * @param rem Entity to remove
	 */
	public void remove(Entity rem){
		toRemove.push(rem);
	}
	
	/**
	 * @return An iterator that goes through every entity in this list
	 */
	public Iterator<Entity> getIterator(){
		return entities.iterator();
	}
	
	/**
	 * Clear this list
	 */
	public void clear(){
		entities.clear();
		toAdd.clear();
		toRemove.clear();
	}
	
	/**
	 * Update every entity in this list
	 * @param timeStep Amount of time, in seconds, that's passed since last update
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
		
		while(it.hasNext()){
			Entity ent = it.next();
			
			if(ent != null){
				if(ent.removeFlag)
					toRemove.add(ent);
				else
					ent.update(timeStep);
			}
		}
	}
	
	/**
	 * @return How many entities are in this list
	 */
	public int size(){
		return entities.size();
	}
}
