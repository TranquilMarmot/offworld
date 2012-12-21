package com.bitwaffle.guts.physics;

import java.util.ArrayList;

import com.bitwaffle.guts.entities.dynamic.DynamicEntity;

public class BouyancyController {
	/*
	 * see
	 *  http://personal.boristhebrave.com/project/b2buoyancycontroller
	 * http://www.iforce2d.net/b2dtut/joints-overview
	 * http://code.google.com/p/box2dlights/downloads/detail?name=box2dLightTestProject.zip
	 */
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
