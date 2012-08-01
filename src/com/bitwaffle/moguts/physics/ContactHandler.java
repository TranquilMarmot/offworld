package com.bitwaffle.moguts.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.bitwaffle.moguts.entities.DynamicEntity;
import com.bitwaffle.offworld.entities.Player;

/**
 * Handles all collisions
 * 
 * @author TranquilMarmot
 */
public class ContactHandler implements ContactListener{
	
	/**
	 * Get a dynamic entity from a fixture
	 * @param fixture Fixture to get entity from
	 * @return DynamicEntity from fixture
	 */
	private DynamicEntity getDynamicEntity(Fixture fixture){
		return (DynamicEntity)fixture.getBody().getUserData();
	}

	public void beginContact(Contact contact) {
		DynamicEntity entA = getDynamicEntity(contact.getFixtureA());
		DynamicEntity entB = getDynamicEntity(contact.getFixtureB());
		
		if(entA instanceof Player)
			((Player) entA).setCanJump(true);
		else if(entB instanceof Player)
			((Player) entB).setCanJump(true);
	}

	public void endContact(Contact contact) {
		
	}
	
	public void preSolve(Contact contact, Manifold oldManifold) {
		
	}

	public void postSolve(Contact contact, ContactImpulse impulse) {
		
		
	}

}
