package com.bitwaffle.moguts.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.bitwaffle.moguts.physics.Physics;

public class DynamicEntity extends Entity{
	public Body body;
	
	public DynamicEntity(BodyDef bodyDef, FixtureDef fixtureDef){
		super();
		
		body = Physics.world.createBody(bodyDef);
		body.createFixture(fixtureDef);
	}
	
	public DynamicEntity(BodyDef bodyDef, PolygonShape shape, float density){
		super();
		
		body = Physics.world.createBody(bodyDef);
		body.setUserData(this);
		body.createFixture(shape, density);
	}
	
	@Override
	public void update(float timeStep){
		this.location.set(body.getPosition());
		this.angle = body.getAngle();
	}
	
	@Override
	public void setLocation(Vector2 newLocation){
		this.location.set(newLocation);
		this.body.setTransform(location, this.angle);
	}
	
	@Override
	public void setAngle(float newAngle){
		this.angle = newAngle;
		this.body.setTransform(this.location, newAngle);
	}
	
	@Override
	public void render(){
		
	}
}
