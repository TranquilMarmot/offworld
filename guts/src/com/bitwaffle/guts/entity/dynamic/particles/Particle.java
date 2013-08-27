package com.bitwaffle.guts.entity.dynamic.particles;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entity.dynamic.BoxEntity;
import com.bitwaffle.guts.graphics.graphics2d.ObjectRenderer2D;

/**
 * A particle.
 * 
 * @author TranquilMarmot
 */
public class Particle extends BoxEntity {
	/** How long the particle stays alive */
	private float timeToLive, timeAlive;
	/** The emitter this particle came from */
	private ParticleEmitter owner;
	
	public Particle(
			float timeToLive, ParticleEmitter owner,
			ObjectRenderer2D renderer, int layer, 
			BodyDef bodyDef, float width, float height, 
			FixtureDef fixtureDef){
		
		super(renderer, layer, bodyDef, width, height, fixtureDef);
		this.timeToLive = timeToLive;
		this.owner = owner;
	}
	
	@Override
	public void update(float timeStep){
		super.update(timeStep);
		
		timeAlive += timeStep;
		if(timeAlive >= timeToLive)
			Game.physics.removeEntity(this, false);
		
	}
	
	@Override
	public void cleanup(){
		super.cleanup();
		
		owner.notifyOfParticleDeath();
	}
	
	public ParticleEmitter getEmitter(){ return this.owner; };
	
}
