package com.bitwaffle.guts.entities.particles;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.entities.dynamic.DynamicEntity;
import com.bitwaffle.guts.graphics.EntityRenderer;

/**
 * Defines the behaviour of a particle emittter
 * 
 * @author TranquilMarmot
 */
public class EmitterSettings {
	/** Entity in the physics world the emitter is following */
	public DynamicEntity attached;
	
	/** Offset from center of attached entity to generate particles at */
	public Vector2 offset;
	
	/** Maxmimum number of particles to generate */
	public int maxParticles;
	
	/** How long each particle lives for, in seconds. Multiplied by a random float between 0.0 and 1.0. */
	public float particleLifetime;
	
	/** How long to wait between each emission, in seconds */
	public float particleEmissionRate;
	
	/** Number of particles to release on each emission */
	public int particlesPerEmission;
	
	/** How varied particle generation is on each axis. Multiplied by a random float between 0.0 and 1.0. */
	public float xLocationVariance, yLocationVariance;
	
	/** Info for particle physics bodies */
	public float particleFriction, particleRestitution, particleDensity;
	
	/** How large particle are */
	public float particleWidth, particleHeight;
	
	/** How fast particles are going when they get created. Both x and y get multiplied by a random float between 0.0 and 1.0. */
	public Vector2 particleForce;
	
	/** The renderer to use to render particles */
	public EntityRenderer particleRenderer;
}
