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
	protected BatSleepAnimation animation;
	
	private float health;

	public Bat(int layer, Vector2 location){
		super(new BatRenderer(), layer, getBodyDef(location), getFixtureDef());
		animation = new BatSleepAnimation(this);
	}
	
	private static BodyDef getBodyDef(Vector2 location){
		BodyDef def = new BodyDef();
		def.position.set(location);
		
		def.type = BodyDef.BodyType.DynamicBody;
		
		return def;
	}
	
	private static FixtureDef getFixtureDef(){
		FixtureDef def = new FixtureDef();
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(0.78f, 1.5f);
		def.shape = shape;
		
		def.density = 1.0f;
		
		return def;
	}
	
	@Override
	public void update(float timeStep){
		super.update(timeStep);
		animation.update(timeStep);
	}
	
	
	
	@Override
	public float currentHealth() { return health; }

	@Override
	public void hurt(float amount) { health -= amount; }

	@Override
	public void heal(float amount) { health += amount; }
	
	
}
