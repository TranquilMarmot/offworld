package com.bitwaffle.moguts.entities;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.bitwaffle.moguts.graphics.render.Render2D;
import com.bitwaffle.moguts.physics.Physics;
import com.bitwaffle.offworld.Game;

/**
 * An {@link Entity} that can interact with the {@link Physics} world.
 * 
 * @author TranquilMarmot
 */
public abstract class DynamicEntity extends Entity{
	/** Body that's in the Physics world */
	public Body body;
	
	// these are all used for initialization TODO there's gotta be a better way to do this
	private BodyDef bodyDef;
	private FixtureDef fixtureDef;
	private PolygonShape polyShape;
	private float density;
	private boolean isInitialized = false;
	
	/**
	 * Create a new DynmicEntity
	 * @param bodyDef Body definition
	 * @param fixtureDef Fixture definition
	 */
	public DynamicEntity(BodyDef bodyDef, FixtureDef fixtureDef){
		super();
		
		this.bodyDef = bodyDef;
		this.fixtureDef = fixtureDef;
	}
	
	/**
	 * Create a new DynamicEntity
	 * @param bodyDef Body definition
	 * @param shape Shape of entity
	 * @param density Density of entity
	 */
	public DynamicEntity(BodyDef bodyDef, PolygonShape shape, float density){
		super();
		
		this.bodyDef = bodyDef;
		this.polyShape = shape;
		this.density = density;
	}
	
	/**
	 * Create a new DynamicEntity
	 * @param body Body that this entity is using
	 */
	public DynamicEntity(Body body){
		super();
		this.body = body;
		body.setUserData(this);
		this.isInitialized = true;
	}
	
	/**
	 * Initialize this DynamicEntity and add it to the Physics world
	 */
	public void init(){
		if(!this.isInitialized){
			body = Game.physics.world.createBody(bodyDef);
			body.setUserData(this);
			
			if(fixtureDef != null){
				body.createFixture(fixtureDef);
				fixtureDef.shape.dispose();
				fixtureDef = null;
			} else if(polyShape != null){
				body.createFixture(polyShape, density);
				polyShape.dispose();
				polyShape = null;
			} else{
				Log.wtf("ohshit", "DynamicEntity not given enough parameters to initialize physics info!");
			}
			
			isInitialized = true;
		}
	}
	
	/**
	 * @return Whether or not this DynamicEntity has been added to the Physics world yet
	 */
	public boolean isInitialized(){
		return this.isInitialized;
	}
	
	@Override
	public void update(float timeStep){
		if(this.isInitialized){
			this.location.set(body.getPosition());
			this.angle = body.getAngle();
		}
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
	public abstract void render(Render2D renderer);

	@Override
	public void cleanup() {
		Game.physics.world.destroyBody(body);
		body = null;
	}
}
