package com.bitwaffle.offworld.physics;

import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.bitwaffle.guts.entity.dynamic.DynamicEntity;
import com.bitwaffle.guts.entity.dynamic.particles.Particle;
import com.bitwaffle.guts.physics.PhysicsHelper;
import com.bitwaffle.offworld.entities.enemies.bat.Bat;
import com.bitwaffle.offworld.entities.player.Player;
import com.bitwaffle.offworld.weapons.pistol.PistolBullet;

/**
 * Handles all collisions
 * 
 * @author TranquilMarmot
 */
public class OffworldContactFilter implements ContactFilter{
	@Override
	public boolean shouldCollide(Fixture fixA, Fixture fixB) {
		DynamicEntity entA = PhysicsHelper.getDynamicEntity(fixA);
		DynamicEntity entB = PhysicsHelper.getDynamicEntity(fixB);
		
		if(entA instanceof Particle)
			return particleCollision((Particle)entA, fixA, entB, fixB);
		else if(entB instanceof Particle)
			return particleCollision((Particle)entB, fixB, entA, fixB);
		
		if(entA instanceof PistolBullet)
			return pistolBulletCollision((PistolBullet)entA, fixA, entB, fixB);
		else if(entB instanceof PistolBullet)
			return pistolBulletCollision((PistolBullet)entB, fixB, entA, fixA);
		
		if(entA instanceof Bat)
			return batCollision((Bat)entA, fixA, entB, fixB);
		else if(entB instanceof Bat)
			return batCollision((Bat)entB, fixB, entA, fixA);
		
		
		return true;
	}
	
	private boolean particleCollision(Particle particle, Fixture particleFix, DynamicEntity other, Fixture otherFix){
		// ignore particles hitting their emitter and any bullets
		if(other != particle.getEmitter().settings.attached && !(other instanceof PistolBullet))
			return true;
		else
			return false;
	}
	
	private boolean pistolBulletCollision(PistolBullet bullet, Fixture bulletFix, DynamicEntity other, Fixture otherFix){
		// ignore bullets hitting what they came from and hitting sensors
		if(!otherFix.isSensor() && other != bullet.getOwner())
			return true;
		else
			return false;
	}
	
	private boolean batCollision(Bat bat, Fixture batFixture, DynamicEntity other, Fixture otherFixture){
		// ignore anything hitting bat sensor except a player
		if(batFixture == bat.sleepState.playerSensor){
			if(other instanceof Player)
				return true;
			else
				return false;
		} else {
			if(other instanceof Bat){
				Bat ob = (Bat)other;
				if(ob.ai.currentState() == ob.sleepState)
					return false;
				else
					return true;
			} else{
				return true;
			}
		}
	}

}
