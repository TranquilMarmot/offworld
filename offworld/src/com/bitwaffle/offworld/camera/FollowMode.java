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
	private DynamicEntity following;

	/** Whether or not to lerp the camera's location */
	private boolean interpolate = true;
	
	/** Offsets for rendering */
	private float
			xOffset = 0.0f,
			yOffset = -10.0f;
	
	/** How much the velocity of what the camera is following effects where the camera is looking */
	private float
			xVelocityFactor =  1.0f,
			yVelocityFactor = 0.5f;
	
	/** How much where the player is aiming effects the camera */
	private float
			xPlayerTargetFollowFactor = 0.3f,
			yPlayerTargetFollowFactor = 0.3f;
	
	/** @param newTarget New entity to follow*/
	public void follow(DynamicEntity newTarget){ this.following = newTarget; }
	
	/** @return Current entity being followed by the camera */
	public DynamicEntity getTarget(){ return following; }
	
	/** @param camera Camera that this mode will be controlling */
	public FollowMode(Camera2D camera){
		super(camera);
	}
	
	
	@Override
	public void update(float timeStep){
		if(following != null){
			Vector2 followLoc = following.getLocation();
			Vector2 windowSize = camera.getWorldWindowSize();
			
			// offset for player's target
			Vector2 playerTargetDist = new Vector2(0.0f, 0.0f);
			if(following instanceof Player){
				Vector2 ptarget = ((Player)following).getCurrentTarget();
				float pdx = followLoc.x - ptarget.x;
				float pdy = followLoc.y - ptarget.y;
				playerTargetDist.set(pdx, pdy);
			}
			
			// if entity has no body, linear velocity doesn't effect camera
			Vector2 linVec = new Vector2(0.0f, 0.0f);
			if(following.body != null)
				linVec = following.body.getLinearVelocity();
			
			// calculate new camera location based on variables
			float newX = xOffset + -followLoc.x + windowSize.x + (-linVec.x * xVelocityFactor) + (playerTargetDist.x * xPlayerTargetFollowFactor),
				  newY = yOffset + -followLoc.y + windowSize.y + (-linVec.y * yVelocityFactor) + (playerTargetDist.y * yPlayerTargetFollowFactor);
			Vector2 newLoc = new Vector2(newX, newY);
			
			// set the camera's location
			if(interpolate)
				camera.getLocation().lerp(newLoc, timeStep);
			else
				camera.getLocation().set(newLoc);
		}
	}
}
