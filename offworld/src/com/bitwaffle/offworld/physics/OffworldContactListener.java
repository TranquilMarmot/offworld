package com.bitwaffle.offworld.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.bitwaffle.guts.entity.dynamic.DynamicEntity;
import com.bitwaffle.guts.physics.PhysicsHelper;
import com.bitwaffle.offworld.entities.player.Player;
import com.bitwaffle.offworld.weapons.pistol.PistolBullet;

public class OffworldContactListener implements ContactListener {
	@Override
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

	@Override
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
	
	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		
	}
	
	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		
	}
}
