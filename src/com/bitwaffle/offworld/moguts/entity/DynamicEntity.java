package com.bitwaffle.offworld.moguts.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.bitwaffle.offworld.moguts.physics.Physics;

public class DynamicEntity extends Entity{
	private Body body;
	
	public DynamicEntity(BodyDef bodyDef, FixtureDef fixtureDef){
		super();
		
		body = Physics.world.createBody(bodyDef);
		body.createFixture(fixtureDef);
	}
	
	public DynamicEntity(BodyDef bodyDef, PolygonShape shape, float density){
		super();
		
		body = Physics.world.createBody(bodyDef);
		body.createFixture(shape, density);
	}
	
	@Override
	public void update(){
		this.location.set(body.getPosition());
		this.angle = body.getAngle();
		//System.out.println("ENTITY LOC: " + location.x + " " + location.y + " " + angle);
	}
	
	@Override
	public void setLocation(Vector2 newLocation){
		this.location.set(newLocation);
		this.body.setTransform(location, this.angle);
	}
	
	@Override
	public void render(){
		
	}
}
