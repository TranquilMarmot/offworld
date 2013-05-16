package com.bitwaffle.guts.ai.states;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.ai.path.Node;
import com.bitwaffle.guts.ai.path.PathFinder;
import com.bitwaffle.guts.entity.dynamic.DynamicEntity;
import com.bitwaffle.guts.util.MathHelper;

/**
 * Moves the entity being controlled towards a given goal
 * 
 * @author TranquilMarmot
 */
public class PathFollower extends AIState {
	
	// TODO find a way to get path from pathfinder and step through it
	private Queue<Node> path;
	
	public PathFinder pathfinder;
	
	public PathFollower(DynamicEntity ent){
		path = new LinkedList<Node>();
		pathfinder = new PathFinder(2.5f, 5.0f, 0.5f);
	}

	@Override
	public void update(float timeStep) {
		pathfinder.updatePath(timeStep);
		if(pathfinder.newPath())
			setPath(pathfinder.getPath());
		moveTowardsTarget();
	}
	
	public void setPath(LinkedList<Node> newPath){ path = newPath; }
	
	private void moveTowardsTarget(){
		Node n = path.peek();
		if(n != null){
			Vector2 target = path.peek().loc();
			float angle = MathHelper.angle(controlling.getLocation(), target);
			
			float speed = 10.0f;
			
			Vector2 linvec = new Vector2(speed, 0.0f);
			linvec.rotate(angle);
			controlling.body.setLinearVelocity(linvec);
			
			// how close it has to be before moving on to next node
			float nodeDist = 1.0f;
			if(controlling.getLocation().dst(target) <= nodeDist)
				path.poll();
		}
	}
}
