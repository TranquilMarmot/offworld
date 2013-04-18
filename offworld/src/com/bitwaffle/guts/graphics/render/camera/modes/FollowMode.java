package com.bitwaffle.guts.graphics.render.camera.modes;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.dynamic.DynamicEntity;

/**
 * Camera mode that follows a {@link DynamicEntity}
 * 
 * @author TranquilMarmot
 */
public class FollowMode extends CameraMode {
	/** What the camera is following */
	private DynamicEntity target;
	
	/** How fast the camera follows its target. Higher number means slower following, 1 means instant. */
	private float followSpeed;
	
	/** Offsets for rendering */
	private float xOffset, yOffset;
	
	/** How much the velocity of what the camera is following effects where the camera is looking */
	private float xVelocityFactor, yVelocityFactor;
	
	public FollowMode(float followSpeed, float xOffset, float yOffset, float xVelocityFactor, float yVelocityFactor){
		this.followSpeed = followSpeed;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.xVelocityFactor = xVelocityFactor;
		this.yVelocityFactor = yVelocityFactor;
	}
	
	public void setTarget(DynamicEntity newTarget){ this.target = newTarget; }
	
	
	/** @return Current entity being followed by the camera */
	public DynamicEntity getTarget(){ return target; }
	
	
	@Override
	public void update(float timeStep){
		if(target != null){
			float zoom = camera.getZoom();
			Vector2 camLoc = camera.getLocation();
			Vector2 targetLoc = target.getLocation();
			
			// if entity has no body, linear velocity doesnt effect camera
			Vector2 linVec;
			if(target.body != null)
				linVec = target.body.getLinearVelocity();
			else
				linVec = new Vector2(0.0f, 0.0f);
			
			// find the center of the screen
			float heightRatio = (float)Game.windowHeight / (float)Game.windowWidth;
			float widthRatio = (float)Game.windowWidth / (float)Game.windowHeight;
			float centerX = -targetLoc.x + ((widthRatio / 2.0f) / zoom);
			float centerY = -targetLoc.y + (heightRatio / zoom);
			
			// step camera towards entity on X axis
			float newX = centerX + (-linVec.x * xVelocityFactor) + (xOffset / zoom);
			float diffX = camLoc.x - newX;
			if(diffX > 0.5f || diffX < -0.5f)
				camLoc.x -= diffX / followSpeed;
			
			// step camera towards entity on Y axis
			float newY= centerY + (-linVec.y * yVelocityFactor) + (yOffset / zoom);
			float diffY = camLoc.y - newY;
			if(diffY > 0.5f || diffY < -0.5f)
				camLoc.y -= diffY / followSpeed;

			camera.setLocation(camLoc);
		}
	}
	
	/** @retufn Current follow speed. Higher number means slower speed, 1 means instant. */
	public float getFollowSpeed(){
		return followSpeed;
	}
	
	/** Set speed camera is following its target at. Higher speed means slower, 1 means instant. */
	public void setFollowSpeed(float newSpeed){
		if(newSpeed == 0.0f)
			newSpeed = 1.0f;
		
		this.followSpeed = newSpeed;
	}
}
