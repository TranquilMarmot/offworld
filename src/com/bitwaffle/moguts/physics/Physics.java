package com.bitwaffle.moguts.physics;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.bitwaffle.moguts.entities.BoxEntity;
import com.bitwaffle.moguts.entities.Entities;

/**
 * Handles all physics workings
 * 
 * @author TranquilMarmot
 */
public class Physics {
	/** World that everything is in */
	public static World world;
	
	/** Entities in the world */
	public static Entities entities;
	
	/** Gravity for the world */
	private Vector2 gravity = new Vector2(0.0f, -9.8f);
	
	/** Whether or not to sleep TODO look into what this means */
	boolean doSleep = true;
	
	/* How much to step the simulation each update */
	//final float timeStep = 1.0f / 30.0f;
	
	/** How many iterations to do for calculations */
	final int velocityIterations = 5, positionIterations = 2;
	
	/**
	 * Initialized physics
	 */
	public Physics(){
		// load natvies before doing anything (this is important!)
		GdxNativesLoader.load();
		
		// initialize the world
		world = new World(gravity, doSleep);
		entities = new Entities();
		
		temp();
	}
	
	/**
	 * Steps the physics simlation and updates every entity's location
	 */
	public void update(float timeStep){
		world.step(timeStep, velocityIterations, positionIterations);
		
		entities.update(timeStep);
	}
	
	/**
	 *  FIXME this initialization method is only temporary until some sort of save file gets implemented
	 */
	private void temp(){
		// bottom
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.position.set(0.0f, -50.0f);
		
		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(100.0f, 1.0f);
		
		BoxEntity ground = new BoxEntity(groundBodyDef, 100.0f, 1.0f, groundBox, 0.0f, new float[]{0.0f, 1.0f, 0.0f, 1.0f});
		entities.addEntity(ground);
		
		// right
		BodyDef groundBodyDef2 = new BodyDef();
		groundBodyDef2.position.set(100.0f, 50.0f);
		
		PolygonShape groundBox2 = new PolygonShape();
		groundBox2.setAsBox(1.0f, 100.0f);
		
		BoxEntity ground2 = new BoxEntity(groundBodyDef2, 1.0f, 100.0f, groundBox2, 0.0f, new float[]{0.0f, 1.0f, 0.0f, 1.0f});
		entities.addEntity(ground2);
		
		// left
		BodyDef groundBodyDef3 = new BodyDef();
		groundBodyDef3.position.set(-100.0f, 50.0f);
		
		PolygonShape groundBox3 = new PolygonShape();
		groundBox3.setAsBox(1.0f, 100.0f);
		
		BoxEntity ground3 = new BoxEntity(groundBodyDef3, 1.0f, 100.0f, groundBox3, 0.0f, new float[]{0.0f, 1.0f, 0.0f, 1.0f});
		entities.addEntity(ground3);
		
		// top
		BodyDef groundBodyDef4 = new BodyDef();
		groundBodyDef4.position.set(0.0f, 150.0f);
		
		PolygonShape groundBox4 = new PolygonShape();
		groundBox4.setAsBox(100.0f, 1.0f);
		
		BoxEntity ground4 = new BoxEntity(groundBodyDef4, 100.0f, 1.0f, groundBox4, 0.0f, new float[]{0.0f, 1.0f, 0.0f, 1.0f});
		entities.addEntity(ground4);
		
		for(int i = 0 ; i < 150; i ++)
			makeRandomBox();
		
		entities.update(1.0f / 30.0f);
	}
	
	/**
	 * Makes random boxes
	 */
	public void makeRandomBox(){
		Random randy = new Random();
		float boxX = randy.nextFloat() * 125.0f - 50.0f;
		if(boxX < 1.0f) boxX = 1.0f;
		float boxY = randy.nextFloat() * 100.0f - 25.0f;
		if(boxY < 1.0f) boxY = 1.0f;
		float sizeX = randy.nextFloat() * 10.0f;
		float sizeY = randy.nextFloat() * 10.0f;
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
		boxFixture.restitution = 0.4f;
		
		BoxEntity box = new BoxEntity(boxDef, sizeX, sizeY, boxFixture, new float[]{r, g, b, 1.0f});
		entities.addEntity(box);
		
		box.body.setAngularVelocity(randy.nextFloat() * 200.0f);
	}
}
