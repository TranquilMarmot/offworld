package com.bitwaffle.offworld.camera;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.entity.dynamic.DynamicEntity;
import com.bitwaffle.guts.graphics.camera.Camera;
import com.bitwaffle.guts.graphics.camera.Camera2DMode;
import com.bitwaffle.offworld.entities.player.Player;

/**
 * Camera mode that follows a {@link DynamicEntity}
 * 
 * @author TranquilMarmot
 */
public class FollowMode extends Camera2DMode {
	/** What the camera is following */
	private DynamicEntity following;
	
	private Vector2
		/** How much the velocity of what the camera is following effects where the camera is looking */
		velocityFactor = new Vector2(1.0f, 0.5f),
		/** How much where the player is aiming effects the camera */
		playerTargetFollowFactor = new Vector2(0.3f, 0.3f);
	
	public FollowMode(Camera camera) {
		super(camera);
		
		//offset.set(-0.3f, -10.0f);
	}
	
	/** @param newTarget New entity to follow */
	public void follow(DynamicEntity newTarget){ this.following = newTarget; }
	
	/** @return Current entity being followed by the camera */
	public DynamicEntity getTarget(){ return following; }
	
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
				linVec.set(following.body.getLinearVelocity());
			
			// calculate new camera location based on variables
			float newX = offset.x + -followLoc.x + windowSize.x + (-linVec.x * velocityFactor.x) + (playerTargetDist.x * playerTargetFollowFactor.x),
				  newY = offset.y + -followLoc.y + windowSize.y + (-linVec.y * velocityFactor.y) + (playerTargetDist.y * playerTargetFollowFactor.y);
			
			// set the camera's location
			target.set(newX, newY);
		}
	}
}
