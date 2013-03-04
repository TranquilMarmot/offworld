package com.bitwaffle.guts.entities.particles

import com.bitwaffle.guts.entities.dynamic.BoxEntity
import com.badlogic.gdx.physics.box2d.BodyDef
import com.bitwaffle.guts.graphics.EntityRenderer
import com.bitwaffle.guts.graphics.Render2D
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.bitwaffle.guts.Game
import com.bitwaffle.guts.entities.dynamic.DynamicEntity
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.math.Vector2

class Particle(
		renderer: EntityRenderer,
		layer: Int,
		bodyDef: BodyDef,
		width: Float, height: Float,
		fixtureDef: FixtureDef,
		timeToLive: Float,
		owner: DynamicEntity)
		
		extends BoxEntity(renderer, layer, bodyDef, width, height, fixtureDef) {
	
	var timeAlive = 0.0f
	
	override def update(timeStep: Float){
		super.update(timeStep)
		
		timeAlive += timeStep
		if(timeAlive >= timeToLive)
			Game.physics.removeEntity(this, false);
	}
	
	def getOwner = owner
}