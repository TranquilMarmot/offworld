package com.bitwaffle.offworld.entities.player;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.dynamic.BoxEntity;
import com.bitwaffle.guts.entities.dynamic.DynamicEntity;
import com.bitwaffle.guts.physics.CollisionFilters;
import com.bitwaffle.guts.util.MathHelper;
import com.bitwaffle.offworld.entities.player.render.PlayerBodyAnimation;
import com.bitwaffle.offworld.entities.player.render.PlayerRenderer;
import com.bitwaffle.offworld.interfaces.Firearm;
import com.bitwaffle.offworld.interfaces.FirearmHolder;
import com.bitwaffle.offworld.interfaces.Health;
import com.bitwaffle.offworld.weapons.pistol.Pistol;

/**
 * The person the person playing the game plays as while they play the game.
 * The player. Playing the game.
 * 
 * @author TranquilMarmot
 */
public class Player extends BoxEntity implements FirearmHolder, Health{
	/** Size of player's hitbox */
	private static final float WIDTH = 0.52062f, HEIGHT = 1.8034f;
	
	/** The player's current firearm */
	private Firearm firearm;
	
	/** Player's current health */
	private float health;
	
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
	public Player(int layer, Vector2 location) {
		super(new PlayerRenderer(), layer, getBodyDef(location), WIDTH, HEIGHT, getFixtureDef());
		init();
	}
	
	private static BodyDef getBodyDef(Vector2 location){
		BodyDef playerBodyDef = new BodyDef();
		playerBodyDef.type = BodyDef.BodyType.DynamicBody;
		playerBodyDef.position.set(location);
		
		return playerBodyDef;
	}
	
	private static FixtureDef getFixtureDef(){
		FixtureDef playerFixture = new FixtureDef();
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(WIDTH, HEIGHT);
		playerFixture.shape = boxShape;
		
		playerFixture.density = 1.0f;
		playerFixture.friction = 0.3f;
		playerFixture.restitution = 0.0f;
		playerFixture.filter.categoryBits = CollisionFilters.PLAYER;
		playerFixture.filter.maskBits = CollisionFilters.EVERYTHING;
		return playerFixture;
	}
	
	/**
	 * Init method only used in constructor
	 */
	private void init(){
		health = 100.0f;
		movingRight = false;
		movingLeft = false;
		
		// FIXME temp pistol
		firearm = new Pistol(this);
		target = new Vector2();
		
		bodyAnimation = new PlayerBodyAnimation(this);
		
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
	
	@Override
	public void update(float timeStep){
		super.update(timeStep);
		bodyAnimation.update(timeStep);
		jetpack.update(timeStep);
		jumpTimer += timeStep;
		facingRight = target.x >= this.location.x;
		
		// apply forces based on toggles
		if(movingLeft)
			applyLeftForce();
		if(movingRight)
			applyRightForce();
		
		// update the location of the target so it moves with the player
		if(body != null){
			Vector2 linVec = body.getLinearVelocity();
			target.x += linVec.x * timeStep;
			target.y += linVec.y * timeStep;
		}
		
		// update and shoot pistol
		if(firearm != null){
			firearm.update(timeStep);
			if(this.isShooting)
				firearm.shootAt(body.getWorld(), target);
		}
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
				Game.resources.sounds.getSound("jump").play();
				// add force to current velocity and set it
				linVec.y += JUMP_FORCE;
				body.setLinearVelocity(linVec);
				
				// don't forget to reset the timer
				jumpTimer = 0.0f;
				return true;
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
	
	/** @return The player's jump sensor */
	public JumpSensor getJumpSensor(){ return jumpSensor; }
	
	public PlayerBodyAnimation getBodyAnimation(){ return bodyAnimation; }
	
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

	/** @return Whether or not the player is facing right */
	public boolean isFacingRight(){ return facingRight; }
	
	/** @param target Target to start shooting at */
	public void beginShooting(Vector2 target){
		this.setTarget(target);
		beginShooting();
	}
	/** Start shootin'! */
	public void beginShooting(){ isShooting = true; }
	/** Stop shooting */
	public void endShooting(){ isShooting = false; }
	/** @return Whether or not the player is shooting */
	public boolean isShooting(){ return isShooting; }
	
	/**
	 * Updates where the player is aiming
	 * @param target New spot to aim at
	 */
	public void setTarget(Vector2 target){
		if(!Game.isPaused())
			this.target.set(target);
	}
	
	/** @return The player's current weapon */
	public Firearm getCurrentFirearm(){ return firearm; }
	
	/** @return Where the player is aiming */
	public Vector2 getCurrentTarget(){ return target; }
	
	/** @return The angle from the player to the player's target */
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
	public DynamicEntity getFirearmOwningEntity(){ return this; }
	
	@Override
	public float getFirearmAngle(){ return getArmAngle(); }

	@Override
	public float currentHealth() { return health; }

	@Override
	public void hurt(float amount) { health -= amount; }

	@Override
	public void heal(float amount) { health += amount; }
	
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