package com.bitwaffle.offworld.entities.dynamic;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.dynamic.DynamicEntity;
import com.bitwaffle.guts.graphics.EntityRenderer;
import com.bitwaffle.offworld.interfaces.Health;

/**
 * A DynamicEntity that implements Health
 * and removes itself from the world upon health going <= 0
 * 
 * @author TranquilMarmot
 */
public class DestroyableEntity extends DynamicEntity implements Health {
	private int health;
	
	/**
	 * @param renderer Renderer to use for entity
	 * @param layer Layer to draw entity on
	 * @param bodyDef Body def to use
	 * @param fixtureDef Fixture def to use
	 * @param initialHealth Initial health to give entity
	 */
	public DestroyableEntity(EntityRenderer renderer, int layer, BodyDef bodyDef, FixtureDef fixtureDef, int initialHealth){
		super(renderer, layer, bodyDef, fixtureDef);
		
		this.health = initialHealth;
	}

	@Override
	public void hurt(float amount) {
		health -= amount;		
		if(health <= 0)
			Game.physics.removeEntity(this, true);
	}

	@Override
	public void heal(float amount) { health += amount; }
	

	@Override
	public float currentHealth() { return health; }

}
