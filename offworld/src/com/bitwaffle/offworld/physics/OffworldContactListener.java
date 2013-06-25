package com.bitwaffle.offworld.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.bitwaffle.guts.entity.dynamic.DynamicEntity;
import com.bitwaffle.guts.physics.PhysicsHelper;
import com.bitwaffle.offworld.camera.CameraChangeSensor;
import com.bitwaffle.offworld.entities.enemies.bat.Bat;
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
		if(entA instanceof Player)
			beginPlayerContact((Player)entA, fixtureA, entB, fixtureB);
		else if(entB instanceof Player)
			beginPlayerContact((Player)entB, fixtureB, entA, fixtureA);
		
		// bullet collision
		if(entA instanceof PistolBullet)
			((PistolBullet)entA).collision(contact, entB);
		else if(entB instanceof PistolBullet)
			((PistolBullet)entB).collision(contact, entA);
		
		// bat collision
		if(entA instanceof Bat)
			beginBatContact((Bat)entA, fixtureA, entB, fixtureB);
		else if(entB instanceof Bat)
			beginBatContact((Bat)entB, fixtureB, entA, fixtureA);
	}

	@Override
	public void endContact(Contact contact) {
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		DynamicEntity entA = PhysicsHelper.getDynamicEntity(fixtureA);
		DynamicEntity entB = PhysicsHelper.getDynamicEntity(fixtureB);
		
		// check if it's the player's jump sensor and decrement if it is
		if(entA instanceof Player)
			endPlayerContact((Player)entA, fixtureA, entB, fixtureB);
		else if(entB instanceof Player)
			endPlayerContact((Player)entB, fixtureB, entA, fixtureA);
	}
	
	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		
	}
	
	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		
	}
	
	/** Called when player hits something */
	private static void beginPlayerContact(Player player, Fixture playerFixture, DynamicEntity contact, Fixture contactFixture){
		// report any camera change sensor hits
		if(contact instanceof CameraChangeSensor)
			((CameraChangeSensor)contact).beginContact(player);
		
		// report fixtures to jump sensor
		else if(playerFixture.equals(player.getJumpSensor().fixture()))
			player.getJumpSensor().beginContact(contact);
		
		// report pickups to pickup sensor
		else if(playerFixture.equals(player.getPickupSensor().fixture()))
			player.getPickupSensor().reportContact(contact);
	}
	
	private static void endPlayerContact(Player player, Fixture playerFixture, DynamicEntity contact, Fixture contactFixture){
		//if(contactFixture.getUserData() instanceof CameraChangeSensor)
			
			
		if(playerFixture.equals(player.getJumpSensor().fixture()))
			player.getJumpSensor().endContact(contact);
	}
	
	/** Called when bat hits something */
	private static void beginBatContact(Bat bat, Fixture batFixture, DynamicEntity contact, Fixture contactFixture){
		if(contact instanceof Player && batFixture == bat.sleepState.playerSensor){
			bat.startAttacking(contact);
		} else if(contact instanceof PistolBullet){
			bat.startAttacking(((PistolBullet)contact).getOwner());
		}
	}
}
