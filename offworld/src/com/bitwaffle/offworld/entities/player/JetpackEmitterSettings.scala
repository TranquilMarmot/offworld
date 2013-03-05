package com.bitwaffle.offworld.entities.player

import com.bitwaffle.guts.entities.particles.EmitterSettings
import com.bitwaffle.guts.entities.dynamic.DynamicEntity
import com.badlogic.gdx.math.Vector2
import com.bitwaffle.guts.graphics.EntityRenderer
import com.bitwaffle.guts.graphics.Render2D
import com.bitwaffle.guts.entities.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.bitwaffle.guts.entities.particles.Particle
import com.bitwaffle.guts.Game

class JetpackEmitterSettings(attachedTo: DynamicEntity) extends EmitterSettings {
	def attached = attachedTo;
	def offset = new Vector2(Game.random.nextFloat * xLocationVariance * -0.2f, Game.random.nextFloat * yLocationVariance * 0.02f)
	def maxParticles = 150
	def particleDensity = 0.1f
	def particleEmissionRate = 0.05f
	def particlesPerEmission = 5
	def particleForce = new Vector2(if(Game.random.nextBoolean()) Game.random.nextFloat * -5.0f else Game.random.nextFloat * 5.0f, Game.random.nextFloat * -50.0f)
	def particleHeight = 0.25f
	def particleWidth = 0.25f
	def particleLifetime = {
		if(Game.random.nextFloat > 0.9f){
			val low = 0.8f
			val high = 1.0f
			val liveLong = Game.random.nextFloat
			if(liveLong < low) low * 2.0f else if(liveLong > high) high * 2.0f else liveLong * 2.0f
		}else
			Game.random.nextFloat * 0.75f;
			
	}
	def xLocationVariance = 0.5f
	def yLocationVariance = 0.1f
	def particleFriction = 10.0f
	def particleRestitution = 0.2f
	
	
	def particleRenderer = new EntityRenderer {
		override def render(renderer: Render2D, ent: Entity, debug: Boolean){
			Gdx.gl20.glEnable(GL20.GL_BLEND);
			Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			
			if(debug) renderDebug(renderer, ent)
			else{
				val p = ent.asInstanceOf[Particle]
				
				renderer.program.setUniform("vColor", 1.0f, 1.0f, 1.0f, 0.75f);
				//Gdx.gl20.glEnable(GL20.GL_BLEND);
				//Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_SRC_ALPHA);
				Game.resources.textures.bindTexture("particle-fire");
				renderer.quad.render(p.getWidth, p.getHeight);
				//Gdx.gl20.glDisable(GL20.GL_BLEND);
			}
		}
		
		def renderDebug(renderer: Render2D, ent: Entity){
			val p = ent.asInstanceOf[Particle]
			
			renderer.program.setUniform("vColor", 0.0f, 1.0f, 1.0f, 0.4f);
		
			Game.resources.textures.bindTexture("blank");
			renderer.quad.render(p.getWidth, p.getHeight);
		}
	}
}