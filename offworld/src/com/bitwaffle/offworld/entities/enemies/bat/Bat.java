package com.bitwaffle.offworld.entities.enemies.bat;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.bitwaffle.guts.entities.dynamic.DynamicEntity;
import com.bitwaffle.offworld.interfaces.Health;

/**
 * Ey batty bwoy what a gwan seen? Irie star.
 * 
 * @author TranquilMarmot
 */
public class Bat extends DynamicEntity implements Health {
	protected BatSleepAnimation sleepAnimation;
	protected BatFlyAnimation flyAnimation;
	
	private float health;
	
	private boolean sleeping = false;
	
	float timer = 0.0f;

	public Bat(int layer, Vector2 location){
		super(new BatRenderer(), layer, getBodyDef(location), getFixtureDef());
		sleepAnimation = new BatSleepAnimation(this);
		flyAnimation = new BatFlyAnimation();
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
		shape.setAsBox(0.78f, 1.35f);
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
		
		if(sleeping){
			//Vector2 linVec = this.body.getLinearVelocity();
			this.body.setLinearVelocity(0.0f, 1.0f);
		} else {
			this. body.setLinearVelocity((float)Math.sin(timer) * 2.0f, (float)Math.cos(timer) * 2.0f);
		}
	}
	
	public boolean isSleeping(){ return sleeping; }
	
	@Override
	public float currentHealth() { return health; }

	@Override
	public void hurt(float amount) { health -= amount; }

	@Override
	public void heal(float amount) { health += amount; }
	
	
}
