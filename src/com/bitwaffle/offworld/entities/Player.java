package com.bitwaffle.offworld.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.bitwaffle.moguts.entities.BoxEntity;
import com.bitwaffle.moguts.graphics.animation.Animation;
import com.bitwaffle.moguts.graphics.render.renderers.Renderers;
import com.bitwaffle.offworld.Game;
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
	private boolean facingRight = false;
	private Pistol pistol;
	
	/** 
	 * Whether or not the player is able to jump right now
	 * This becomes false after the player jumps,
	 * and only becomes true once the player has landed on something
	 * (to prevent infinite jumping)
	 */
	private boolean canJump;
	
	/** 
	 * How fast the player can go on the X axis
	 * Goes both ways, so going right it can go
	 * maxVelocityX and going left it can go
	 * -maxVelocityX
	 */
	private float maxVelocityX = 15.0f;
	
	/** Animation for player */
	public Animation animation;
	
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
		
		canJump = true;
		
		pistol = new Pistol(this, 20, 2000.0f, 25.0f);
	}
	
	@Override
	public void init(){
		super.init();
		
		// don't want out player rotating all willy nilly now, do we?
		this.body.setFixedRotation(true);
		
		animation = Game.resources.textures.getAnimation("playerwalk");
	}
	
	@Override
	public void update(float timeStep){
		super.update(timeStep);
		
		Vector2 linVec = body.getLinearVelocity();
		if(linVec.x > 0.5f || linVec.x < -0.5f){
			// FIXME should this be defined in the XML?
			float animationStep = timeStep * Math.abs(linVec.x / 15.0f);
			animation.updateAnimation(animationStep);
		}
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
				facingRight = false;
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
				facingRight = true;
			}
		}
	}
	
	/**
	 * Make the player jump
	 */
	public void jump(){
		if(!Game.paused){
			Vector2 linVec = body.getLinearVelocity();
			if(this.canJump && linVec.y <= 7.5f && linVec.y >= -7.5f){
				Game.vibration.vibrate(25);
				Game.resources.sounds.play("jump");
				
				linVec.y += 7.5f;
				body.setLinearVelocity(linVec);
					
				this.canJump = false;
			}
		}
	}
	
	/**
	 * @return Whether or not the player can jump
	 */
	public boolean canJump(){
		return canJump;
	}
	
	/**
	 * @param nowCanJump Set whether or not the player can jump
	 */
	public void setCanJump(boolean nowCanJump){
		this.canJump = nowCanJump;
	}
	
	public boolean isFacingRight(){
		return facingRight;
	}
	
	/**
	 * Pew pew!
	 * @param target World-space vector to shoot towards
	 */
	public void shoot(Vector2 target){
		pistol.shootAt(target);
	}
	
	@Override
	public void write(Kryo kryo, Output output){
		super.write(kryo, output);
	}
	
	public void read(Kryo kryo, Input input){
		super.read(kryo, input);
		Game.player = this;
	}
}