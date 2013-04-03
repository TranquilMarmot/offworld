package com.bitwaffle.offworld.entities.player;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.dynamic.BoxEntity;
import com.bitwaffle.guts.entities.dynamic.DynamicEntity;
import com.bitwaffle.guts.graphics.EntityRenderer;
import com.bitwaffle.guts.util.MathHelper;
import com.bitwaffle.offworld.interfaces.Firearm;
import com.bitwaffle.offworld.interfaces.FirearmHolder;
import com.bitwaffle.offworld.weapons.pistol.Pistol;

/**
 * The person the person playing the game plays as while they play the game.
 * The player. Playing the game.
 * 
 * @author TranquilMarmot
 */
public class Player extends BoxEntity implements FirearmHolder{
	/** The player's current firearm */
	private Firearm firearm;
	
	// FIXME temp values
	private int pistolDamage = 10;
	private float pistolForce = 2000.0f, pistolRange = 25.0f, pistolFiringRate = 0.3f;
	
	/** 
	 * How fast the player can go on the X axis
	 * Goes both ways, so going right it can go
	 * maxVelocityX and going left it can go
	 * -maxVelocityX
	 */
	private float maxVelocityX = 15.0f;
	
	/** If the counter for this is >= 1, it means the player can jump. See JumpSensorFixture docs for more info. */
	private JumpSensor jumpSensor;
	
	/** Used to time jumps, so that the player can't jump too often */
	private float jumpTimer = 0.0f;
	/** How long the player has to wait in between jumps */
	private final float JUMP_COOLDOWN = 0.5f;
	/** How much force the player jumps with */
	private final float JUMP_FORCE = 5.0f;
	
	/** What the player is aiming at */
	private Vector2 target;
	
	/** Whether or not the player is shooting */
	private boolean isShooting;
	
	/** Whether or not the player is moving left/right */
	private boolean movingRight, movingLeft;

	/** Describes which way the player is moving/facing */
	private boolean facingRight = false;
	
	/** Animation for player's legs */
	protected PlayerBodyAnimation bodyAnimation;
	
	/** How fast the animation is played, depending on the player's linear velocity. Higher number means slower animation. */
	private float animationSpeed = 10.0f;
	
	/** How fast the player has to be moving for the animation to be stepped */
	private float minAnimationVelocity = 0.5f;
	
	/** To infinity and yada yada */
	public Jetpack jetpack;
	
	/**
	 * Noargs constructor ONLY to be used with serialization!!!
	 */
	public Player(){
		super();
		init();
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
		init();
	}
	
	/**
	 * Init method only used in constructor
	 */
	private void init(){
		movingRight = false;
		movingLeft = false;
		
		firearm = new Pistol(this, pistolDamage, pistolForce, pistolRange, pistolFiringRate);
		target = new Vector2();
		
		bodyAnimation = new PlayerBodyAnimation(Game.resources.textures.getAnimation("player-body"), this);
		
		jetpack = new Jetpack(this);
	}
	
	@Override
	public void init(World world){
		super.init(world);
		
		// don't want out player rotating all willy nilly now, do we?
		this.body.setFixedRotation(true);
		
		// creating the jump sensor adds it as a fixture to the player's body
		jumpSensor = new JumpSensor(this);
	}
	
	/**
	 * @return The player's jump sensor
	 */
	public JumpSensor getJumpSensor(){
		return jumpSensor;
	}
	
	@Override
	public void update(float timeStep){
		super.update(timeStep);
		
		// update animation
		if(body != null){
			Vector2 linVec = body.getLinearVelocity();
			if((linVec.x > minAnimationVelocity || linVec.x < -minAnimationVelocity)){
				float animationStep = timeStep * (linVec.x / animationSpeed);
				if(!facingRight) animationStep = -animationStep;
				bodyAnimation.update(animationStep);
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
		
		jetpack.update(timeStep);
		
		// apply forces based on toggles
		if(movingLeft)
			applyLeftForce();
		if(movingRight)
			applyRightForce();
	}
	
	/**
	 * Make the player jump
	 */
	public boolean jump(){
		// we can only jump if the game isn't paused and if the timer is done
		if(!Game.isPaused() && jumpTimer >= JUMP_COOLDOWN){
			// if there's a contact, jump!
			if(jumpSensor.numContacts() >= 1){
				Vector2 linVec = body.getLinearVelocity();
				// can only jump if the current vertical speed is within a certain range
				if(linVec.y <= JUMP_FORCE && linVec.y >= -JUMP_FORCE){
					//Game.vibration.vibrate(25);
					Game.resources.sounds.getSound("jump").play();
					// add force to current velocity and set it
					linVec.y += JUMP_FORCE;
					body.setLinearVelocity(linVec);
					return true;
				}
				
				// don't forget to reset the timer
				jumpTimer = 0.0f;
			}
		}
		return false;
	}
	
	/**
	 * Move the player left
	 * Should be called every frame that the player is moving,
	 * i.e. when a button is being held down
	 */
	private void applyLeftForce(){
		if(!Game.isPaused()){
			Vector2 linVec = body.getLinearVelocity();
			if(linVec.x > -maxVelocityX) {
				linVec.x -= 0.5f;
				body.setLinearVelocity(linVec);
			}
		}
	}
	
	/**
	 * Move the player right
	 * Should be called every frame that the player is moving,
	 * i.e. when a button is being held down
	 */
	private void applyRightForce(){
		if(!Game.isPaused()){
			Vector2 linVec = body.getLinearVelocity();
			if(linVec.x < maxVelocityX) {
				linVec.x += 0.5f;
				body.setLinearVelocity(linVec);
			}
		}
	}
	
	/** Start moving right */
	public void moveRight(){ movingRight = true; }
	/** Start moving left */
	public void moveLeft(){ movingLeft = true; }
	/** Stop moving right */
	public void stopMovingRight(){ movingRight = false; }
	/** Stop moving left */
	public void stopMovingLeft(){ movingLeft = false; }
	/** @return Whether or not the player is moving right */
	public boolean isMovingRight(){ return movingRight; }
	/** @return Whether or not the player is moving left */
	public boolean isMovingLeft(){ return movingLeft; }

	/**
	 * @return Whether or not the player is facing right
	 */
	public boolean isFacingRight(){
		return facingRight;
	}
	
	/**
	 * Start shootin'!
	 * @param target Target to start shooting at
	 */
	public void beginShooting(Vector2 target){
		this.setTarget(target);
		beginShooting();
	}
	
	/**
	 * Start shootin'!
	 */
	public void beginShooting(){
		isShooting = true;
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
	public void setTarget(Vector2 target){
		if(!Game.isPaused())
			this.target.set(target);
	}
	
	/**
	 * @return The player's current weapon
	 */
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
	
	/**
	 * Get the current location of the player's firearm, in world coordinates.
	 * Does the same translations as in PlayerRenderer to get to firearm's location.
	 */
	@Override
	public Vector2 getFirearmLocation(){
		// the same translations as in the player renderer to get to the firearm
		Vector2 rArmLoc = bodyAnimation.getCurrentRShoulderLocation();
		Vector2 gunOffset = bodyAnimation.getGunOffset();
		float armAngle = getArmAngle();
		Matrix4 tempMat = new Matrix4();
		tempMat.idt();
		tempMat.translate(this.location.x, this.location.y, 0.0f);
		tempMat.translate(facingRight ? rArmLoc.x : -rArmLoc.x, rArmLoc.y, 0.0f);
		tempMat.rotate(0.0f, 0.0f, 1.0f, armAngle);
		tempMat.translate(gunOffset.x, facingRight ? gunOffset.y : -gunOffset.y, 0.0f);
		return new Vector2(tempMat.getValues()[Matrix4.M03], tempMat.getValues()[Matrix4.M13]);
	}
	
	@Override
	public DynamicEntity getFirearmOwningEntity(){
		return this;
	}
	
	@Override
	public float getFirearmAngle(){
		return getArmAngle();
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
	
	@Override
	public void cleanup(){
		super.cleanup();
		jetpack.cleanup();
	}
}