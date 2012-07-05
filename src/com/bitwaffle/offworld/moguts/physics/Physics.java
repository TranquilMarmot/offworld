package com.bitwaffle.offworld.moguts.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.bitwaffle.offworld.moguts.entity.Entities;

public class Physics {
	public static World world;
	public static Entities entities;
	
	Vector2 gravity = new Vector2(0.0f, -9.8f);
	boolean doSleep = true;
	
	BodyDef groundBodyDef, bodyDef;
	Body groundBody, body;
	PolygonShape groundBox, dynamicBox;
	
	FixtureDef fixtureDef;
	
	final float timeStep = 1.0f / 60.0f;
	final int velocityIterations = 6, positionIterations = 2;
	
	public Physics(){
		// load natvies before doing anything (this is important!)
		GdxNativesLoader.load();
		
		// initialize the world
		world = new World(gravity, doSleep);
		
		entities = new Entities();
		
		// make ground
		groundBodyDef = new BodyDef();
		groundBodyDef.position.set(0.0f, -10.0f);
		
		groundBody = world.createBody(groundBodyDef);
		
		groundBox = new PolygonShape();
		groundBox.setAsBox(50.0f, 10.0f);
		
		groundBody.createFixture(groundBox, 0.0f);
		
		// now for body
		bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(0.0f, 4.0f);
		
		body = world.createBody(bodyDef);
		dynamicBox = new PolygonShape();
		dynamicBox.setAsBox(1.0f, 1.0f);
		
		fixtureDef = new FixtureDef();
		fixtureDef.shape = dynamicBox;
		fixtureDef.density = 1.0f;
		fixtureDef.friction = 0.3f;
		body.createFixture(fixtureDef);
	}
	
	public void update(){
		
		world.step(timeStep, velocityIterations, positionIterations);
		
		Vector2 position = body.getPosition();
		float angle = body.getAngle();
		
		System.out.println(position.x + " " + position.y + " " + angle);
	}
}
