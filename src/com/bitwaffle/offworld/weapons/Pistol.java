package com.bitwaffle.offworld.weapons;

import android.opengl.Matrix;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.bitwaffle.moguts.entities.Entity;
import com.bitwaffle.moguts.entities.dynamic.DynamicEntity;
import com.bitwaffle.moguts.entities.passive.Decal;
import com.bitwaffle.moguts.graphics.render.Render2D;
import com.bitwaffle.moguts.graphics.render.Renderers;
import com.bitwaffle.moguts.physics.callbacks.ClosestHitRayCastCallback;
import com.bitwaffle.moguts.util.BufferUtils;
import com.bitwaffle.moguts.util.MathHelper;
import com.bitwaffle.offworld.Game;
import com.bitwaffle.offworld.interfaces.Firearm;
import com.bitwaffle.offworld.interfaces.Health;
import com.bitwaffle.offworld.renderers.PlayerRenderer;

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
	
	/** How fast this pistol can shoot */
	private float firingRate;
	
	/** How long it's been since the last shot, in seconds */
	private float timeSinceLastShot;
	
	/** Whether or not there's currently a muzzle flash */
	boolean muzzleFlash;
	
	/** How long the flash lives and a timer to know when to turn it off */
	float flashTTL = 0.1f, flashLived; 
	
	// FIXME temp?
	private final float 		
		GUN_X_SCALE = 0.363f,
		GUN_Y_SCALE = 0.25f;
	
	/**
	 * Create a new pistol
	 * @param owner Owner of this pistol (where the shots come from)
	 * @param damage How much damage this does when hitting stuff that implements the Health interface
	 * @param force How much force this pistol exerts on stuff it hits
	 * @param range How far the pistol can shoot
	 */
	public Pistol(Entity owner, int damage, float force, float range, float firingRate){
		// TODO should probably also include an 'offset' Vector2 to represent the gun's actual location
		this.owner = owner;
		this.damage = damage;
		this.force = force;
		this.range = range;
		this.firingRate = firingRate;
		timeSinceLastShot = 0.0f;
		callback = new ClosestHitRayCastCallback(owner.getLocation());
		flashLived = 0.0f;
	}

	/**
	 * Shoot, man!
	 * @param target Location to shoot at
	 */
	public void shootAt(World world, Vector2 target) {
		if(timeSinceLastShot >= firingRate){
			Game.resources.sounds.play("shoot");
			muzzleFlash = true;
			
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
				
				// add spark decal at hit
				Game.physics.addEntity(new Decal(
						Renderers.SPARK,
						owner.getLayer() + 1,
						point,
						MathHelper.toRadians(normal.angle()),
						0.1f)
				);
				
				if(hit instanceof Health)
					((Health)hit).hurt(this.damage);
			}
			// reset shot timer
			timeSinceLastShot = 0.0f;
		}
	}
	
	public void update(float timeStep){
		// tick shot timer
		timeSinceLastShot += timeStep;
		
		// tick muzzle flash timer if necessary
		if(muzzleFlash){
			flashLived += timeStep;
			if(flashLived >= flashTTL){
				flashLived = 0.0f;
				muzzleFlash = false;
			}
		}
	}
	
	public void render(Render2D renderer){
		boolean facingRight = Game.player.isFacingRight();
		
		// FIXME this is VERY quick and VERY dirty
		if(muzzleFlash){
			float[] tmp = new float[16];
			BufferUtils.deepCopyFloatArray(renderer.modelview, tmp);
			Matrix.translateM(renderer.modelview, 0, 0.6f, facingRight ? 0.09f : -0.09f, 0.0f);
			renderer.sendModelViewToShader();
			Game.resources.textures.getSubImage("muzzleflash").render(renderer.quad, 0.25f, 0.245f, facingRight, facingRight);
			
			BufferUtils.deepCopyFloatArray(tmp, renderer.modelview);
			renderer.sendModelViewToShader();
		}
		
		Game.resources.textures.getSubImage("pistol").render(renderer.quad, GUN_X_SCALE * PlayerRenderer.SCALE, GUN_Y_SCALE * PlayerRenderer.SCALE, !facingRight, facingRight);
	}
}
