package com.bitwaffle.guts.entity.ai.states;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.entity.ai.path.Node;
import com.bitwaffle.guts.entity.ai.path.PathFinder;
import com.bitwaffle.guts.entity.ai.path.PathFinderSettings;
import com.bitwaffle.guts.entity.dynamic.DynamicEntity;
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
	public PathFollower(DynamicEntity ent, PathFinderSettings settings, float nodeThreshold, float followSpeed){
		super(ent);
		path = new LinkedList<Node>();
		pathfinder = new PathFinder(settings);
		this.nodeThreshold = nodeThreshold;
		this.followSpeed = followSpeed;
	}
	
	public void setPathFinderSettings(PathFinderSettings settings){ pathfinder.setSettings(settings); }	
	public PathFinderSettings getPathFinderSettings(){ return pathfinder.getCurrentSettings(); }

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
			Vector2 linvel = controlling.body.getLinearVelocity();
			controlling.body.setLinearVelocity(new Vector2(linvec.x + linvel.x, linvec.y + linvel.y));
			
			// move on to next node if close enough to current
			if(controlling.getLocation().dst(n.loc()) <= nodeThreshold)
				path.poll();
		}
	}

	@Override
	public void onLoseCurrentState() {

	}

	@Override
	public void onGainCurrentState() {
		pathfinder.forceUpdate();
	}
}
