package com.bitwaffle.moguts.physics;

import java.util.Iterator;
import java.util.Random;
import java.util.Stack;

import android.os.SystemClock;
import android.util.FloatMath;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.bitwaffle.moguts.Game;
import com.bitwaffle.moguts.entities.BoxEntity;
import com.bitwaffle.moguts.entities.DynamicEntity;
import com.bitwaffle.moguts.entities.Entities;
import com.bitwaffle.moguts.entities.Entity;
import com.bitwaffle.moguts.entities.Player;

/**
 * Handles all physics workings
 * 
 * @author TranquilMarmot
 */
public class Physics {
	/** World that everything is in */
	public World world;
	
	/** Entities in the world */
	private Entities entities;
	
	/** Gravity for the world */
	private Vector2 gravity = new Vector2(0.0f, -9.8f/*-39.2f*/);
	
	/** Whether or not to sleep TODO look into what this means */
	boolean doSleep = false;
	
	/** How many iterations to do for calculations */
	final int velocityIterations = 6, positionIterations = 2;
	
	/** How much to step the world each time (Box2D prefers 1/60) */
	private final float FIXED_TIMESTEP = 1.0f / 60.0f;
	
	/** Maximum number of allowed steps per frame */
	private final int MAX_STEPS = 5;
	
	/** Used to know how much time has passed */
	private float timeStepAccum = 0;
	
	/** Used to know how much time has passed */
	private long previousTime;
	
	/**
	 * Sometimes, DynamicEntities can get added in the middle of a physics tick.
	 * When the entity is added to the physics world, this can cause the game to crash
	 * without a stack trace (since the crash is in the C++ native code)
	 * So, each DynamicEntity has an init() method that adds it to the physics world.
	 * Any time a DynamicEntity is added (via the addEntity() method in this class)
	 * it gets added to this stack, then at the beginning of every update() the stack
	 * gets empties and whatever was on it has its init() method called.
	 */
	private Stack<DynamicEntity> toInitialize;
	
	
	/**
	 * Initialized physics
	 */
	public Physics(){
		// load natvies before doing anything (this is important!)
		GdxNativesLoader.load();
		
		// initialize the world
		world = new World(gravity, doSleep);
		world.setContactListener(new ContactHandler());
		entities = new Entities();
		toInitialize = new Stack<DynamicEntity>();
		
		previousTime = SystemClock.elapsedRealtime();
	}
	
	// FIXME this don't work
	public void restartWorld(){
		world.dispose();
		world = new World(gravity, doSleep);
		entities.clear();
		temp();
	}
	
	/**
	 * Steps the physics simulation and updates every entity's location
	 */
	public void update(){
		// get the current time
		long currentTime = SystemClock.elapsedRealtime();
		
		// initialize any entities that need intialization
		while(!toInitialize.isEmpty())
			toInitialize.pop().init();

		// subtract and convert to seconds 
		float deltaTime = (currentTime - previousTime) / 1000.0f;
		
		// set previousTime for next iteration
		previousTime = currentTime;
		
		// add the change in time to the accumulator, then find out how many steps we need to do
		timeStepAccum += deltaTime;
		float steps = FloatMath.floor(timeStepAccum / FIXED_TIMESTEP);
		
		// only touch the accumulator if necessary
		if(steps > 0)
			timeStepAccum -= steps * FIXED_TIMESTEP;
		
		// clamp steps and iterate however many times
		for(int i = 0; i < Math.min(steps, MAX_STEPS); i++){
			world.step(FIXED_TIMESTEP, velocityIterations, positionIterations);
			entities.update(FIXED_TIMESTEP);
		}
		
		world.clearForces();
	}
	
	/**
	 *  FIXME this initialization method is only temporary until some sort of save file gets implemented
	 */
	public void temp2(){
		// bottom
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.position.set(0.0f, -50.0f);
		
		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(1000.0f, 1.0f);
		
		BoxEntity ground = new BoxEntity(groundBodyDef, 1000.0f, 1.0f, groundBox, 0.0f, new float[]{0.5f, 0.5f, 0.5f, 1.0f});
		this.addEntity(ground);
		
		// player
		BodyDef playerBodyDef = new BodyDef();
		playerBodyDef.type = BodyDef.BodyType.DynamicBody;
		playerBodyDef.position.set(0.0f, 5.0f);
		
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(1.0f, 1.5f);
		
		FixtureDef playerFixture = new FixtureDef();
		playerFixture.shape = boxShape;
		playerFixture.density = 1.0f;
		playerFixture.friction = 0.3f;
		playerFixture.restitution = 0.0f;
		
		Game.player = new Player(playerBodyDef, 1.0f, 1.5f, playerFixture);
		this.addEntity(Game.player);
		
		for(int i = 0; i < 175; i++)
			makeRandomBox();
	}
	
	/**
	 *  FIXME this initialization method is only temporary until some sort of save file gets implemented
	 */
	public void temp(){
		// bottom
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.position.set(0.0f, -50.0f);
		
		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(100.0f, 1.0f);
		
		BoxEntity ground = new BoxEntity(groundBodyDef, 100.0f, 1.0f, groundBox, 0.0f, new float[]{0.0f, 1.0f, 0.0f, 1.0f});
		this.addEntity(ground);
		
		// right
		BodyDef groundBodyDef2 = new BodyDef();
		groundBodyDef2.position.set(100.0f, 50.0f);
		
		PolygonShape groundBox2 = new PolygonShape();
		groundBox2.setAsBox(1.0f, 100.0f);
		
		BoxEntity ground2 = new BoxEntity(groundBodyDef2, 1.0f, 100.0f, groundBox2, 0.0f, new float[]{0.0f, 1.0f, 0.0f, 1.0f});
		this.addEntity(ground2);
		
		// left
		BodyDef groundBodyDef3 = new BodyDef();
		groundBodyDef3.position.set(-100.0f, 50.0f);
		
		PolygonShape groundBox3 = new PolygonShape();
		groundBox3.setAsBox(1.0f, 100.0f);
		
		BoxEntity ground3 = new BoxEntity(groundBodyDef3, 1.0f, 100.0f, groundBox3, 0.0f, new float[]{0.0f, 1.0f, 0.0f, 1.0f});
		this.addEntity(ground3);
		
		// top
		BodyDef groundBodyDef4 = new BodyDef();
		groundBodyDef4.position.set(0.0f, 150.0f);
		
		PolygonShape groundBox4 = new PolygonShape();
		groundBox4.setAsBox(100.0f, 1.0f);
		
		BoxEntity ground4 = new BoxEntity(groundBodyDef4, 100.0f, 1.0f, groundBox4, 0.0f, new float[]{0.0f, 1.0f, 0.0f, 1.0f});
		this.addEntity(ground4);
		
		for(int i = 0 ; i < 75; i ++)
			makeRandomBox();
		
		entities.update(1.0f / 30.0f);
	}
	
	/**
	 * Makes random boxes
	 */
	public void makeRandomBox(){
		Random randy = new Random();
		float boxX = randy.nextFloat() * 30.0f - 50.0f;
		if(boxX < 1.0f) boxX = 1.0f;
		float boxY = randy.nextFloat() * 30.0f - 25.0f;
		if(boxY < 1.0f) boxY = 1.0f;
		float sizeX = randy.nextFloat() * 1.5f;
		if(sizeX < 1.0f) sizeX = 1.0f;
		float sizeY = randy.nextFloat() * 1.5f;
		if(sizeY < 1.0f) sizeY = 1.0f;
		float r = randy.nextFloat();
		float g = randy.nextFloat();
		float b = randy.nextFloat();
		
		
		BodyDef boxDef = new BodyDef();
		boxDef.type = BodyDef.BodyType.DynamicBody;
		boxDef.position.set(boxX, boxY);
		
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(sizeX, sizeY);
		
		FixtureDef boxFixture = new FixtureDef();
		boxFixture.shape = boxShape;
		boxFixture.density = 1.0f;
		boxFixture.friction = 0.3f;
		boxFixture.restitution = 0.3f;
		
		BoxEntity box = new BoxEntity(boxDef, sizeX, sizeY, boxFixture, new float[]{r, g, b, 1.0f}){
			@Override
			public void init(){
				super.init();
				
				Random randy = new Random();
				this.body.setAngularVelocity(randy.nextFloat() * 1.0f);
				
				float linX = randy.nextFloat() * 1.0f;
				float linY = randy.nextFloat() * 1.0f;
				if(randy.nextBoolean()) linX *= -1.0f;
				if(randy.nextBoolean()) linY *= -1.0f;
				this.body.setLinearVelocity(linX, linY);
			}
		};
		this.addEntity(box);
	}
	
	/**
	 * Add a DynamicEntity to the Physics world
	 * @param ent Entity to add
	 */
	public void addEntity(DynamicEntity ent){
		entities.addDynamicEntity(ent);
		toInitialize.push(ent);
	}
	
	/**
	 * Add a passive Entity to the Entities list
	 * @param ent Entity to add
	 */
	// FIXME should this method be somewhere else? Passive entities are pretty much unrelated to the physics world
	public void addEntity(Entity ent){
		entities.addPassiveEntity(ent);
	}
	
	/**
	 * @return An iterator of every passive Entity
	 */
	public Iterator<Entity> getPassiveEntityIterator(){
		return entities.getPassiveEntityIterator();
	}
	
	/**
	 * @return An iterator of every DynamicEntity
	 */
	public Iterator<DynamicEntity> getDynamicEntityIterator(){
		return entities.getDynamicEntityIterator();
	}
}
