package com.bitwaffle.guts.entities.particles;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.entities.dynamic.DynamicEntity;
import com.bitwaffle.guts.graphics.EntityRenderer;

public class EmitterSettings {
	public DynamicEntity attached;
	
	public Vector2 offset;
	
	public int numParticles;
	
	public float particleLifetime;
	
	public float particleEmissionRate;
	
	public int particlesPerEmission;
	
	public float xLocationVariance, yLocationVariance;
	
	public float particleFriction, particleRestitution;
	
	public float particleWidth, particleHeight;
	
	public float particleDensity;
	
	public Vector2 particleForce;
	
	public EntityRenderer particleRenderer;
}
