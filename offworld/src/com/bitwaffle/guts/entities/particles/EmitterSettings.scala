package com.bitwaffle.guts.entities.particles

import com.bitwaffle.guts.entities.dynamic.DynamicEntity
import com.badlogic.gdx.math.Vector2
import com.bitwaffle.guts.graphics.EntityRenderer

/** Settings for a particle emitter */
trait EmitterSettings {
	/** The emitter stays attached to this entity */
	def attached: DynamicEntity
	
	/** Offset from center of attached entity that the emitter sits at */
	def offset: Vector2
	
	/** Maximum number of particles the emitter can have at one time */
	def maxParticles: Int
	
	/** How long each particle lives */
	def particleLifetime: Float
	
	/** How long the emitter takes to emit particles, in seconds */
	def particleEmissionRate: Float
	
	/** How many particles are let out per emission */
	def particlesPerEmission: Int
	
	/** X/Y scatter amount */
	def xLocationVariance: Float
	def yLocationVariance: Float
	
	/** Info for particle in physics world */
	def particleFriction: Float
	def particleRestitution: Float
	def particleDensity: Float
	
	/** Size of particles */
	def particleWidth: Float
	def particleHeight: Float
	
	/** How fast particles come out of the emitter */
	def particleForce: Vector2
	
	/** Renderer to use for each particle */
	def particleRenderer: EntityRenderer
}