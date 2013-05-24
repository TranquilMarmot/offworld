package com.bitwaffle.guts.ai.path;

/** 
 * Settings for the pathfinder to use when finding paths
 * 
 * @author TranquilMarmot
 */
public class PathFinderSettings {
	public float
		/** How far apart each node is in the grid */
		nodeDist = 100.0f,
		/** How close algorithm has to get to consider itself at the goal */
		goalThreshold = 150.0f,
		/** How frequently the path gets updated */
		updateFrequency = 1.0f;
		
	public int
		/** Maximum number of iterations pathfinder will do (for special cases) */
		maxIterations = Integer.MAX_VALUE;
	
	public boolean
		/** Whether or not to allow finding paths diagonally (for special cases) */
		allowDiagonal = true;
	
	public PathFinderSettings(){}
	
	public PathFinderSettings(
			float nodeDist,
			float goalThreshold,
			float updateFrequency,
			int maxIterations,
			boolean allowDiagonal){
		this.nodeDist = nodeDist;
		this.goalThreshold = goalThreshold;
		this.updateFrequency = updateFrequency;
		this.maxIterations = maxIterations;
		this.allowDiagonal = allowDiagonal;
		
	}
	
	/** @param other Settings to set these settings to */
	public void set(PathFinderSettings other){
		this.nodeDist = other.nodeDist;
		this.goalThreshold = other.goalThreshold;
		this.updateFrequency = other.updateFrequency;
		this.maxIterations = other.maxIterations;
		this.allowDiagonal = other.allowDiagonal;
	}
}
