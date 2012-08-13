package com.bitwaffle.moguts.entities;

import java.util.ArrayList;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
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
	//private FixtureDef fixtureDef;
	private ArrayList<FixtureDef> fixtureDefs;
	private Shape shape;
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
		fixtureDefs = new ArrayList<FixtureDef>();
		fixtureDefs.add(fixtureDef);
		//this.fixtureDef = fixtureDef;
	}
	
	public DynamicEntity(BodyDef bodyDef, ArrayList<FixtureDef> fixtureDefs){
		super();
		
		this.bodyDef = bodyDef;
		this.fixtureDefs = fixtureDefs;
	}
	
	/**
	 * Create a new DynamicEntity
	 * @param bodyDef Body definition
	 * @param shape Shape of entity
	 * @param density Density of entity
	 */
	public DynamicEntity(BodyDef bodyDef, Shape shape, float density){
		super();
		
		this.bodyDef = bodyDef;
		this.shape = shape;
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
			
			if(fixtureDefs != null){
				for(FixtureDef def : fixtureDefs){
					body.createFixture(def);
					def.shape.dispose();
				}
				fixtureDefs.clear();
				fixtureDefs = null;
			} else if(shape != null){
				body.createFixture(shape, density);
				shape.dispose();
				shape = null;
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
