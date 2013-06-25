package com.bitwaffle.offworld.camera;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.entity.dynamic.DynamicEntity;
import com.bitwaffle.guts.graphics.graphics2d.Camera2D;
import com.bitwaffle.offworld.entities.player.Player;

/**
 * Camera mode that follows a {@link DynamicEntity}
 * 
 * @author TranquilMarmot
 */
public class FollowMode extends Camera2D.CameraMode {
	
	/** What the camera is following */
	private DynamicEntity target;
	
	/** How fast the camera follows its target. Higher number means slower following, 1 means instant. */
	private float followSpeed = 100.0f;
	
	/** Offsets for rendering */
	private float
			xOffset = 0.0f,
			yOffset = -10.0f;
	
	/** How much the velocity of what the camera is following effects where the camera is looking */
	private float
			xVelocityFactor =  1.0f,
			yVelocityFactor = 0.5f;
	
	private float
			xPlayerTargetFollowFactor = 0.3f,
			yPlayerTargetFollowFactor = 0.3f;
	
	public void setTarget(DynamicEntity newTarget){ this.target = newTarget; }
	
	
	/** @return Current entity being followed by the camera */
	public DynamicEntity getTarget(){ return target; }
	
	public FollowMode(Camera2D camera){
		super(camera);
	}
	
	
	@Override
	public void update(float timeStep){
		if(target != null){
			Vector2 targetLoc = target.getLocation();
			
			// offset for player's target
			Vector2 playerTargetDist = new Vector2(0.0f, 0.0f);
			if(target instanceof Player){
				Player player = (Player)target;
				Vector2 ptarget = player.getCurrentTarget();
				float pdx = targetLoc.x - ptarget.x;
				float pdy = targetLoc.y - ptarget.y;
				playerTargetDist.set(pdx, pdy);
			}
			
			// if entity has no body, linear velocity doesnt effect camera
			Vector2 linVec;
			if(target.body != null)
				linVec = target.body.getLinearVelocity();
			else
				linVec = new Vector2(0.0f, 0.0f);
			
			
			Vector2 windowSize = camera.getWorldWindowSize();
			float newX = xOffset + -targetLoc.x + windowSize.x + (-linVec.x * xVelocityFactor) + (playerTargetDist.x * xPlayerTargetFollowFactor),
				  newY = yOffset + -targetLoc.y + windowSize.y + (-linVec.y * yVelocityFactor) + (playerTargetDist.y * yPlayerTargetFollowFactor);	
			Vector2 newLoc = new Vector2(newX, newY);
			
			camera.getLocation().lerp(newLoc, timeStep);
		}
	}
	
	/** @return Current follow speed. Higher number means slower speed, 1 means instant. */
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
