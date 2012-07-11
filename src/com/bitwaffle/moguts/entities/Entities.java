package com.bitwaffle.moguts.entities;

import java.util.Iterator;

/**
 * Keeps track of every Entity and DynamicEntity,
 * mostly used to render everything
 * 
 * @author TranquilMarmot
 * @see Entity
 * @see DynamicEntity
 */
public class Entities {
	private EntityList<Entity> passiveEntities;
	private EntityList<DynamicEntity> dynamicEntities;
	
	/**
	 * Initialize entity lists
	 */
	public Entities(){
		passiveEntities = new EntityList<Entity>();
		dynamicEntities = new EntityList<DynamicEntity>();
	}
	
	/**
	 * Add an Entity to the list
	 * @param ent Entity to add
	 */
	public void addPassiveEntity(Entity ent){
		passiveEntities.add(ent);
	}
	
	public void addDynamicEntity(DynamicEntity ent){
		dynamicEntities.add(ent);
	}
	
	/**
	 * Remove an Entity from the list
	 * @param ent
	 */
	public void removePassiveEntity(Entity ent){
		passiveEntities.remove(ent);
	}
	
	public void removeDynamicEntity(DynamicEntity ent){
		dynamicEntities.remove(ent);
	}
	
	/**
	 * Get an iterator to go through every entity
	 * @return Iterator of list of entities
	 */
	public Iterator<Entity> getPassiveEntityIterator(){
		return passiveEntities.getIterator();
	}
	
	public Iterator<DynamicEntity> getDynamicEntityIterator(){
		return dynamicEntities.getIterator();
	}
	
	public void clear(){
		passiveEntities.clear();
		dynamicEntities.clear();
	}
	
	/**
	 * Update every entity
	 * @param timeStep How much time has passed since last update
	 */
	public void update(float timeStep){
		passiveEntities.update(timeStep);
		dynamicEntities.update(timeStep);
	}
}
