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
		
		// ignore particles hitting their emitter and any bullets
		if(entA instanceof Particle){
			if(entB != ((Particle)entA).getOwner() && !(entB instanceof PistolBullet))
				return true;
			else
				return false;
		} else if(entB instanceof Particle){
			if(entA != ((Particle)entB).getOwner() && !(entA instanceof PistolBullet))
				return true;
			else
				return false;
		}
		
		// ignore bullets hitting what they came from
		if(entA instanceof PistolBullet){
			if(entB != ((PistolBullet)entA).getOwner())
				return true;
			else
				return false;
		} else if(entB instanceof PistolBullet){
			if(entA != ((PistolBullet)entB).getOwner())
				return true;
			else
				return false;
		}
		
		// ignore anything hitting bat sensor
		if(entA instanceof Bat){
			if(fixA == ((Bat)entA).sleepState.playerSensor){
				if(entB instanceof Player)
					return true;
				else
					return false;
			} else {
				return true;
			}
		} else if(entB instanceof Bat){
			if(fixB == ((Bat)entB).sleepState.playerSensor){
				if(entA instanceof Player)
					return true;
				else
					return false;
			} else {
				return true;
			}
		}
		
		
		return true;
	}

}
