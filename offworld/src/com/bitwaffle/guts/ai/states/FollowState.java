package com.bitwaffle.guts.ai.states;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.ai.path.PathFinder;
import com.bitwaffle.guts.entity.dynamic.DynamicEntity;
import com.bitwaffle.guts.util.MathHelper;

/**
 * Moves the entity being controlled towards a given goal
 * 
 * @author TranquilMarmot
 */
public class FollowState extends AIState {
	
	// TODO find a way to get path from pathfinder and step through it (when does pathfinder get told to update its path?!)
	private Queue<Vector2> path;
	
	//private Vector2 goal;
	
	PathFinder pathfinder;
	
	public FollowState(DynamicEntity ent){
		path = new LinkedList<Vector2>();
		pathfinder = new PathFinder();
	}

	@Override
	public void update(float timeStep) {		
		/*if(path.isEmpty())
			path = pathfinder.updatePath(controlling.getLocation(), goal);
		else*/
			moveTowardsTarget();
	}
	
	public void setPath(LinkedList<Vector2> newPath){ path = newPath; }
	
	private void moveTowardsTarget(){
		Vector2 target = path.peek();
		float angle = MathHelper.angle(controlling.getLocation(), target);
		
		float speed = 10.0f;
		
		Vector2 linvec = new Vector2(speed, 0.0f);
		linvec.rotate(angle);
		controlling.body.setLinearVelocity(linvec);
		
		float nodeDist = 1.0f;
		if(controlling.getLocation().dst(target) <= nodeDist)
			path.poll();
	}
}
