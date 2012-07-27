package com.bitwaffle.moguts.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.bitwaffle.moguts.entities.DynamicEntity;
import com.bitwaffle.moguts.entities.Player;

public class ContactHandler implements ContactListener{

	public void beginContact(Contact contact) {
		DynamicEntity entA = (DynamicEntity)contact.getFixtureA().getBody().getUserData();
		DynamicEntity entB = (DynamicEntity)contact.getFixtureB().getBody().getUserData();
		
		if(entA instanceof Player)
			((Player) entA).setCanJump(true);
		else if(entB instanceof Player)
			((Player) entB).setCanJump(true);
	}

	public void endContact(Contact contact) {
		//DynamicEntity entA = (DynamicEntity)arg0.getFixtureA().getBody().getUserData();
		//DynamicEntity entB = (DynamicEntity)arg0.getFixtureB().getBody().getUserData();
	}

	public void postSolve(Contact contact, ContactImpulse impulse) {
		
		
	}

	public void preSolve(Contact contact, Manifold oldManifold) {
		
	}

}
