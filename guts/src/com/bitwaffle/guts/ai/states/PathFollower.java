package com.bitwaffle.guts.ai.states;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.ai.path.Node;
import com.bitwaffle.guts.ai.path.PathFinder;
import com.bitwaffle.guts.util.MathHelper;

/**
 * Moves the entity being controlled towards a given goal
 * 
 * @author TranquilMarmot
 */
public class PathFollower extends AIState {
	/** Path entity is following */
	private Queue<Node> path;
	/** Finds path that gets followed */
	public PathFinder pathfinder;
	
	private float
		/** How close the entity has to be to each node before moving on to the next one */
		nodeThreshold,
		/** Speed that entity moves along path */
		followSpeed;
	
	/**
	 * @param pathNodeDist How far apart each node in the pathfinding should be (farther is faster)
	 * @param pathGoalThreshold How close a node has to be for it to be considered the goal during pathfinding
	 * @param pathUpdateFrequency How frequently to refind the path (in seconds)
	 * @param nodeThreshold How close entity has to be to node in path to move on to next node
	 * @param followSpeed How fast entity follows the path
	 */
	public PathFollower(float pathNodeDist, float pathGoalThreshold, float pathUpdateFrequency, int pathMaxIterations,
			float nodeThreshold, float followSpeed){
		path = new LinkedList<Node>();
		pathfinder = new PathFinder(pathNodeDist, pathGoalThreshold, pathUpdateFrequency, pathMaxIterations);
		this.nodeThreshold = nodeThreshold;
		this.followSpeed = followSpeed;
	}

	@Override
	public void update(float timeStep) {
		pathfinder.updatePath(timeStep);
		if(pathfinder.newPath())
			setPath(pathfinder.getPath());
		moveTowardsTarget();
	}
	
	/** Set the path being followed */
	public void setPath(LinkedList<Node> newPath){ path = newPath; }
	
	/** Sets linear velocity to move towards current point */
	private void moveTowardsTarget(){
		Node n = path.peek();
		if(n != null){
			float angle = MathHelper.angle(controlling.getLocation(), n.loc());
			
			Vector2 linvec = new Vector2(followSpeed, 0.0f);
			linvec.rotate(angle);
			controlling.body.setLinearVelocity(linvec);
			
			// move on to next node if close enough to current
			if(controlling.getLocation().dst(n.loc()) <= nodeThreshold)
				path.poll();
		}
	}
}
