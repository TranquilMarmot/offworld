package com.bitwaffle.offworld.entities.player;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.mappings.Ouya;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entity.dynamic.DynamicEntity;
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
public class Player extends DynamicEntity implements FirearmHolder, Health{
	/** Size of player's hitbox */
	private static final float WIDTH = 0.52062f, HEIGHT = 1.8034f;
	
	/** Two boxes are put on the side of the player with no friction, so that the player slides along walls */
	private static final float SIDE_BOX_W = 0.2f, SIDE_BOX_H = HEIGHT - 0.05f;
	
	//private static final float MAX_TARGET_DX = 10.0f, MAX_TARGET_DY = 8.0f;
	
	private static final float MAX_TARGET_RADIUS = 10.0f;
	
	/** The player's current firearm */
	private Firearm firearm;
	
	/** Player's inventory */
	public Inventory backpack;
	
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
	
	/** Sensor to detect any pickups */
	private PickupSensor pickupSensor;
	
	/** Used to time jumps, so that the player can't jump too often */
	private float jumpTimer = 0.0f;
	/** How long the player has to wait in between jumps */
	private final float JUMP_COOLDOWN = 0.5f;
	/** How much force the player jumps with */
	private final float JUMP_FORCE = 5.0f;
	
	/** What the player is aiming at */
	private Vector2 target;
	
	/** 
	 * Whether or not this player is being controlled by the mouse, so the value can get grabbed every frame
	 * If it's not grabbed every frame, the target only gets updated when the mouse moves which doesn't compensate for camera movement.
	 */
	public boolean controlledByMouse = true;
	
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
	
	/** Noargs constructor ONLY to be used with serialization!!! */
	public Player(){
		super();
		init();
	}
	
	/** Create a new Player instance */
	public Player(int layer, Vector2 location) {
		super(new PlayerRenderer(), layer, getBodyDef(location), getFixtureDef());
		init();
	}
	
	private static BodyDef getBodyDef(Vector2 location){
		BodyDef playerBodyDef = new BodyDef();
		playerBodyDef.type = BodyDef.BodyType.DynamicBody;
		playerBodyDef.position.set(location);
		playerBodyDef.fixedRotation = true;
		
		return playerBodyDef;
	}
	
	private static ArrayList<FixtureDef> getFixtureDef(){	
		// actual body
		FixtureDef playerFixture = new FixtureDef();
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(WIDTH - (SIDE_BOX_W * 2.0f), HEIGHT);
		playerFixture.shape = boxShape;
		playerFixture.density = 1.0f;
		playerFixture.friction = 0.5f;
		playerFixture.restitution = 0.0f;
		playerFixture.filter.categoryBits = CollisionFilters.PLAYER;
		playerFixture.filter.maskBits = CollisionFilters.EVERYTHING;
		
		// left side without friction to make it so player can't stick to walls
		FixtureDef leftSideDef = new FixtureDef();
		PolygonShape leftBox = new PolygonShape();
		leftBox.setAsBox(SIDE_BOX_W, SIDE_BOX_H, new Vector2(-WIDTH/2.0f, 0.0f), 0.0f);
		leftSideDef.shape = leftBox;
		leftSideDef.density = 1.0f;
		leftSideDef.friction = 0.0f;
		leftSideDef.restitution = 0.0f;
		leftSideDef.filter.categoryBits = CollisionFilters.PLAYER;
		leftSideDef.filter.maskBits = CollisionFilters.EVERYTHING;
		
		// right side without friction to make it so player can't stick to walls
		FixtureDef rightSideDef = new FixtureDef();
		PolygonShape rightBox = new PolygonShape();
		rightBox.setAsBox(SIDE_BOX_W, SIDE_BOX_H, new Vector2(WIDTH/2.0f, 0.0f), 0.0f);
		rightSideDef.shape = rightBox;
		rightSideDef.density = 1.0f;
		rightSideDef.friction = 0.0f;
		rightSideDef.restitution = 0.0f;
		rightSideDef.filter.categoryBits = CollisionFilters.PLAYER;
		rightSideDef.filter.maskBits = CollisionFilters.EVERYTHING;
		
		ArrayList<FixtureDef> defs = new ArrayList<FixtureDef>();
		defs.add(playerFixture);
		defs.add(leftSideDef);
		defs.add(rightSideDef);
		return defs;
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
		backpack = new Inventory();
	}
	
	@Override
	public void init(World world){
		super.init(world);
		
		// creating the sensors adds them as a fixture to the player's body,
		// so the body needs to exist before they are created
		jumpSensor = new JumpSensor(this);
		pickupSensor = new PickupSensor(this);
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
		
		if(!Ouya.runningOnOuya && controlledByMouse){
			Vector2 mouse = new Vector2();
			MathHelper.toWorldSpace(mouse, Gdx.input.getX(), Gdx.input.getY(), Game.renderer.r2D.camera);
			setTarget(mouse);
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
				Game.resources.sounds.play("jump");
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
	
	/** @return Total width of player's fixture */
	public float getWidth(){ return WIDTH; }
	/** @return Total height of player's fixture */
	public float getHeight(){ return HEIGHT; }
	
	public JumpSensor getJumpSensor(){ return jumpSensor; }
	public PickupSensor getPickupSensor() { return pickupSensor; }
	
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
		if(!Game.isPaused()){
			float angle = MathHelper.angle(this.getLocation(), target);
			
			Vector2 circ = new Vector2(MAX_TARGET_RADIUS, 0.0f);
			circ = circ.rotate(angle);
			
			this.target.set(location.x + circ.x, location.y + circ.y);
		}
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