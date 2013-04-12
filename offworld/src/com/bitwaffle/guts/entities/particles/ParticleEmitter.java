package com.bitwaffle.guts.entities.particles;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.Entity;
import com.bitwaffle.guts.physics.CollisionFilters;

/**
 * Poops out particles
 * 
 * @author TranquilMarmot
 */
public class ParticleEmitter extends Entity{
	/** Settings being used by this emitter */
	public EmitterSettings settings;
	
	/** Counter to know when to emit particles */
	private float timeSinceLastEmission;
	
	/** Whether or not this emitter is currently releasing particles */
	private boolean active;
	
	/** Number of particles that this emitter has created */
	private int numParticlesOut;
	
	public ParticleEmitter(int layer, EmitterSettings settings){
		super(null, layer, new Vector2(settings.offset().x + settings.attached().getLocation().x, settings.offset().y + settings.attached().getLocation().y));
		this.settings = settings;
		
		this.timeSinceLastEmission = 0.0f;
		this.active = true;
		numParticlesOut = 0;
	}
	
	/** @return Body def for particle, based on current settings */
	private BodyDef getParticleBodyDef(){
		BodyDef def = new BodyDef();
		
		def.active = true;
		def.allowSleep = false;
		def.awake = true;
		def.type = BodyType.DynamicBody;
		def.linearVelocity.set(settings.particleForce().x, settings.particleForce().y);
		def.position.set(settings.offset().x + settings.attached().getLocation().x, settings.offset().y + settings.attached().getLocation().y);
		
		return def;
	}
	
	/** @return Fixture def for particle, based on current settings */
	private FixtureDef getParticleFixtureDef(){
		FixtureDef def = new FixtureDef();
		
		def.density = settings.particleDensity();
		def.filter.categoryBits = CollisionFilters.PARTICLE;
		//def.filter.groupIndex = (short) -CollisionFilters.BULLET;
		//def.filter.maskBits = (short) (CollisionFilters.GROUND | CollisionFilters.ENTITY);
		def.friction = settings.particleFriction();
		def.restitution = settings.particleRestitution();
		PolygonShape particleBox = new PolygonShape();
		particleBox.setAsBox(settings.particleWidth(), settings.particleHeight());
		def.shape = particleBox;
		
		return def;
	}
	
	@Override
	public void update(float timeStep){
		super.update(timeStep);
		
		// follow the entity that the emitter is attached to
		this.setLocation(new Vector2(settings.offset().x + settings.attached().getLocation().x, settings.offset().y + settings.attached().getLocation().y));
		
		if(this.active){
			timeSinceLastEmission += timeStep;
			
			// only emit particles if we're past the emission rate
			if(timeSinceLastEmission >= settings.particleEmissionRate() && numParticlesOut < settings.maxParticles()){
				// release number of specified particles
				for(int i = 0; i < settings.particlesPerEmission(); i++){
					Game.physics.addEntity(new Particle(
							settings.particleRenderer(),
							this.getLayer(),
							getParticleBodyDef(),
							settings.particleWidth(), settings.particleHeight(),
							getParticleFixtureDef(),
							Game.random.nextFloat() * settings.particleLifetime(),
							this
							),false);
					
					numParticlesOut++;
					if(numParticlesOut >= settings.maxParticles())
						break;
				}
				
				// reset counter
				timeSinceLastEmission = 0.0f;
			}
		}
	}
	
	/** Deactivate this emitter so it no longer releases particles */
	public void deactivate(){ this.active = false; }
	
	/** Activate this emitter */
	public void activate(){ this.active = true; }	
	
	/**
	 * Called by particles when they die so the emitter knows how
	 * many particles it has out
	 */
	protected void notifyOfParticleDeath(){ numParticlesOut--; }
}
