package com.bitwaffle.guts.physics;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;

import com.bitwaffle.guts.entity.Entity;

/**
 * Handles keeping track of which entities to render and handles
 * updating all entities
 * 
 * @author TranquilMarmot
 */
public class Entities {
	@SuppressWarnings("serial")
	public class EntityHashMap extends HashMap<Integer, Entity>{};
	
	/** Request to add entity to world */
	public static class EntityAddRequest{
		/** Entity being added */
		public Entity ent;
		/** Hash to add entity with */
		public int hash;
		
		public EntityAddRequest(Entity ent, int hash){
			this.ent = ent;
			this.hash = hash;
			ent.setHashCode(hash);
		}
	}
	
	/** Request to remove entity from world */
	public static class EntityRemoveRequest{
		/** Layer to remove entity from */
		public int layer;
		/** Hash of entity being removed */
		public int hash;
		
		public EntityRemoveRequest(int layer, int hash){
			this.layer = layer;
			this.hash = hash;
		}
	}
	
	/** Number of layers of entities we have */
	public static final int NUM_LAYERS = 10;
	
	/** List of lists to keep track of everything */
	public EntityHashMap[] layers;
	
	/** Used when adding entities to avoid ConcurrentModificationException */
	private Stack<EntityAddRequest> toAdd2D;
	
	/** Used when removing entities to avoid ConcurrentModificationException */
	private Stack<EntityRemoveRequest> toRemove2D;
	
	public Entities(){
		layers = new EntityHashMap[NUM_LAYERS];
		for(int i = 0; i < layers.length; i++)
			layers[i] = new EntityHashMap();
		
		toAdd2D = new Stack<EntityAddRequest>();
		toRemove2D = new Stack<EntityRemoveRequest>();
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
		toAdd2D.add(new EntityAddRequest(ent, hash));
	}
	
	/** Remove an entity from being rendered/updated */
	public void removeEntity(Entity ent){
		removeEntity(ent, ent.hashCode());
	}
	
	/** Remove an entity with a specific hash value */
	public void removeEntity(Entity ent, int hash){
		toRemove2D.add(new EntityRemoveRequest(ent.getLayer(), hash));
	}
	
	/**
	 * Update every entity
	 * @param timeStep Amount of time passed since last update, in seconds
	 */
	public void update(float timeStep){
		// check for any entities to be added
		while(!toAdd2D.isEmpty())
			handleAddRequest(toAdd2D.pop());
		
		// update all entities
		for(EntityHashMap list : layers)
			updateLayer(list, timeStep);
		
		// check for any entities to be removed
		while(!toRemove2D.isEmpty())
			handleRemoveRequest(toRemove2D.pop());
	}
	
	/** Add a request to remove an entity from the world */
	private void handleRemoveRequest(EntityRemoveRequest req){
		Entity ent = layers[req.layer].get(req.hash);
		if(ent != null){
			ent.cleanup();
			
			layers[req.layer].remove(ent.hashCode());
			
			ent = null;
		}
	}
	
	/** Add an entity to be rendered */
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
	private void updateLayer(EntityHashMap list, float timeStep){
		for(Entity ent : list.values())
			if(ent != null)
				ent.update(timeStep);
	}
	
	/** Clear every entity from this list */
	public void clear(){
		for(EntityHashMap l : layers){
			l.clear();
		}
	}
	
	/** @return Total number of entities */
	public int numEntities(){
		int count = 0;
		for(EntityHashMap l : layers){
			count += l.size();
		}
		return count;
	}
	
	/** @return An array containing an iterator for each layer */
	public Iterator<Entity>[] getAll2DIterators(){
		@SuppressWarnings("unchecked")  //can't have a generic array
		Iterator<Entity>[] its = new Iterator[NUM_LAYERS];
		
		for(int i = 0; i < NUM_LAYERS; i++)
			its[i] = layers[i].values().iterator();
		
		return its;
	}
}
