package com.bitwaffle.offworld.weapons;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.bitwaffle.moguts.entities.DynamicEntity;
import com.bitwaffle.moguts.entities.Entity;
import com.bitwaffle.moguts.physics.callbacks.ClosestHitRayCastCallback;
import com.bitwaffle.moguts.util.MathHelper;
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
	
	/** How far the pistol can shoot */
	private float range;
	
	/** Callback used for handling hits */
	private ClosestHitRayCastCallback callback;
	
	/**
	 * Create a new pistol
	 * @param owner Owner of this pistol (where the shots come from)
	 * @param damage How much damage this does when hitting stuff that implements the Health interface
	 * @param force How much force this pistol exerts on stuff it hits
	 * @param range How far the pistol can shoot
	 */
	public Pistol(Entity owner, int damage, float force, float range){
		// TODO should probably also include an 'offset' Vector2 to represent the gun's actual location
		this.owner = owner;
		this.damage = damage;
		this.force = force;
		this.range = range;
		callback = new ClosestHitRayCastCallback(owner.getLocation());
	}

	/**
	 * Shoot, man!
	 * @param target Location to shoot at
	 */
	public void shootAt(World world, Vector2 target) {
		// find the difference between the pistol's range and the distance to the given target
		float diff = range - target.dst(owner.getLocation());
		// create a difference vector and rotate it accordingly
		Vector2 clamps = new Vector2(diff, 0.0f);
		clamps.rotate(MathHelper.angle(owner.getLocation(), target));
		
		// perform raycast to clamped target
		callback.reset(owner.getLocation());
		world.rayCast(callback, owner.getLocation(), new Vector2(target.x + clamps.x, target.y + clamps.y));
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
