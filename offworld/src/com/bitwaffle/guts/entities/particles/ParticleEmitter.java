package com.bitwaffle.guts.entities.particles;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.Entity;
import com.bitwaffle.guts.physics.CollisionFilters;

public class ParticleEmitter extends Entity{
	private EmitterSettings settings;
	
	private float timeSinceLastEmission;
	
	/**
	 * Create a new particle emitter
	 * @param layer Layer to emit particles on
	 * @param offset Offset from center of entity being followed
	 * @param attached Entity that this emitter is attached to
	 */
	public ParticleEmitter(int layer, EmitterSettings settings){
		super(null, layer, new Vector2(settings.offset.x + settings.attached.getLocation().x, settings.offset.y + settings.attached.getLocation().y));
		this.settings = settings;
		
		this.timeSinceLastEmission = 0.0f;
	}
	
	private BodyDef getParticleBodyDef(){
		BodyDef def = new BodyDef();
		
		def.active = true;
		def.allowSleep = false;
		def.awake = true;
		def.type = BodyType.DynamicBody;
		//particleDef.angularDamping =
		def.linearVelocity.set(settings.particleForce);
		def.position.set(settings.offset.x + settings.attached.getLocation().x, settings.offset.y + settings.attached.getLocation().y);
		def.position.add(Game.random.nextFloat() * 2.0f, 0.0f);
		
		return def;
	}
	
	private FixtureDef getParticleFixtureDef(){
		FixtureDef def = new FixtureDef();
		
		def.density = settings.particleDensity;
		def.filter.categoryBits = CollisionFilters.ENTITY;
		def.filter.groupIndex = (short) -CollisionFilters.BULLET;
		def.filter.maskBits = (short) (CollisionFilters.GROUND | CollisionFilters.ENTITY);
		def.friction = 0.01f;
		def.restitution = 0.01f;
		PolygonShape particleBox = new PolygonShape();
		particleBox.setAsBox(settings.particleWidth, settings.particleHeight);
		def.shape = particleBox;
		
		return def;
	}
	
	@Override
	public void update(float timeStep){
		super.update(timeStep);
		
		this.setLocation(new Vector2(settings.offset.x + settings.attached.getLocation().x, settings.offset.y + settings.attached.getLocation().y));
		
		timeSinceLastEmission += timeStep;
		if(timeSinceLastEmission >= settings.particleEmissionRate){
			Game.physics.addEntity(new Particle(
					settings.particleRenderer,
					this.getLayer(),
					getParticleBodyDef(),
					settings.particleWidth, settings.particleHeight,
					getParticleFixtureDef(),
					settings.particleLifetime,
					settings.attached
					),false);
			timeSinceLastEmission = 0.0f;
		}
	}
}
