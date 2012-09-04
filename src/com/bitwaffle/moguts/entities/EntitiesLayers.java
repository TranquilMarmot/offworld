package com.bitwaffle.moguts.entities;

import java.util.Iterator;

import com.bitwaffle.moguts.graphics.render.Render2D;

public class EntitiesLayers {
	public static final int NUM_LAYERS = 10;
	
	private EntityList[] layers;
	
	public EntitiesLayers(){
		layers = new EntityList[NUM_LAYERS];
		for(int i = 0; i < layers.length; i++)
			layers[i] = new EntityList();
	}
	
	public void addEntity(Entity ent){
		layers[ent.getLayer()].add(ent);
	}
	
	public void removeEntity(Entity ent){
		layers[ent.getLayer()].remove(ent);
	}
	
	public void update(float timeStep){
		for(EntityList l : layers)
			l.update(timeStep);
	}
	
	public void renderAll(Render2D renderer){
		for(EntityList l : layers)
			renderer.renderEntities(l.getIterator());
	}
	
	public void clear(){
		for(EntityList l : layers)
			l.clear();
	}
	
	public int numEntities(){
		int count = 0;
		for(EntityList l : layers)
			count += l.size();
		return count;
	}
	
	public Iterator<Entity>[] getAllIterators(){
		@SuppressWarnings("unchecked")
		Iterator<Entity>[] its = new Iterator[NUM_LAYERS];
		
		for(int i = 0; i < NUM_LAYERS; i++)
			its[i] = layers[i].getIterator();
		
		return its;
	}
}
