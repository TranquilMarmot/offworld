package com.bitwaffle.moguts.entities;

import android.util.SparseArray;

public class EntitiesLayers {
	private SparseArray<EntityList<Entity>> layers;
	
	public EntitiesLayers(){
		layers = new SparseArray<EntityList<Entity>>();
	}
	
	public void addEntity(Entity ent, int layer){
		EntityList<Entity> layerList = layers.get(layer);
		if(layerList == null){
			layerList = new EntityList<Entity>();
			layers.put(layer, layerList);
		}
		layerList.add(ent);
	}
	
	public void removeEntity(Entity ent, int layer){
		EntityList<Entity> layerList = layers.get(layer);
		if(layerList != null){
			layerList.remove(ent);
		}
	}
}
