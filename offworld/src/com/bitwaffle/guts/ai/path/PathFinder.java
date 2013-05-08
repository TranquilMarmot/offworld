package com.bitwaffle.guts.ai.path;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.physics.callbacks.HitCountRayCastCallback;

public class PathFinder {

	PriorityQueue<Vector2> openset, closedset;
	
	HashMap<Vector2, Integer> cameFrom;
	
	/** Start and end nodes */
	private Vector2 start, goal;
	
	/** Callback for sweeping physics world for obstacles */
	private HitCountRayCastCallback callback;
	
	/** How far out to sweep for obstacles */
	private float sweepRadius = 10.0f;
	
	/** When finder sweeps in a circle (0 to 360) it steps by this amount */
	private float sweepResolution = 30.0f;
	
	private Comparator<Vector2> goalDistanceComparator = new Comparator<Vector2>(){
		@Override
		public int compare(Vector2 vecA, Vector2 vecB) {
			float da = vecA.dst(goal);
			float db = vecB.dst(goal);
			return (int)(da - db);
		}
	};
	
	public PathFinder(){
		callback = new HitCountRayCastCallback();
		
		openset = new PriorityQueue<Vector2>(20, goalDistanceComparator);
		closedset = new PriorityQueue<Vector2>(20, goalDistanceComparator);
	}
	
	public void updatePath(Vector2 start, Vector2 goal){
		this.start = start;
		this.goal = goal;
		
		closedset.clear();
		
		openset.clear();
		buildStartNodes();
	}
	
	public Queue<Vector2> getPath(){
		return openset;
	}
	
	private void buildStartNodes(){
		for(float a = 0; a < 360.0f; a += sweepResolution){
			Vector2 vec = new Vector2(sweepRadius, 0.0f);
			vec = vec.rotate(a);
			
			Vector2 target = new Vector2(start);
			target = target.add(vec);
			
			
			callback.reset();
			Game.physics.rayCast(callback, start, target);
			if(callback.hitCount() <= 0)
				openset.add(target);
		}
	}
}
