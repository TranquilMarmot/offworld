package com.bitwaffle.offworld.entities.enemies.bat;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entity.ai.AI;
import com.bitwaffle.guts.entity.ai.path.PathFinderSettings;
import com.bitwaffle.guts.entity.dynamic.DynamicEntity;
import com.bitwaffle.guts.physics.PhysicsUpdateRequest;
import com.bitwaffle.offworld.entities.enemies.bat.render.BatFlyAnimation;
import com.bitwaffle.offworld.entities.enemies.bat.render.BatRenderer;
import com.bitwaffle.offworld.entities.enemies.bat.render.BatSleepAnimation;
import com.bitwaffle.offworld.entities.player.Player;
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
	
	/** AI controlling this Bat */
	public AI ai;
	/** Makes bat move along given path */
	public AttackAIState attackState;
	/** Sense any players and wakes on finding one */
	public SleepAIState sleepState;
	
	public Bat(int layer, Vector2 location){
		super(new BatRenderer(), layer, getBodyDef(location), getFixtureDef());
		sleepAnimation = new BatSleepAnimation(this);
		flyAnimation = new BatFlyAnimation(this);
		
		float
			pathNodeDist = 17.5f,
			pathGoalThreshold = 18.0f,
			pathUpdateFrequency = 2.0f,
			nodeThreshold = 2.0f,
			followSpeed = 0.5f;
		int pathMaxIterations = 1000;
		boolean allowDiagonal = true;
		ai = new AI(this);
		attackState = new AttackAIState(this, new PathFinderSettings(
				pathNodeDist,
				pathGoalThreshold,
				pathUpdateFrequency,
				pathMaxIterations, allowDiagonal), nodeThreshold, followSpeed);
	}
	

	@Override
	public void init(World world){
		super.init(world);
		
		sleepState = new SleepAIState(this, 15.0f);
		ai.setState(sleepState);
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
		ai.update(timeStep);
	}
	
	/** Called in OffworldContactListener when a Player hits the bat's player sensor */
	public void reportPlayerSensorHit(Player contact) {
		attackState.setPlayer(contact);
		
		ai.setState(attackState);
		Game.physics.addUpdateRequest(new PhysicsUpdateRequest(){
			@Override
			public void doRequest() {
				body.destroyFixture(sleepState.playerSensor);
			}
			
		});
		
	}
	
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
