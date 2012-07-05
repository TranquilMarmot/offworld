package com.bitwaffle.offworld.moguts.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class DynamicEntity extends Entity{
	private Body body;
	
	public DynamicEntity(){
		super();
	}
	
	@Override
	public void setLocation(Vector2 newLocation){
		// TODO set 'location' and change location in physics world
	}
}
