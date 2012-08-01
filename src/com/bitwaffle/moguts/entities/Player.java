package com.bitwaffle.moguts.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.bitwaffle.moguts.Game;
import com.bitwaffle.moguts.graphics.animation.Animation;
import com.bitwaffle.moguts.graphics.render.Render2D;

/**
 * Player class
 * 
 * @author TranquilMarmot
 */
public class Player extends BoxEntity {
	// FIXME these are temp
	private static float[] defaultColor = { 1.0f, 1.0f, 1.0f, 1.0f };
	private boolean facingRight = false;
	
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
	private Animation animation;
	
	/**
	 * Create a new Player instance
	 * @param bodyDef Definition of player's body
	 * @param width Width of player
	 * @param height Height of player
	 * @param fixtureDef Definition for player's fixture
	 */
	public Player(BodyDef bodyDef, float width, float height,
			FixtureDef fixtureDef) {
		super(bodyDef, width, height, fixtureDef, defaultColor);
		
		this.color = defaultColor;
		
		canJump = false;
	}
	
	@Override
	public void init(){
		super.init();
		animation = Game.resources.textures.getAnimation("playerwalk");
	}
	
	@Override
	public void update(float timeStep){
		super.update(timeStep);
		
		// don't want out player rotating all willy nilly now, do we?
		this.setAngle(0.0f);
		
		Vector2 linVec = body.getLinearVelocity();
		if(linVec.x > 0.5f || linVec.x < -0.5f)
			animation.updateAnimation(timeStep);
	}
	
	/**
	 * Move the player left
	 * Should be called every frame that the player is moving,
	 * i.e. when a button is being held down
	 */
	public void goLeft(){
		Vector2 linVec = body.getLinearVelocity();
		if(linVec.x > -maxVelocityX) {
			linVec.x -= 0.5f;
			body.setLinearVelocity(linVec);
			facingRight = false;
		}
	}
	
	/**
	 * Move the player right
	 * Should be called every frame that the player is moving,
	 * i.e. when a button is being held down
	 */
	public void goRight(){
		Vector2 linVec = body.getLinearVelocity();
		if(linVec.x < maxVelocityX) {
			linVec.x += 0.5f;
			body.setLinearVelocity(linVec);
			facingRight = true;
		}
	}
	
	/**
	 * Make the player jump
	 */
	public void jump(){
		Vector2 linVec = body.getLinearVelocity();
		if(this.canJump && linVec.y <= 7.5f && linVec.y >= -7.5f){
			Game.vibration.vibrate(25);
			Game.resources.sounds.play("jump");
			
			linVec.y += 7.5f;
			body.setLinearVelocity(linVec);
				
			this.canJump = false;
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
	
	@Override
	public void render(Render2D renderer){
		renderer.program.setUniform("vColor", color[0], color[1], color[2], color[3]);
		animation.renderCurrentFrame(renderer, this.width, this.height, facingRight, false);
	}
}