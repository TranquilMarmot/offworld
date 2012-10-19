package com.bitwaffle.guts.physics;

import java.util.ArrayList;

import com.bitwaffle.guts.entities.dynamic.DynamicEntity;

public class BouyancyController {
	// see here http://personal.boristhebrave.com/project/b2buoyancycontroller
	private ArrayList<DynamicEntity> ents;
	
	public BouyancyController(){
		ents = new ArrayList<DynamicEntity>();
	}
	
	public void addEntity(DynamicEntity ent){
		ents.add(ent);
	}
	
	public void removeEntity(DynamicEntity ent){
		ents.remove(ent);
	}
}
