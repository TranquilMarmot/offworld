package com.bitwaffle.moguts.entities;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.bitwaffle.moguts.Game;

/**
 * Player class
 * 
 * @author TranquilMarmot
 */
public class Player extends BoxEntity {
	private static float[] defaultColor = { 0.0f, 1.0f, 0.0f, 1.0f };
	
	private Random r;
	
	private boolean canJump;
	
	private float maxVelocityX = 15.0f;
	
	public Player(BodyDef bodyDef, float width, float height,
			FixtureDef fixtureDef) {
		super(bodyDef, width, height, fixtureDef, defaultColor);
		
		r = new Random();
		
		canJump = false;
	}
	
	@Override
	public void update(float timeStep){
		super.update(timeStep);
		this.color[0] = r.nextFloat();
		this.color[1] = r.nextFloat();
		this.color[2] = r.nextFloat();
		
		// don't want out player rotating all willy nilly now, do we?
		this.setAngle(0.0f);
	}
	
	public void goLeft(){
			Vector2 linVec = body.getLinearVelocity();
			if(linVec.x > -maxVelocityX) {
				linVec.x -= 0.5f;
				body.setLinearVelocity(linVec);
			}
	}
	
	public void goRight(){
		Vector2 linVec = body.getLinearVelocity();
		if(linVec.x < maxVelocityX) {
			linVec.x += 0.5f;
			body.setLinearVelocity(linVec);
		}
	}
	
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
	
	public boolean canJump(){
		return canJump;
	}
	
	public void setCanJump(boolean nowCanJump){
		this.canJump = nowCanJump;
	}
}