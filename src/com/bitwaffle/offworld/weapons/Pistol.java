package com.bitwaffle.offworld.weapons;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.moguts.entities.DynamicEntity;
import com.bitwaffle.moguts.entities.Entity;
import com.bitwaffle.moguts.physics.callbacks.ClosestHitCallback;
import com.bitwaffle.offworld.Game;
import com.bitwaffle.offworld.interfaces.Firearm;
import com.bitwaffle.offworld.interfaces.Health;

/**
 * Single-shooter
 * 
 * @author TranquilMarmot
 */
public class Pistol implements Firearm {
	/** The owner of this pistol (where the shots come from) */
	private Entity owner;
	
	/** How much damage this pistol does to anything implementing Health */
	private int damage;
	
	/** Force this pistol exerts on whatever it hits (makes things move away) */
	private float force;
	
	/** Callback used for handling hits */
	private ClosestHitCallback callback;
	
	/**
	 * Create a new pistol
	 * @param owner Owner of this pistol (where the shots come from)
	 * @param damage How much damage this does when hitting stuff that implements the Health interface
	 * @param force How much force this pistol exerts on stuff it hits
	 */
	public Pistol(Entity owner, int damage, float force){
		// TODO should probably also include an 'offset' Vector2 to represent gun's actual location
		this.owner = owner;
		this.damage = damage;
		this.force = force;
		callback = new ClosestHitCallback(owner.getLocation());
	}

	
	public void shootAt(Vector2 target) {
		callback.reset(owner.getLocation());
		
		Game.physics.world.rayCast(callback, owner.getLocation(), target);
		
		DynamicEntity hit = callback.getClosestHit();
		if(hit != null){
			Vector2 normal = callback.normalOnClosest();
			Vector2 point = callback.pointOnClosest();
			hit.body.applyForce(new Vector2(normal.x * -force, normal.y * -force), point);
			
			if(hit instanceof Health)
				((Health)hit).hurt(this.damage);
		}
	}

}
