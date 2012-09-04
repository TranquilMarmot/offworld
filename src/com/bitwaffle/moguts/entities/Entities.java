package com.bitwaffle.moguts.entities;

import java.util.Iterator;

import com.bitwaffle.moguts.entities.dynamic.DynamicEntity;
import com.bitwaffle.moguts.graphics.render.Render2D;

/**
 * Keeps track of every Entity and DynamicEntity,
 * mostly used to render everything
 * 
 * @author TranquilMarmot
 * @see Entity
 * @see DynamicEntity
 */
public class Entities {
	private EntityList passiveEntities;
	private EntityList dynamicEntities;
	
	/**
	 * Initialize entity lists
	 */
	public Entities(){
		passiveEntities = new EntityList();
		dynamicEntities = new EntityList();
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
	 * Get an iterator to go through every passive entity
	 * @return Iterator of list of passive entities
	 */
	public Iterator<Entity> getPassiveEntityIterator(){
		return passiveEntities.getIterator();
	}
	
	/**
	 * Get an iterator to go through every dynamic entity
	 * @return Iterator of list of dynamic entities
	 */
	public Iterator<Entity> getDynamicEntityIterator(){
		return dynamicEntities.getIterator();
	}
	
	/**
	 * Get rid of all Entities
	 */
	public void clear(){
		passiveEntities.clear();
		dynamicEntities.clear();
	}
	
	/**
	 * Update every entity
	 * @param timeStep How much time has passed since last update
	 */
	public void update(float timeStep){
		if(Render2D.camera != null)
			Render2D.camera.update(timeStep);
		passiveEntities.update(timeStep);
		dynamicEntities.update(timeStep);
	}
	
	public int numDynamicEntities(){
		return dynamicEntities.size();
	}
}
