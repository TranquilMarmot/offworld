package com.bitwaffle.guts.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.bitwaffle.guts.entity.dynamic.DynamicEntity;
import com.bitwaffle.guts.entity.particles.Particle;
import com.bitwaffle.offworld.entities.player.Player;
import com.bitwaffle.offworld.weapons.pistol.PistolBullet;

/**
 * Handles all collisions
 * 
 * @author TranquilMarmot
 */
public class ContactHandler implements ContactListener, ContactFilter{
	/** From ContactListener*/
	public void beginContact(Contact contact) {
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		DynamicEntity entA = PhysicsHelper.getDynamicEntity(fixtureA);
		DynamicEntity entB = PhysicsHelper.getDynamicEntity(fixtureB);
		
		// check if it's the player's jump sensor and increment if it is
		if(entA instanceof Player){
			Player player = (Player)entA;
			if(fixtureA.equals(player.getJumpSensor().fixture()))
				player.getJumpSensor().beginContact(entB);
			else if(fixtureA.equals(player.getPickupSensor().fixture()))
				player.getPickupSensor().reportContact(entB);
		} else if(entB instanceof Player){
			Player player = (Player)entB;
			if(fixtureB.equals(player.getJumpSensor().fixture()))
					player.getJumpSensor().beginContact(entA);
			else if(fixtureB.equals(player.getPickupSensor().fixture()))
				player.getPickupSensor().reportContact(entA);
		}
		
		// bullet collision
		if(entA instanceof PistolBullet)
			((PistolBullet)entA).collision(contact, entB);
		else if(entB instanceof PistolBullet)
			((PistolBullet)entB).collision(contact, entA);
	}

	public void endContact(Contact contact) {
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		DynamicEntity entA = PhysicsHelper.getDynamicEntity(fixtureA);
		DynamicEntity entB = PhysicsHelper.getDynamicEntity(fixtureB);
		
		// check if it's the player's jump sensor and decrement if it is
		if(entA instanceof Player){
			Player player = (Player)entA;
			if(fixtureA.equals(player.getJumpSensor().fixture()))
				player.getJumpSensor().endContact(entB);
		} else if(entB instanceof Player){
			Player player = (Player)entB;
			if(fixtureB.equals(player.getJumpSensor().fixture()))
				player.getJumpSensor().endContact(entA);
		}
	}
	
	public void preSolve(Contact contact, Manifold oldManifold) {
		
	}

	public void postSolve(Contact contact, ContactImpulse impulse) {
		
		
	}

	/** From ContactFilter */
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
