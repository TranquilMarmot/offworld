package com.bitwaffle.offworld.physics;

import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.bitwaffle.guts.entity.dynamic.DynamicEntity;
import com.bitwaffle.guts.entity.dynamic.particles.Particle;
import com.bitwaffle.guts.physics.PhysicsHelper;
import com.bitwaffle.offworld.weapons.pistol.PistolBullet;

/**
 * Handles all collisions
 * 
 * @author TranquilMarmot
 */
public class OffworldContactFilter implements ContactFilter{
	@Override
	public boolean shouldCollide(Fixture fix1, Fixture fix2) {
		DynamicEntity entA = PhysicsHelper.getDynamicEntity(fix1);
		DynamicEntity entB = PhysicsHelper.getDynamicEntity(fix2);
		
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
		
		return true;
	}

}
