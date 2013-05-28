package com.bitwaffle.guts.entity.dynamic.particles

import com.bitwaffle.guts.entity.dynamic.BoxEntity
import com.badlogic.gdx.physics.box2d.BodyDef
import com.bitwaffle.guts.graphics.graphics2d.EntityRenderer2D
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.bitwaffle.guts.Game

/** A particle that comes from an emitter */
class Particle(
		renderer: EntityRenderer2D,
		layer: Int,
		bodyDef: BodyDef,
		width: Float, height: Float,
		fixtureDef: FixtureDef,
		timeToLive: Float, // how long particle is alive for (in seconds)
		owner: ParticleEmitter)
		
		extends BoxEntity(renderer, layer, bodyDef, width, height, fixtureDef) {
	
	/** As soon as this gets above the time to live, particle dies*/
	var timeAlive = 0.0f
	
	override def update(timeStep: Float){
		super.update(timeStep)
		
		// die if been alive for longer than TTL
		timeAlive += timeStep
		if(timeAlive >= timeToLive)
			Game.physics.removeEntity(this, false)
	}
	
	override def cleanup{
	  super.cleanup
	  
	  owner notifyOfParticleDeath
	}
	
	def getOwner = owner.settings.attached
}