package com.bitwaffle.offworld.moguts.physics;

import java.util.Iterator;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.bitwaffle.offworld.moguts.entity.BoxEntity;
import com.bitwaffle.offworld.moguts.entity.Entities;
import com.bitwaffle.offworld.moguts.entity.Entity;

public class Physics {
	public static World world;
	public static Entities entities;
	
	Vector2 gravity = new Vector2(0.0f, -9.8f);
	boolean doSleep = true;
	
	final float timeStep = 1.0f / 60.0f;
	final int velocityIterations = 6, positionIterations = 2;
	
	public Physics(){
		// load natvies before doing anything (this is important!)
		GdxNativesLoader.load();
		
		// initialize the world
		world = new World(gravity, doSleep);
		
		entities = new Entities();
		
		temp();
	}
	
	private void temp(){
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.position.set(0.0f, -1.3f);
		
		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(25.0f, 1.0f);
		
		BoxEntity ground = new BoxEntity(groundBodyDef, 25.0f, 1.0f, groundBox, 0.0f, new float[]{0.0f, 1.0f, 0.0f, 1.0f});
		entities.addEntity(ground);
		
		
		BodyDef boxDef = new BodyDef();
		boxDef.type = BodyDef.BodyType.DynamicBody;
		boxDef.position.set(0.0f, 10.0f);
		
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(1.0f, 1.0f);
		
		FixtureDef boxFixture = new FixtureDef();
		boxFixture.shape = boxShape;
		boxFixture.density = 1.0f;
		boxFixture.friction = 0.3f;
		boxFixture.restitution = 0.39999f;
		
		BoxEntity box = new BoxEntity(boxDef, 1.0f, 1.0f, boxFixture, new float[]{1.0f, 0.0f, 0.0f, 1.0f});
		entities.addEntity(box);
		
		entities.update();
	}
	
	public void update(){
		entities.update();
		
		world.step(timeStep, velocityIterations, positionIterations);
	
		Iterator<Entity> it = entities.getIterator();
		
		while(it.hasNext())
			it.next().update();
	}
}
