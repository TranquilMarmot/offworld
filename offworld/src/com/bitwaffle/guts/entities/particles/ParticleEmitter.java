package com.bitwaffle.guts.entities.particles;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.Sphere;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.Entity;
import com.bitwaffle.guts.physics.CollisionFilters;

public class ParticleEmitter extends Entity{
	private EmitterSettings settings;
	
	private float timeSinceLastEmission;
	
	private boolean active;
	
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
		this.active = true;
	}
	
	private BodyDef getParticleBodyDef(){
		BodyDef def = new BodyDef();
		
		def.active = true;
		def.allowSleep = false;
		def.awake = true;
		def.type = BodyType.DynamicBody;
		//particleDef.angularDamping =
		def.linearVelocity.set(Game.random.nextFloat() * settings.particleForce.x, Game.random.nextFloat() * settings.particleForce.y);
		def.position.set(settings.offset.x + settings.attached.getLocation().x, settings.offset.y + settings.attached.getLocation().y);
		def.position.add(Game.random.nextFloat() * settings.xLocationVariance, Game.random.nextFloat() * settings.yLocationVariance);
		
		return def;
	}
	
	private FixtureDef getParticleFixtureDef(){
		FixtureDef def = new FixtureDef();
		
		def.density = settings.particleDensity;
		def.filter.categoryBits = CollisionFilters.PARTICLE;
		//def.filter.groupIndex = (short) -CollisionFilters.BULLET;
		//def.filter.maskBits = (short) (CollisionFilters.GROUND | CollisionFilters.ENTITY);
		def.friction = settings.particleFriction;
		def.restitution = settings.particleRestitution;
		PolygonShape particleBox = new PolygonShape();
		particleBox.setAsBox(settings.particleWidth, settings.particleHeight);
		//CircleShape circ = new CircleShape();
		//circ.setRadius(settings.particleWidth);
		def.shape = particleBox;
		
		return def;
	}
	
	@Override
	public void update(float timeStep){
		super.update(timeStep);
		
		this.setLocation(new Vector2(settings.offset.x + settings.attached.getLocation().x, settings.offset.y + settings.attached.getLocation().y));
		
		if(this.active){
			timeSinceLastEmission += timeStep;
			if(timeSinceLastEmission >= settings.particleEmissionRate){
				for(int i = 0; i < settings.particlesPerEmission; i++){
					Game.physics.addEntity(new Particle(
							settings.particleRenderer,
							this.getLayer(),
							getParticleBodyDef(),
							settings.particleWidth, settings.particleHeight,
							getParticleFixtureDef(),
							Game.random.nextFloat() * settings.particleLifetime,
							settings.attached
							),false);
					timeSinceLastEmission = 0.0f;
				}
			}
		}
	}
	
	public void deactivate(){
		this.active = false;
	}
	
	public void activate(){
		this.active = true;
	}
}
