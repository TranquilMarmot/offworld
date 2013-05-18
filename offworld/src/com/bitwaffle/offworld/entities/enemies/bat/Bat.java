package com.bitwaffle.offworld.entities.enemies.bat;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.ai.AI;
import com.bitwaffle.guts.ai.states.PathFollower;
import com.bitwaffle.guts.entity.dynamic.DynamicEntity;
import com.bitwaffle.offworld.OffworldGame;
import com.bitwaffle.offworld.entities.enemies.bat.render.BatFlyAnimation;
import com.bitwaffle.offworld.entities.enemies.bat.render.BatRenderer;
import com.bitwaffle.offworld.entities.enemies.bat.render.BatSleepAnimation;
import com.bitwaffle.offworld.interfaces.Health;

/**
 * Ey batty bwoy what a gwan seen? Irie star.
 * 
 * @author TranquilMarmot
 */
public class Bat extends DynamicEntity implements Health {
	// TODO add sensor to tell when player is near and attack player
	// TODO add states for idle sweepin (sin and cos velocities)
	
	protected BatSleepAnimation sleepAnimation;
	protected BatFlyAnimation flyAnimation;
	
	private static float width = 0.78f, height = 1.35f;
	
	private float health = 100.0f;
	
	public boolean sleeping = false;
	
	// FIXME temp??
	float timer = 0.0f;
	
	/** AI controlling this Bat */
	private AI ai;
	/** Makes bat move along given path */
	public PathFollower follower;
	
	public Bat(int layer, Vector2 location){
		super(new BatRenderer(), layer, getBodyDef(location), getFixtureDef());
		sleepAnimation = new BatSleepAnimation(this);
		flyAnimation = new BatFlyAnimation(this);
		
		float
			pathNodeDist = 7.5f,
			pathGoalThreshold = 14.0f,
			pathUpdateFrequency = 2.0f,
			nodeThreshold = 2.0f,
			followSpeed = 7.5f;
		int pathMaxIterations = 1000;
		ai = new AI(this);
		follower = new PathFollower(pathNodeDist, pathGoalThreshold, pathUpdateFrequency, pathMaxIterations, nodeThreshold, followSpeed);
		ai.setState(follower);
	}
	
	private static BodyDef getBodyDef(Vector2 location){
		BodyDef def = new BodyDef();
		def.position.set(location);
		
		def.type = BodyDef.BodyType.DynamicBody;
		def.fixedRotation = true;
		
		return def;
	}
	
	private static FixtureDef getFixtureDef(){
		FixtureDef def = new FixtureDef();
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width, height);
		def.shape = shape;
		
		def.density = 1.0f;
		
		return def;
	}
	
	@Override
	public void update(float timeStep){
		super.update(timeStep);
		sleepAnimation.update(timeStep);
		flyAnimation.update(timeStep);
		timer += timeStep;
		
		follower.pathfinder.setStart(this.location);
		follower.pathfinder.setGoal(OffworldGame.players[0].getLocation());
		ai.update(timeStep);
		
		/*
		if(sleeping){
			//Vector2 linVec = this.body.getLinearVelocity();
			this.body.setLinearVelocity(0.0f, 1.0f);
		} else {
			this. body.setLinearVelocity((float)Math.sin(timer) * 2.0f, (float)Math.cos(timer) * 1.0f);
		}
		*/
	}
	
	public boolean isSleeping(){ return sleeping; }
	
	public float getWidth() { return width; }
	public float getHeight(){ return height; }
	
	public BatFlyAnimation getFlyAnimation(){ return flyAnimation; }
	public BatSleepAnimation getSleepAnimation(){ return sleepAnimation; }
	
	@Override
	public float currentHealth() { return health; }

	@Override
	public void hurt(float amount) {
		health -= amount;
		if(health <= 0.0f)
			Game.physics.removeEntity(this, true);
	}

	@Override
	public void heal(float amount) { health += amount; }
}
