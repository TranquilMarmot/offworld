package com.bitwaffle.offworld.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.bitwaffle.moguts.entities.BoxEntity;
import com.bitwaffle.moguts.graphics.render.renderers.Renderers;
import com.bitwaffle.moguts.graphics.textures.animation.Animation;
import com.bitwaffle.moguts.physics.callbacks.FirstHitQueryCallback;
import com.bitwaffle.moguts.util.MathHelper;
import com.bitwaffle.offworld.Game;
import com.bitwaffle.offworld.interfaces.Firearm;
import com.bitwaffle.offworld.weapons.Pistol;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * Player class
 * 
 * @author TranquilMarmot
 */
public class Player extends BoxEntity implements KryoSerializable{
	// FIXME these are temp
	private static float[] defaultColor = { 1.0f, 1.0f, 1.0f, 1.0f };
	private boolean movingRight = false, facingRight = false;
	private Firearm firearm;
	
	/** 
	 * How fast the player can go on the X axis
	 * Goes both ways, so going right it can go
	 * maxVelocityX and going left it can go
	 * -maxVelocityX
	 */
	private float maxVelocityX = 15.0f;
	
	/** Used to time jumps, so that the player can't jump too often */
	private float jumpTimer = 0.0f;
	/** How long the player has to wait in between jumps */
	private final float JUMP_COOLDOWN = 0.5f;
	/** How much force the player jumps with */
	private final float JUMP_FORCE = 7.5f;
	
	/** What the player is aiming at */
	private Vector2 target;
	
	/** Whether or not the player is shooting */
	private boolean isShooting;
	
	/** Animation for player */
	public Animation legsAnimation;
	
	public Player(){
		super();
		firearm = new Pistol(this, 20, 2000.0f, 25.0f, 0.3f);
		this.color = defaultColor;
		legsAnimation = Game.resources.textures.getAnimation("playerlegs");
		target = new Vector2();
	}
	
	/**
	 * Create a new Player instance
	 * @param bodyDef Definition of player's body
	 * @param width Width of player
	 * @param height Height of player
	 * @param fixtureDef Definition for player's fixture
	 */
	public Player(Renderers renderer, BodyDef bodyDef, float width, float height,
			FixtureDef fixtureDef) {
		super(renderer, bodyDef, width, height, fixtureDef, defaultColor);
		
		this.color = defaultColor;
		firearm = new Pistol(this, 20, 2000.0f, 25.0f, 0.3f);
		legsAnimation = Game.resources.textures.getAnimation("playerlegs");
		target = new Vector2();
	}
	
	@Override
	public void init(World world){
		super.init(world);
		
		// don't want out player rotating all willy nilly now, do we?
		this.body.setFixedRotation(true);
	}
	
	@Override
	public void update(float timeStep){
		super.update(timeStep);
		
		// update animation
		if(body != null){
			Vector2 linVec = body.getLinearVelocity();
			if(linVec.x > 0.5f || linVec.x < -0.5f){
				// FIXME should this be defined in the animation's XML?
				float animationStep = timeStep * Math.abs(linVec.x / 15.0f);
				legsAnimation.updateAnimation(animationStep);
			}
		}
		
		// update which direction the player is facing
		if(target.x >= this.location.x)
			facingRight = true;
		else
			facingRight = false;
		
		// update and shoot pistol
		if(firearm != null){
			firearm.update(timeStep);
			if(this.isShooting)
				shoot(target);
		}
		
		// add time to jump timer
		jumpTimer += timeStep;
	}
	
	/**
	 * Move the player left
	 * Should be called every frame that the player is moving,
	 * i.e. when a button is being held down
	 */
	public void goLeft(){
		if(!Game.paused){
			Vector2 linVec = body.getLinearVelocity();
			if(linVec.x > -maxVelocityX) {
				linVec.x -= 0.5f;
				body.setLinearVelocity(linVec);
				movingRight = false;
			}
		}
	}
	
	/**
	 * Move the player right
	 * Should be called every frame that the player is moving,
	 * i.e. when a button is being held down
	 */
	public void goRight(){
		if(!Game.paused){
			Vector2 linVec = body.getLinearVelocity();
			if(linVec.x < maxVelocityX) {
				linVec.x += 0.5f;
				body.setLinearVelocity(linVec);
				movingRight = true;
			}
		}
	}
	
	/**
	 * Make the player jump
	 */
	public void jump(){
		// we can only jump if the game isn't paused and if the timer is done
		if(!Game.paused && jumpTimer >= JUMP_COOLDOWN){
			// perform an AABB query underneath the player's feet
			Vector2 underneath = new Vector2(this.location.x, this.location.y - this.height * 2.0f);
			FirstHitQueryCallback callback = new FirstHitQueryCallback();
			this.body.getWorld().QueryAABB(callback, 
			                             underneath.x - this.width,
			                             underneath.y - 0.01f,
			                             underneath.x + this.width,
			                             underneath.y + 0.01f);
			
			// if there's a hit, jump!
			if(callback.getHit() != null && callback.getHit() != this){
				Vector2 linVec = body.getLinearVelocity();
				// can only jump if the current vertical speed is within a certain range
				if(linVec.y <= JUMP_FORCE && linVec.y >= -JUMP_FORCE){
					Game.vibration.vibrate(25);
					Game.resources.sounds.play("jump");
					
					// add force to current velocity and set it
					linVec.y += JUMP_FORCE;
					body.setLinearVelocity(linVec);
				}
				
				// don't forget to reset the timer
				jumpTimer = 0.0f;
			}
		}
	}
	
	/**
	 * @return Whether or not the player is moving left
	 */
	public boolean isMovingRight(){
		return movingRight;
	}
	
	/**
	 * @return Whether or not the player is facing right
	 */
	public boolean isFacingRight(){
		return facingRight;
	}
	
	/**
	 * Start shooting
	 * @param target Target to start shooting at
	 */
	public void beginShooting(Vector2 target){
		isShooting = true;
		this.updateTarget(target);
	}
	
	/**
	 * Stop shooting
	 */
	public void endShooting(){
		isShooting = false;
	}
	
	/**
	 * @return Whether or not the player is shooting
	 */
	public boolean isShooting(){
		return isShooting;
	}
	
	/**
	 * Updates where the player is aiming
	 * @param target New spot to aim at
	 */
	public void updateTarget(Vector2 target){
		if(!Game.paused)
			this.target.set(target);
	}
	
	/**
	 * @return Where the player is aiming
	 */
	public Vector2 getCurrentTarget(){
		return target;
	}
	
	/**
	 * @return The angle from the player to the player's target
	 */
	public float getArmAngle(){
		return MathHelper.angle(this.getLocation(), this.target);
	}
	
	
	/**
	 * Pew pew!
	 * @param target World-space vector to shoot towards
	 */
	private void shoot(Vector2 target){
		firearm.shootAt(body.getWorld(), target);
	}
	
	@Override
	public void write(Kryo kryo, Output output){
		super.write(kryo, output);
		
		kryo.writeObject(output, this.target);
	}
	
	@Override
	public void read(Kryo kryo, Input input){
		super.read(kryo, input);
		
		this.target.set(kryo.readObject(input, Vector2.class));
	}
}