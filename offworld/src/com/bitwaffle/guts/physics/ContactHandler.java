package com.bitwaffle.guts.physics;

import java.util.ArrayList;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.bitwaffle.guts.Game;
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
		DynamicEntity entA = PhysicsHelper.getDynamicEntity(contact.getFixtureA());
		DynamicEntity entB = PhysicsHelper.getDynamicEntity(contact.getFixtureB());
		
		if(entA instanceof Player){
			Game.out.println("begin player contact");
			Player player = (Player)entA;
			ArrayList<Fixture> fixtures = player.body.getFixtureList();
			for(Fixture f : fixtures){
				if(f.equals(player.getJumpSensor().fixture()))
					Game.out.println("wat");
			}
		} else if(entB instanceof Player){
			Player player = (Player)entB;
			ArrayList<Fixture> fixtures = player.body.getFixtureList();
			for(Fixture f : fixtures){
				if(f.equals(player.getJumpSensor().fixture()))
					Game.out.println("wat");
			}
		}
		// bullet collision
		if(entA instanceof Bullet)
			((Bullet)entA).collision(contact, entB);
		else if(entB instanceof Bullet)
			((Bullet)entB).collision(contact, entA);
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
