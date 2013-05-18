package com.bitwaffle.guts.resources.entityinfo;

import java.util.HashMap;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * Manages entity body and fixture definitions
 * 
 * @author TranquilMarmot
 */
public class EntityInfoManager {
	/** Info about entities */
	private HashMap<String, EntityInfo> info;
	
	public EntityInfoManager(){
		info = new HashMap<String, EntityInfo>();
	}
	
	public void addEntityInfo(String name, EntityInfo entinfo){
		info.put(name, entinfo);
	}
	
	public FixtureDef getEntityFixtureDef(String name){
		return info.get(name).getFixtureDef();
	}
	
	public BodyDef getEntityBodyDef(String name){
		return info.get(name).getBodyDef();
	}
}
