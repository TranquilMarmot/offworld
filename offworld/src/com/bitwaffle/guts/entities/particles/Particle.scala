package com.bitwaffle.guts.entities.particles

import com.bitwaffle.guts.entities.dynamic.BoxEntity
import com.badlogic.gdx.physics.box2d.BodyDef
import com.bitwaffle.guts.entities.entities2d.Entity2DRenderer
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.bitwaffle.guts.Game
import com.bitwaffle.guts.entities.dynamic.DynamicEntity
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.math.Vector2

/** A particle that comes from an emitter */
class Particle(
		renderer: Entity2DRenderer,
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