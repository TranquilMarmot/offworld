package com.bitwaffle.guts.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.bitwaffle.guts.entities.dynamic.DynamicEntity;
import com.bitwaffle.offworld.entities.player.Player;
import com.bitwaffle.offworld.weapons.Bullet;

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
				player.getJumpSensor().beginContact();
		} else if(entB instanceof Player){
			Player player = (Player)entB;
			if(fixtureB.equals(player.getJumpSensor().fixture()))
					player.getJumpSensor().beginContact();
		}
		
		// bullet collision
		if(entA instanceof Bullet)
			((Bullet)entA).collision(contact, entB);
		else if(entB instanceof Bullet)
			((Bullet)entB).collision(contact, entA);
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
				player.getJumpSensor().endContact();
		} else if(entB instanceof Player){
			Player player = (Player)entB;
			if(fixtureB.equals(player.getJumpSensor().fixture()))
				player.getJumpSensor().endContact();
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
		
		// ignore bullets hitting what they came from
		if(entA instanceof Bullet){
			if(entB != ((Bullet)entA).getOwner())
				return true;
			else
				return false;
		} else if(entB instanceof Bullet){
			if(entA != ((Bullet)entB).getOwner())
				return true;
			else
				return false;
		}
		
		return true;
	}

}
