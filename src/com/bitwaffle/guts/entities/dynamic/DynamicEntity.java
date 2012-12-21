package com.bitwaffle.guts.entities.dynamic;

import java.util.ArrayList;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.bitwaffle.guts.entities.Entity;
import com.bitwaffle.guts.graphics.render.EntityRenderer;
import com.bitwaffle.guts.physics.Physics;
import com.bitwaffle.guts.util.PhysicsHelper;
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
	
	// these are all used for initialization
	private BodyDef bodyDef;
	private ArrayList<FixtureDef> fixtureDefs;
	private Shape shape;
	private float density;
	private boolean isInitialized = false;
	
	/**
	 * No-args constructor for kryo only!
	 */
	public DynamicEntity(){
		super();
	}
	
	/**
	 * Create a new DynmicEntity with one fixture
	 * @param renderer Renderer to use to draw entity
	 * @param layer Layer to draw entity on
	 * @param bodyDef Body definition
	 * @param fixtureDef Fixture definition
	 */
	public DynamicEntity(EntityRenderer renderer, int layer, BodyDef bodyDef, FixtureDef fixtureDef){
		super(renderer, layer);
		
		this.bodyDef = bodyDef;
		fixtureDefs = new ArrayList<FixtureDef>();
		fixtureDefs.add(fixtureDef);
	}
	
	/**
	 * Create a new DynmicEntity with multiple fixtures
	 * @param renderer Renderer to use to draw entity
	 * @param layer Layer to draw entity on
	 * @param bodyDef Body definition
	 * @param fixtureDefs Fixture definition list
	 */
	public DynamicEntity(EntityRenderer renderer, int layer, BodyDef bodyDef, ArrayList<FixtureDef> fixtureDefs){
		super(renderer, layer);
		
		this.bodyDef = bodyDef;
		this.fixtureDefs = fixtureDefs;
	}
	
	/**
	 * Create a new DynamicEntity with a single shape and given density
	 * @param bodyDef Body definition 
	 * @param layer Layer to draw entity on
	 * @param shape Shape of entity
	 * @param density Density of entity
	 */
	public DynamicEntity(EntityRenderer renderer, int layer, BodyDef bodyDef, Shape shape, float density){
		super(renderer, layer);
		
		this.bodyDef = bodyDef;
		this.shape = shape;
		this.density = density;
	}
	
	/**
	 * Create a new DynamicEntity
	 * @param body Body that this entity is using
	 */
	public DynamicEntity(EntityRenderer renderer, int layer, Body body){
		super(renderer, layer);
		this.body = body;
		body.setUserData(this);
		this.isInitialized = true;
	}
	
	/**
	 * Initialize this DynamicEntity and add it to the Physics world
	 * @param world Physics world to put this Entity into
	 */
	public void init(World world){
		// only initialize if not intiialized
		if(!this.isInitialized){
			// create a body using the given body def
			body = world.createBody(bodyDef);
			
			// set a pointer back to this object
			body.setUserData(this);
			
			// if we're given fixture defs, use them to create body
			if(fixtureDefs != null){
				for(FixtureDef def : fixtureDefs){
					body.createFixture(def);
					def.shape.dispose();
				}
				fixtureDefs.clear();
				fixtureDefs = null;
				
			// else we're given a single shape as the body
			} else if(shape != null){
				body.createFixture(shape, density);
				shape.dispose();
				shape = null;
			} else{
				Log.e("DynamicEntity", "DynamicEntity not given enough parameters to initialize physics info!");
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
		if(this.isInitialized && body.isAwake()){
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
		this.body.getWorld().destroyBody(body);
		body = null;
	}

	@Override
	public void read(Kryo kryo, Input input) {
		super.read(kryo, input);
		
		// read in body def
		BodyDef bodyDef = kryo.readObject(input, BodyDef.class);
		
		// read in number of fixtures
		int numFixtures = input.readInt();
		
		// read in each fixture
		ArrayList<FixtureDef> fixtureDefs = new ArrayList<FixtureDef>(numFixtures);
		for(int i = 0; i < numFixtures; i++){
			FixtureDef fixDef = kryo.readObject(input, FixtureDef.class);
			fixtureDefs.add(fixDef);
		}
		
		this.bodyDef = bodyDef;
		this.fixtureDefs = fixtureDefs;
	}

	@Override
	public void write(Kryo kryo, Output output) {
		super.write(kryo, output);
		
		// write out the body def
		kryo.writeObject(output, PhysicsHelper.getBodyDef(body));
		
		ArrayList<Fixture> fixtures = body.getFixtureList();
		
		// write out number of fixtures
		output.writeInt(fixtures.size());
		
		// write out each fixture
		for(Fixture f : fixtures)
			kryo.writeObject(output, PhysicsHelper.getFixtureDef(f));
	}
}
