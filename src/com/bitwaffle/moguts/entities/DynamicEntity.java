package com.bitwaffle.moguts.entities;

import java.util.ArrayList;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.bitwaffle.moguts.graphics.render.renderers.Renderers;
import com.bitwaffle.moguts.physics.Physics;
import com.bitwaffle.moguts.util.PhysicsHelper;
import com.bitwaffle.offworld.Game;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * An {@link Entity} that can interact with the {@link Physics} world.
 * 
 * @author TranquilMarmot
 */
public class DynamicEntity extends Entity implements KryoSerializable{
	/** Body that's in the Physics world */
	public Body body;
	
	// these are all used for initialization TODO there's gotta be a better way to do this
	private BodyDef bodyDef;
	private ArrayList<FixtureDef> fixtureDefs;
	private Shape shape;
	private float density;
	private boolean isInitialized = false;
	
	public DynamicEntity(){
		super();
	}
	
	/**
	 * Create a new DynmicEntity
	 * @param bodyDef Body definition
	 * @param fixtureDef Fixture definition
	 */
	public DynamicEntity(Renderers renderer, BodyDef bodyDef, FixtureDef fixtureDef){
		super(renderer);
		
		this.bodyDef = bodyDef;
		fixtureDefs = new ArrayList<FixtureDef>();
		fixtureDefs.add(fixtureDef);
	}
	
	public DynamicEntity(Renderers renderer, BodyDef bodyDef, ArrayList<FixtureDef> fixtureDefs){
		super(renderer);
		
		this.bodyDef = bodyDef;
		this.fixtureDefs = fixtureDefs;
	}
	
	/**
	 * Create a new DynamicEntity
	 * @param bodyDef Body definition
	 * @param shape Shape of entity
	 * @param density Density of entity
	 */
	public DynamicEntity(Renderers renderer, BodyDef bodyDef, Shape shape, float density){
		super(renderer);
		
		this.bodyDef = bodyDef;
		this.shape = shape;
		this.density = density;
	}
	
	/**
	 * Create a new DynamicEntity
	 * @param body Body that this entity is using
	 */
	public DynamicEntity(Renderers renderer, Body body){
		super(renderer);
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
	public void cleanup() {
		Game.physics.world.destroyBody(body);
		body = null;
	}

	public void read(Kryo kryo, Input input) {
		this.renderer = Renderers.values()[input.readInt()];
		
		BodyDef bodyDef = kryo.readObject(input, BodyDef.class);
		
		int numFixtures = input.readInt() - 1;
		ArrayList<FixtureDef> fixtureDefs = new ArrayList<FixtureDef>();
		for(int i = 0; i < numFixtures; i++){
			fixtureDefs.add(kryo.readObject(input, FixtureDef.class));
		}
		
		this.bodyDef = bodyDef;
		this.fixtureDefs = fixtureDefs;
		
		Game.physics.addEntity(this);
	}

	public void write(Kryo kryo, Output output) {
		output.writeInt(renderer.ordinal());
		
		kryo.writeObject(output, PhysicsHelper.getBodyDef(body));
		
		ArrayList<Fixture> fixtures = body.getFixtureList();
		output.writeInt(fixtures.size());
		for(Fixture f : fixtures)
			kryo.writeObject(output, PhysicsHelper.getFixtureDef(f));
	}
}
