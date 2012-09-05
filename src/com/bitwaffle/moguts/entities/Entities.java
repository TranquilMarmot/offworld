package com.bitwaffle.moguts.entities;

import java.util.Iterator;

import com.bitwaffle.moguts.graphics.render.Render2D;

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
	private EntityList[] layers;
	
	public Entities(){
		layers = new EntityList[NUM_LAYERS];
		for(int i = 0; i < layers.length; i++)
			layers[i] = new EntityList();
	}
	
	/**
	 * Add an entity to be rendered/updated
	 * @param ent
	 */
	public void addEntity(Entity ent){
		int layer = ent.getLayer();
		if(layer > NUM_LAYERS)
			layer = NUM_LAYERS;
		layers[layer].add(ent);
	}
	
	/**
	 * Remove an entity from being rendered/updated
	 * @param ent
	 */
	public void removeEntity(Entity ent){
		int layer = ent.getLayer();
		if(layer > NUM_LAYERS)
			layer = NUM_LAYERS;
		layers[layer].remove(ent);
	}
	
	/**
	 * Update every entity
	 * @param timeStep Amount of time passed since last update
	 */
	public void update(float timeStep){
		for(EntityList l : layers)
			l.update(timeStep);
	}
	
	/**
	 * Renders every entity
	 * @param renderer Renderer to render entities with
	 */
	public void renderAll(Render2D renderer){
		for(EntityList l : layers)
			renderer.renderEntities(l.getIterator());
	}
	
	/**
	 * Clear every entity from this list
	 */
	public void clear(){
		for(EntityList l : layers)
			l.clear();
	}
	
	/**
	 * @return Total number of entities
	 */
	public int numEntities(){
		int count = 0;
		for(EntityList l : layers)
			count += l.size();
		return count;
	}
	
	/**
	 * @return An array containing an iterator for each layer
	 */
	public Iterator<Entity>[] getAllIterators(){
		@SuppressWarnings("unchecked")  //can't have a generic array
		Iterator<Entity>[] its = new Iterator[NUM_LAYERS];
		
		for(int i = 0; i < NUM_LAYERS; i++)
			its[i] = layers[i].getIterator();
		
		return its;
	}
}
