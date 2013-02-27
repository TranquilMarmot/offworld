package com.bitwaffle.offworld.entities.player;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.dynamic.BoxEntity;
import com.bitwaffle.guts.entities.dynamic.DynamicEntity;
import com.bitwaffle.guts.graphics.EntityRenderer;
import com.bitwaffle.guts.graphics.animation.Animation;
import com.bitwaffle.guts.input.KeyBindings;
import com.bitwaffle.guts.physics.callbacks.FirstHitQueryCallback;
import com.bitwaffle.guts.util.MathHelper;
import com.bitwaffle.offworld.interfaces.Firearm;
import com.bitwaffle.offworld.interfaces.FirearmHolder;
import com.bitwaffle.offworld.weapons.Pistol;

/**
 * The person the person playing the game plays as while they play the game.
 * The player. Playing the game.
 * 
 * @author TranquilMarmot
 */
public class Player extends BoxEntity implements FirearmHolder{
	/** The player's current firearm */
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
	
	/** Describes which way the player is moving/facing */
	private boolean movingRight = false, facingRight = false;
	
	/** Animation for player's legs */
	public Animation bodyAnimation, rArmAnimation, lArmAnimation;
	
	/**
	 * Noargs constructor ONLY to be used with serialization!!!
	 */
	public Player(){
		super();
		// TODO get rid of these magic numbers
		firearm = new Pistol(this, 20, 2000.0f, 25.0f, 0.3f);
		bodyAnimation = Game.resources.textures.getAnimation("player-body");
		rArmAnimation = Game.resources.textures.getAnimation("player-r-arm");
		lArmAnimation = Game.resources.textures.getAnimation("player-l-arm");
		target = new Vector2();
	}
	
	/**
	 * Create a new Player instance
	 * @param bodyDef Definition of player's body
	 * @param width Width of player
	 * @param height Height of player
	 * @param fixtureDef Definition for player's fixture
	 */
	public Player(EntityRenderer renderer, int layer, BodyDef bodyDef, float width, float height,
			FixtureDef fixtureDef) {
		super(renderer, layer, bodyDef, width, height, fixtureDef);
		
		// TODO get rid of these magic numbers
		firearm = new Pistol(this, 20, 2000.0f, 25.0f, 0.3f);
		bodyAnimation = Game.resources.textures.getAnimation("player-body");
		rArmAnimation = Game.resources.textures.getAnimation("player-r-arm");
		lArmAnimation = Game.resources.textures.getAnimation("player-l-arm");
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
			if((linVec.x > 0.5f || linVec.x < -0.5f)){
				// FIXME should the animation speed be defined in the animation's XML?
				float animationStep = timeStep * (linVec.x / 10.0f);
				if(!facingRight) animationStep = -animationStep;
				bodyAnimation.update(animationStep);
				rArmAnimation.update(animationStep);
				lArmAnimation.update(animationStep);
			}
			
			// update the location of the target so it moves with the player
			target.x += linVec.x * timeStep;
			target.y += linVec.y * timeStep;
		}
		
		// update which direction the player is facing
		facingRight = target.x >= this.location.x;
		
		// update and shoot pistol
		if(firearm != null){
			firearm.update(timeStep);
			if(this.isShooting)
				firearm.shootAt(body.getWorld(), target);
		}
		
		// add time to jump timer
		jumpTimer += timeStep;
		
		// check for input
		if(!Game.gui.console.isOn()){
			if(KeyBindings.CONTROL_RIGHT.isPressed())
				goRight();
			if(KeyBindings.CONTROL_LEFT.isPressed())
				goLeft();
			if(KeyBindings.CONTROL_JUMP.pressedOnce())
				jump();
		}
	}
	
	/**
	 * Move the player left
	 * Should be called every frame that the player is moving,
	 * i.e. when a button is being held down
	 */
	public void goLeft(){
		if(!Game.isPaused()){
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
		if(!Game.isPaused()){
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
		if(!Game.isPaused() && jumpTimer >= JUMP_COOLDOWN){
			// perform an AABB query underneath the player's feet
			Vector2 underneath = new Vector2(this.location.x, this.location.y - this.height * 1.25f);
			FirstHitQueryCallback callback = new FirstHitQueryCallback();
			this.body.getWorld().QueryAABB(callback, 
			                             underneath.x - this.width,
			                             underneath.y - 0.01f,
			                             underneath.x + this.width,
			                             underneath.y + 0.01f);
			
			// if there's a hit, jump!
			//if(callback.getHit() != null && callback.getHit() != this){
				Vector2 linVec = body.getLinearVelocity();
				// can only jump if the current vertical speed is within a certain range
				if(linVec.y <= JUMP_FORCE && linVec.y >= -JUMP_FORCE){
					//Game.vibration.vibrate(25);
					Game.resources.sounds.getSound("jump").play();
					
					// add force to current velocity and set it
					linVec.y += JUMP_FORCE;
					body.setLinearVelocity(linVec);
				}
				
				// don't forget to reset the timer
				jumpTimer = 0.0f;
			//}
				
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
		if(!Game.isPaused())
			this.target.set(target);
	}
	
	public Firearm getCurrentFirearm(){
		return firearm;
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
	
	public Vector2 getFirearmLocation(){
		// the same translations as in the player renderer to get to the pistol
		Matrix4 tempMat = new Matrix4();
		tempMat.idt();
		tempMat.translate(this.location.x, this.location.y, 0.0f);
		tempMat.translate(facingRight ? PlayerRenderer.L_ARM_X_OFFSET : -PlayerRenderer.L_ARM_X_OFFSET, PlayerRenderer.L_ARM_Y_OFFSET, 0.0f);
		tempMat.rotate(0.0f, 0.0f, 1.0f, this.getArmAngle());
		tempMat.translate(PlayerRenderer.ARM_ROTATION_X_OFFSET, facingRight ? PlayerRenderer.ARM_ROTATION_Y_OFFSET : -PlayerRenderer.ARM_ROTATION_Y_OFFSET, 0.0f);
		tempMat.translate(new Vector3(PlayerRenderer.GUN_X_OFFSET, facingRight ?  PlayerRenderer.GUN_Y_OFFSET  : -PlayerRenderer.GUN_Y_OFFSET, 0.0f));
		return new Vector2(tempMat.getValues()[Matrix4.M03], tempMat.getValues()[Matrix4.M13]);
	}
	
	public DynamicEntity getFirearmOwningEntity(){
		return this;
	}
	
	public float getFirearmAngle(){
		return getArmAngle();
	}
	
	@Override
	public void cleanup(){
		super.cleanup();
		
		// FIXME This should not be here...
		Game.player = null;
	}
	
	/*
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
	*/
}