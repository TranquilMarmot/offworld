package com.bitwaffle.guts.entities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;

import com.bitwaffle.guts.graphics.Render2D;

/**
 * Handles keeping track of which entities to render and handles
 * updating all entities
 * 
 * @author TranquilMarmot
 */
public class Entities {
	/** Number of layers of entities we have */
	public static final int NUM_LAYERS = 10;
	
	/** Custom class so that HashMaps can be put in an array */
	@SuppressWarnings("serial")
	private class EntityHashMap extends HashMap<Integer, Entity>{}
	
	/** List of lists to keep track of everything */
	private EntityHashMap[] layers;
	
	/** Used when adding entities to avoid ConcurrentModificationException */
	private Stack<EntityAddRequest> toAdd;
	
	/** Used when removing entities to avoid ConcurrentModificationException */
	private Stack<EntityRemoveRequest> toRemove;
	
	public Entities(){
		layers = new EntityHashMap[NUM_LAYERS];
		for(int i = 0; i < layers.length; i++)
			layers[i] = new EntityHashMap();
		
		toAdd = new Stack<EntityAddRequest>();
		toRemove = new Stack<EntityRemoveRequest>();
	}
	
	/** Add an entity to be rendered/updated */
	public void addEntity(Entity ent){
		addEntity(ent, ent.hashCode());
	}
	
	/**
	 * Add an entity with a specific hash value
	 * @param ent Entity to add
	 * @param hash Hash to add entity with
	 */
	public void addEntity(Entity ent, int hash){
		toAdd.add(new EntityAddRequest(ent, hash));
	}
	
	/** Remove an entity from being rendered/updated */
	public void removeEntity(Entity ent){
		removeEntity(ent, ent.hashCode());
	}
	
	/**
	 * Remove an entity with a specific hash value
	 * @param ent Entity to remove
	 * @param hash Hash of entity to remove
	 */
	public void removeEntity(Entity ent, int hash){
		if(!toRemove.contains(ent)){
			EntityRemoveRequest req = new EntityRemoveRequest();
			req.hash = hash;
			req.layer = ent.getLayer();
			toRemove.add(req);
		}
	}
	
	/** Get an entity from a given layer/hash */
	public Entity getEntity(int layer, int hash){
		return layers[layer].get(hash);
	}
	
	/**
	 * Update every entity
	 * @param timeStep Amount of time passed since last update, in seconds
	 */
	public void update(float timeStep){
		// check for any entities to be added
		while(!toAdd.isEmpty())
			handleAddRequest(toAdd.pop());
		
		// update all entities
		for(EntityHashMap list : layers)
			updateEntityArrayList(list, timeStep);
		
		// check for any entities to be removed
		while(!toRemove.isEmpty())
			handleRemoveRequest(toRemove.pop());
	}
	
	/** Add a request to remove an entity from the world */
	private void handleRemoveRequest(EntityRemoveRequest req){
		Entity ent = layers[req.layer].get(req.hash);
		if(ent != null){
			ent.cleanup();
			
			int layer = ent.getLayer();
			if(layer > NUM_LAYERS)
				layer = NUM_LAYERS;
			
			layers[layer].remove(ent.hashCode());
			
			ent = null;
		}
	}
	
	/** Initialize an entity in the physics world */
	private void handleAddRequest(EntityAddRequest req){
		int layer = req.ent.getLayer();
		if(layer > NUM_LAYERS)
			layer = NUM_LAYERS;
		
		layers[layer].put(req.hash, req.ent);
	}
	
	/**
	 * Update a list of entities
	 * @param timeStep Time passed since last update, in seconds
	 */
	private void updateEntityArrayList(EntityHashMap list, float timeStep){
		for(Entity ent : list.values())
			if(ent != null)
				ent.update(timeStep);
	}
	
	/** Renders every entity in this Entities */
	public void renderAll(Render2D renderer){
		for(EntityHashMap l : layers)
			renderer.renderEntities(l.values().iterator());
	}
	
	/** Clear every entity from this list */
	public void clear(){
		for(EntityHashMap l : layers)
			l.clear();
	}
	
	/** @return Total number of entities */
	public int numEntities(){
		int count = 0;
		for(EntityHashMap l : layers)
			count += l.size();
		return count;
	}
	
	/** @return An array containing an iterator for each layer */
	public Iterator<Entity>[] getAllIterators(){
		@SuppressWarnings("unchecked")  //can't have a generic array
		Iterator<Entity>[] its = new Iterator[NUM_LAYERS];
		
		for(int i = 0; i < NUM_LAYERS; i++)
			its[i] = layers[i].values().iterator();
		
		return its;
	}
}
