package com.bitwaffle.guts.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.bitwaffle.guts.entities.dynamic.DynamicEntity;
import com.bitwaffle.guts.util.PhysicsHelper;
import com.bitwaffle.offworld.entities.dynamic.Bullet;

/**
 * Handles all collisions
 * 
 * @author TranquilMarmot
 */
public class ContactHandler implements ContactListener, ContactFilter{
	/** From ContactListener*/
	public void beginContact(Contact contact) {
		DynamicEntity entA = PhysicsHelper.getDynamicEntity(contact.getFixtureA());
		DynamicEntity entB = PhysicsHelper.getDynamicEntity(contact.getFixtureB());
		
		// bullet collision
		if(entA instanceof Bullet)
			Bullet.bulletCollision(contact, (Bullet)entA, entB);
		else if(entB instanceof Bullet)
			Bullet.bulletCollision(contact, (Bullet)entB, entA);
	}

	public void endContact(Contact contact) {
		
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
