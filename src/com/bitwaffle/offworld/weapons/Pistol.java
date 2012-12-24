package com.bitwaffle.offworld.weapons;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.graphics.render.Render2D;
import com.bitwaffle.guts.util.MathHelper;
import com.bitwaffle.offworld.entities.dynamic.Bullet;
import com.bitwaffle.offworld.interfaces.Firearm;
import com.bitwaffle.offworld.interfaces.FirearmHolder;
import com.bitwaffle.offworld.renderers.PlayerRenderer;

/**
 * Single-shooter
 * 
 * @author TranquilMarmot
 */
public class Pistol implements Firearm {
	/** The owner of this pistol (where the shots come from) */
	private FirearmHolder owner;
	
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
	public Pistol(FirearmHolder owner, int damage, float force, float range, float firingRate){
		// TODO should probably also include an 'offset' Vector2 to represent the gun's actual location
		this.owner = owner;
		this.firingRate = firingRate;
		timeSinceLastShot = 0.0f;
		//callback = new ClosestHitRayCastCallback(owner.getLocation());
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
			
			float SPEED = 75.0f;
			Vector2 ownerLoc = owner.getFirearmOwningEntity().getLocation();
			float angle = MathHelper.angle(ownerLoc, target);
			Vector2 loc = owner.getFirearmLocation();
			Bullet bullet = new Bullet(this.owner.getFirearmOwningEntity(), loc.x, loc.y, angle, SPEED);
			Game.physics.addEntity(bullet, true);
			
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
	
	public Vector2 getTipOffset(){
		return new Vector2(0.0f, 0.0f);
	}
	
	public void render(Render2D renderer){
		boolean facingRight = Game.player.isFacingRight();
		
		// FIXME this is VERY quick and VERY dirty (PistolRenderer?)
		if(muzzleFlash){
			Matrix4f temp = new Matrix4f();
			Matrix4f.load(renderer.modelview, temp);
			renderer.modelview.translate(new Vector3f(0.6f, facingRight ? 0.09f : -0.09f, 0.0f));
			renderer.sendModelViewToShader();
			Game.resources.textures.getSubImage("muzzleflash").render(renderer.quad, 0.25f, 0.245f, facingRight, facingRight);
			
			Matrix4f.load(temp, renderer.modelview);
			renderer.sendModelViewToShader();
		}
		
		Game.resources.textures.getSubImage("pistol").render(renderer.quad, GUN_X_SCALE * PlayerRenderer.SCALE, GUN_Y_SCALE * PlayerRenderer.SCALE, !facingRight, facingRight);
	}
}
