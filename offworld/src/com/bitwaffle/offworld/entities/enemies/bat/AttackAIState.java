package com.bitwaffle.offworld.entities.enemies.bat;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entity.ai.path.PathFinderSettings;
import com.bitwaffle.guts.entity.ai.states.PathFollower;
import com.bitwaffle.guts.entity.dynamic.DynamicEntity;
import com.bitwaffle.guts.physics.callbacks.FirstHitRayCastCallback;

/**
 * Bat attacking a player by following a path.
 * 
 * @author TranquilMarmot
 */
public class AttackAIState extends PathFollower {
	/** Player bat is attacking */
	private DynamicEntity target;
	
	/** Minumum distance for pathfinding to use for node spacing */
	float minDist = 10.0f;
	
	float pathOffset;
	
	private FirstHitRayCastCallback groundCheckCallback;
	
	public AttackAIState(DynamicEntity ent, PathFinderSettings settings, float nodeThreshold, float followSpeed) {
		super(ent, settings, nodeThreshold, followSpeed);
		groundCheckCallback = new FirstHitRayCastCallback();
		
	}
	
	public void setTarget(DynamicEntity target){
		this.target = target;
		PathFinderSettings settings = ((Bat)controlling).attackState.pathfinder.getCurrentSettings();
		settings.start.set(controlling.getLocation());
		settings.goal.set(target.getLocation());
	}
	
	@Override
	public void update(float timeStep){
		super.update(timeStep);

		Vector2 loc = controlling.getLocation();

		// set pathfinder to go to player from bat
		PathFinderSettings settings = ((Bat)controlling).attackState.pathfinder.getCurrentSettings();
		settings.start.set(loc);
		settings.goal.set(target.getLocation());
		float dist = loc.dst(target.getLocation());
		if(dist < minDist) dist = minDist;
		settings.nodeDist = dist / 10.0f;
		settings.goalThreshold = (dist / 5.0f);
		
		// repel from the ground
		Vector2 groundCheck = new Vector2(loc.x, loc.y + 7.5f);
		Game.physics.rayCast(groundCheckCallback, loc, groundCheck);
		if(groundCheckCallback.getHit() != null){
			Vector2 linVec = controlling.body.getLinearVelocity();
			controlling.body.setLinearVelocity(linVec.x, linVec.y + (12.0f * timeStep));
		}
	}
}
