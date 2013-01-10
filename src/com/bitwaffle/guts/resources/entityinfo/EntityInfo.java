package com.bitwaffle.guts.resources.entityinfo;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class EntityInfo {
	private FixtureDef fixDef;
	private BodyDef bodDef;
	
	public EntityInfo(FixtureDef fixDef, BodyDef bodDef){
		this.fixDef = fixDef;
		this.bodDef = bodDef;
	}
	
	public FixtureDef getFixtureDef(){
		return fixDef;
	}
	
	public BodyDef getBodyDef(){
		return bodDef;
	}
}
