package com.bitwaffle.moguts.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.bitwaffle.moguts.Game;
import com.bitwaffle.moguts.graphics.render.Render2D;
import com.bitwaffle.moguts.physics.Physics;

/**
 * An {@link Entity} that can interact with the {@link Physics} world.
 * 
 * @author TranquilMarmot
 */
public class DynamicEntity extends Entity{
	/** Body that's in the Physics world */
	public Body body;
	
	/**
	 * Create a new DynmicEntity and add it to the physics world
	 * @param bodyDef Body definition
	 * @param fixtureDef Fixture definition
	 */
	public DynamicEntity(BodyDef bodyDef, FixtureDef fixtureDef){
		super();
		
		body = Game.physics.world.createBody(bodyDef);
		body.createFixture(fixtureDef);
		body.setUserData(this);
	}
	
	/**
	 * Create a new DynamicEntity and add it to the physics world
	 * @param bodyDef Body definition
	 * @param shape Shape of entity
	 * @param density Density of entity
	 */
	public DynamicEntity(BodyDef bodyDef, PolygonShape shape, float density){
		super();
		
		body = Game.physics.world.createBody(bodyDef);
		body.createFixture(shape, density);
		body.setUserData(this);
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
	public void render(Render2D renderer) {}

	@Override
	public void cleanup() {
		Game.physics.world.destroyBody(body);
	}
}
