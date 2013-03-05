package com.bitwaffle.guts.entities.particles

import com.bitwaffle.guts.entities.dynamic.DynamicEntity
import com.badlogic.gdx.math.Vector2
import com.bitwaffle.guts.graphics.EntityRenderer

trait EmitterSettings {
	def attached: DynamicEntity
	
	def offset: Vector2
	
	def maxParticles: Int
	
	def particleLifetime: Float
	
	def particleEmissionRate: Float
	
	def particlesPerEmission: Int
	
	def xLocationVariance: Float
	
	def yLocationVariance: Float
	
	def particleFriction: Float
	def particleRestitution: Float
	def particleDensity: Float
	
	def particleWidth: Float
	def particleHeight: Float
	
	def particleForce: Vector2
	
	def particleRenderer: EntityRenderer
}