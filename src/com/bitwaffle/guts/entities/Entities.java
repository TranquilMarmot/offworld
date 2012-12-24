package com.bitwaffle.guts.entities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import com.bitwaffle.guts.graphics.render.Render2D;

/**
 * Handles keeping track of which entities to render and handles
 * updating all entities
 * 
 * @author TranquilMarmot
 */
public class Entities {
	/**
	 * Had to make a custom class so that the ArrayLists could be shoved into
	 * an array
	 */
	@SuppressWarnings("serial")
	private class EntityArrayList extends ArrayList<Entity>{}
	
	/** List of lists to keep track of everything */
	private EntityArrayList[] layers;
	
	/** Used when adding/removing entities to avoid ConcurrentModificationException to ArrayLists */
	private Stack<Entity> toAdd, toRemove;
	
	/** Number of layers of entities we have */
	public static final int NUM_LAYERS = 10;
	
	public Entities(){
		layers = new EntityArrayList[NUM_LAYERS];
		for(int i = 0; i < layers.length; i++)
			layers[i] = new EntityArrayList();
		
		toAdd = new Stack<Entity>();
		toRemove = new Stack<Entity>();
	}
	
	/**
	 * Add an entity to be rendered/updated
	 * @param ent
	 */
	public void addEntity(Entity ent){
		toAdd.add(ent);
	}
	
	/**
	 * Remove an entity from being rendered/updated
	 * @param ent
	 */
	public void removeEntity(Entity ent){
		if(!toRemove.contains(ent))
			toRemove.add(ent);
	}
	
	/**
	 * Update every entity
	 * @param timeStep Amount of time passed since last update
	 */
	public void update(float timeStep){
		// check for any entities to be added
		while(!toAdd.isEmpty())
			init(toAdd.pop());
		
		// update all entities
		for(EntityArrayList list : layers)
			updateEntityArrayList(list, timeStep);
		
		// check for any entities to be removed
		while(!toRemove.isEmpty()){
			Entity ent = toRemove.pop();
			cleanup(ent);
		}
	}
	
	/**
	 * Does it's best to remove an entity from the game and free up any memory
	 * entity may have allocated
	 * @param ent Entity to get rid of
	 */
	private void cleanup(Entity ent){
		ent.cleanup();
		
		int layer = ent.getLayer();
		if(layer > NUM_LAYERS)
			layer = NUM_LAYERS;
		
		layers[layer].remove(ent);
		
		ent = null;
	}
	
	/**
	 * Initialize an entity (that is, add it to the proper layer list)
	 * @param ent Entity to add
	 */
	private void init(Entity ent){
		int layer = ent.getLayer();
		if(layer > NUM_LAYERS)
			layer = NUM_LAYERS;
		
		layers[layer].add(ent);
	}
	
	/**
	 * Update a list of entities. Removes any where removeFlag is true
	 * @param list List to update
	 * @param timeStep 
	 */
	private void updateEntityArrayList(EntityArrayList list, float timeStep){
		for(Entity ent : list)
			if(ent != null)
				ent.update(timeStep);
	}
	
	/**
	 * Renders every entity
	 * @param renderer Renderer to render entities with
	 */
	public void renderAll(Render2D renderer){
		for(EntityArrayList l : layers)
			renderer.renderEntities(l.iterator());
	}
	
	/**
	 * Clear every entity from this list
	 */
	public void clear(){
		for(EntityArrayList l : layers)
			l.clear();
	}
	
	/**
	 * @return Total number of entities
	 */
	public int numEntities(){
		int count = 0;
		for(EntityArrayList l : layers)
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
			its[i] = layers[i].iterator();
		
		return its;
	}
}
