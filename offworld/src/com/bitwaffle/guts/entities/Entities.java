package com.bitwaffle.guts.entities;

import java.util.Iterator;
import java.util.Stack;

import com.bitwaffle.guts.entities.entities2d.Entity2D;
import com.bitwaffle.guts.entities.entities2d.Entity2DAddRequest;
import com.bitwaffle.guts.entities.entities2d.Entity2DRemoveRequest;
import com.bitwaffle.guts.entities.entities3d.Entity3D;
import com.bitwaffle.guts.entities.entities3d.Entity3DAddRequest;
import com.bitwaffle.guts.entities.entities3d.Entity3DRemoveRequest;

/**
 * Handles keeping track of which entities to render and handles
 * updating all entities
 * 
 * @author TranquilMarmot
 */
public class Entities {
	/** Number of layers of entities we have */
	public static final int NUM_LAYERS = 10;
	
	/** List of lists to keep track of everything */
	public EntityLayer[] layers;
	
	/** Used when adding entities to avoid ConcurrentModificationException */
	private Stack<Entity2DAddRequest> toAdd2D;
	private Stack<Entity3DAddRequest> toAdd3D;
	
	/** Used when removing entities to avoid ConcurrentModificationException */
	private Stack<Entity2DRemoveRequest> toRemove2D;
	private Stack<Entity3DRemoveRequest> toRemove3D;
	
	public Entities(){
		layers = new EntityLayer[NUM_LAYERS];
		for(int i = 0; i < layers.length; i++)
			layers[i] = new EntityLayer();
		
		toAdd2D = new Stack<Entity2DAddRequest>();
		toAdd3D = new Stack<Entity3DAddRequest>();
		toRemove2D = new Stack<Entity2DRemoveRequest>();
		toRemove3D = new Stack<Entity3DRemoveRequest>();
	}
	
	/** Add an entity to be rendered/updated */
	public void addEntity(Entity2D ent){
		addEntity(ent, ent.hashCode());
	}
	
	/**
	 * Add an entity with a specific hash value
	 * @param ent Entity to add
	 * @param hash Hash to add entity with
	 */
	public void addEntity(Entity2D ent, int hash){
		toAdd2D.add(new Entity2DAddRequest(ent, hash));
	}
	
	public void addEntity(Entity3D ent){
		addEntity(ent, ent.hashCode());
	}
	
	public void addEntity(Entity3D ent, int hash){
		toAdd3D.add(new Entity3DAddRequest(ent, hash));
	}
	
	/** Remove an entity from being rendered/updated */
	public void removeEntity(Entity2D ent){
		removeEntity(ent, ent.hashCode());
	}
	
	/**
	 * Remove an entity with a specific hash value
	 * @param ent Entity to remove
	 * @param hash Hash of entity to remove
	 */
	public void removeEntity(Entity2D ent, int hash){
		Entity2DRemoveRequest req = new Entity2DRemoveRequest();
		req.hash = hash;
		req.layer = ent.getLayer();
		toRemove2D.add(req);
	}
	
	public void removeEntity(Entity3D ent){
		removeEntity(ent, ent.hashCode());
	}
	
	public void removeEntity(Entity3D ent, int hash){
		Entity3DRemoveRequest req = new Entity3DRemoveRequest();
		req.hash = hash;
		req.layer = ent.getLayer();
		toRemove3D.add(req);
	}
	
	/**
	 * Update every entity
	 * @param timeStep Amount of time passed since last update, in seconds
	 */
	public void update(float timeStep){
		// check for any entities to be added
		while(!toAdd2D.isEmpty())
			handleAddRequest(toAdd2D.pop());
		while(!toAdd3D.isEmpty())
			handleAddRequest(toAdd3D.pop());
		
		// update all entities
		for(EntityLayer list : layers)
			updateLayer(list, timeStep);
		
		// check for any entities to be removed
		while(!toRemove2D.isEmpty())
			handleRemoveRequest(toRemove2D.pop());
		while(!toRemove3D.isEmpty())
			handleRemoveRequest(toRemove3D.pop());
	}
	
	/** Add a request to remove an entity from the world */
	private void handleRemoveRequest(Entity2DRemoveRequest req){
		Entity2D ent = layers[req.layer].entities2D.get(req.hash);
		if(ent != null){
			ent.cleanup();
			
			layers[req.layer].entities2D.remove(ent.hashCode());
			
			ent = null;
		}
	}
	
	private void handleRemoveRequest(Entity3DRemoveRequest req){
		Entity3D ent = layers[req.layer].entities3D.get(req.hash);
		if(ent != null){
			ent.cleanup();
			
			layers[req.layer].entities3D.remove(ent.hashCode());
			
			ent = null;
		}
	}
	
	/** Initialize an entity in the physics world */
	private void handleAddRequest(Entity2DAddRequest req){
		int layer = req.ent.getLayer();
		if(layer > NUM_LAYERS)
			layer = NUM_LAYERS;
		
		layers[layer].entities2D.put(req.hash, req.ent);
	}
	
	private void handleAddRequest(Entity3DAddRequest req){
		int layer = req.ent.getLayer();
		if(layer > NUM_LAYERS)
			layer = NUM_LAYERS;
		
		layers[layer].entities3D.put(req.hash, req.ent);
	}
	
	/**
	 * Update a list of entities
	 * @param timeStep Time passed since last update, in seconds
	 */
	private void updateLayer(EntityLayer list, float timeStep){
		for(Entity2D ent : list.entities2D.values())
			if(ent != null)
				ent.update(timeStep);
		
		for(Entity3D ent : list.entities3D.values())
			if(ent != null)
				ent.update(timeStep);
	}
	
	/** Clear every entity from this list */
	public void clear(){
		for(EntityLayer l : layers){
			l.entities2D.clear();
			l.entities3D.clear();
		}
	}
	
	/** @return Total number of entities */
	public int numEntities(){
		int count = 0;
		for(EntityLayer l : layers){
			count += l.entities2D.size();
			count += l.entities3D.size();
		}
		return count;
	}
	
	/** @return An array containing an iterator for each layer */
	public Iterator<Entity2D>[] getAll2DIterators(){
		@SuppressWarnings("unchecked")  //can't have a generic array
		Iterator<Entity2D>[] its = new Iterator[NUM_LAYERS];
		
		for(int i = 0; i < NUM_LAYERS; i++)
			its[i] = layers[i].entities2D.values().iterator();
		
		return its;
	}
	
	public Iterator<Entity3D>[] getAll3DIterators(){
		@SuppressWarnings("unchecked")  //can't have a generic array
		Iterator<Entity3D>[] its = new Iterator[NUM_LAYERS];
		
		for(int i = 0; i < NUM_LAYERS; i++)
			its[i] = layers[i].entities3D.values().iterator();
		
		return its;
	}
}
