package com.bitwaffle.guts.ai.path;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.physics.callbacks.HitCountRayCastCallback;

/**
 * Performs the A* search algorithm to find a path between a start and a goal node.
 * updatePath() should be called every frame with the timeStep,
 * and the path will only get updated as often as the updateFrequency is set.
 * The start and goal nodes should be set using the appropriate methods at least once before any
 * updates are done.
 * 
 * @author TranquilMarmot
 */
public class PathFinder {
	// TODO try to speed up with jump point search http://harablog.wordpress.com/2011/09/07/jump-point-search/
	// TODO make diagonal searching optional
	// TODO theta*? http://aigamedev.com/open/tutorials/theta-star-any-angle-paths/#Rabin:02
	
	/** Queue of open nodes, sorted by F scores (see Node's compareTo() method) */
	private PriorityQueue<Node> openset;
	
	/** Start and end nodes (not actually in grid) */
	private Node start, goal;
	
	/** Current node being looked at */
	private Node current;
	
	/** The completed path, after updatePath is called */
	private LinkedList<Node> path;
	
	/** Grid to keep track of where nodes are and to get neighbors */
	private SparseMatrix grid;
	
	/** Whether or not the path has changed since the last time it was gotten */
	private boolean newPath;
	
	/** Callback for sweeping physics world for obstacles */
	private static HitCountRayCastCallback callback;
	
	/** How far apart each node is in the grid */
	private float nodeDist;
	
	/** How close algorithm has to get to consider itself at the goal */
	private float goalThreshold;
	
	/** How frequently the path gets updated */
	private float updateFrequency;
	/** Used to time updates*/
	private float timer;
	
	/** Maximum number of iterations before algorithm terminates */
	private int maxIterations, currentIteration;
	
	/**
	 * @param nodeDist Distance to put between each node
	 * @param goalThreshold How close algorithm has to be to consider itself at the goal
	 * @param updateFrequency How often to recalculate the path (in updates per second)
	 */
	public PathFinder(float nodeDist, float goalThreshold, float updateFrequency, int maxIterations){
		this.nodeDist = nodeDist;
		this.goalThreshold = goalThreshold;
		this.updateFrequency = updateFrequency;
		this.maxIterations = maxIterations;
		
		grid = new SparseMatrix(100, 100);
		
		callback = new HitCountRayCastCallback();
		
		start = new Node(new Vector2(), 0, 0);
		goal = new Node(new Vector2(), 0, 0);
		
		openset = new PriorityQueue<Node>(20);
		path = new LinkedList<Node>();
		
		timer = updateFrequency + 0.00001f;
		newPath = false;
	}
	
	public void setStart(Vector2 newStart){ this.start.setLocation(newStart); }
	public Node getStart(){ return start; }
	
	public void setGoal(Vector2 newGoal){ this.goal.setLocation(newGoal); }
	public Node getGoal(){ return goal; }
	
	/** @param newFrequency How often path gets recalculated, in seconds */
	public void setUpdateFrequency(float newFrequency){ this.updateFrequency = newFrequency; }
	/** @return Current update frequency, in updates per second */
	public float getUpdateFrequency(){ return updateFrequency; }
	
	/**
	 * Updates this pathfinder and finds the path again if the timer is up.
	 * @param timeStep Time passed since last update, in seconds
	 */
	public void updatePath(float timeStep){
		timer += timeStep;
		if(timer > updateFrequency){
			timer -= updateFrequency;
			
			// clear everything
			openset.clear();
			grid.clear();
			buildStartNodes();
			
			// perform A* to rebuild path
			aStar();
			newPath = true;
		}
	}
	
	/**
	 * Performs the A* search algorithm to find a path from the start goal to the end goal.
	 * After this is called, this PathFinder's path should contain a path to the goal.
	 * 
	 * Straight from Wikipedia.
	 */
	private void aStar(){
		currentIteration = 0;
		while(!openset.isEmpty()){
			// grabbing the head will give the node with the lowest value
			current = openset.remove();
			
			currentIteration++;
			// return if we've hit the goal
			if(isGoal(current) || currentIteration >= maxIterations){
				reconstructPath(current);
				return;
			}
			
			// add node to closed set
			current.setStatus(Node.Status.CLOSED);
			
			// expand node out
			current.expand(grid, goal, nodeDist);
			
			// iterate through neighbors and update scores as necessary
			for(Node neighbor : grid.getNeighbors(current)){
				Node.Status status = neighbor.status();
				// FIXME 'current.dst(neighbor)' should always be the same since its a grid right?
				float tentativeGScore = current.gScore() + current.dst(neighbor);
				if(status == Node.Status.CLOSED && tentativeGScore >= neighbor.gScore())
					continue;
				else if(status != Node.Status.OPEN || tentativeGScore > neighbor.gScore()){
					neighbor.setParent(current);
					neighbor.setGScore(tentativeGScore);
					neighbor.calcFScore();
					if(status != Node.Status.OPEN){
						neighbor.setStatus(Node.Status.OPEN);
						openset.add(neighbor);
					}
				}
			}
		}
	}
	
	/** Clears path and reconstructs it */
	private void reconstructPath(Node n) {
		path.clear();
		if(n.parent() != null){
			// recurse to add parent's parents
			reconstructPath(n.parent());
			path.add(n);
		} else 
			path.add(n);
	}
	
	/** @return Whether or not the path has changed since the last call to getPath() */
	public boolean newPath(){ return newPath; }
	
	/** Get the path constructed after it's been updated */
	public LinkedList<Node> getPath(){ 
		newPath = false;
		return path;
	}
	
	/** @return Whether or not the given goal is close enough to be the goal */
	private boolean isGoal(Node node){
		if(goal == null || node == null || node.loc() == null)
			return false;
		return goal.dst(node) <= goalThreshold;
	}

	/** Only for debug purposes */
	protected Queue<Node> getOpenset(){ return openset; }
	/** Only for debug purposes */
	protected Node getCurrentNode(){ return current; }
	/** Only for debug purposes */
	protected SparseMatrix getGrid(){ return grid; }
	
	/** @return Whether or not between two nodes is a valid move */
	public static boolean isValidMove(Node from, Node to){ return isValid(from.loc(), to.loc()); }
	/** @return Whether or not between two vectors is a valid move */
	public static boolean isValid(Vector2 from, Vector2 to){
		callback.reset();
		Game.physics.rayCast(callback, from, to);
		return callback.hitCount() <= 0;
	}
	
	/** Builds up start nodes in eight directions */
	private void buildStartNodes(){
		// TODO would it be better to use a hex grid here instead of a square one?
		Vector2
			loc = start.loc(),
			nvec = new Vector2(loc.x, loc.y + nodeDist),
			evec = new Vector2(loc.x + nodeDist, loc.y),
			svec = new Vector2(loc.x, loc.y - nodeDist),
			wvec = new Vector2(loc.x - nodeDist, loc.y),
			swvec = new Vector2(loc.x - nodeDist, loc.y - nodeDist),
			sevec = new Vector2(loc.x + nodeDist, loc.y - nodeDist),
			nevec = new Vector2(loc.x + nodeDist, loc.y + nodeDist),
			nwvec = new Vector2(loc.x - nodeDist, loc.y + nodeDist);
		
		Node n = null, e = null, s = null, w = null, ne = null, nw = null, se = null, sw = null;
		
		// N
		if(isValid(loc, nvec)){
			n = new Node(nvec, 0, 1);
			n.calcScores(goal);
			openset.add(n);
			grid.put(n);
		}
		
		// E
		if(isValid(loc, evec)){
			e = new Node(evec, 1, 0);
			e.calcScores(goal);
			openset.add(e);
			grid.put(e);
		}
		
		// S
		if(isValid(loc, svec)){
			s = new Node(svec, 0 ,-1);
			s.calcScores(goal);
			openset.add(s);
			grid.put(s);
		}
		
		// W
		if(isValid(loc, wvec)){
			w = new Node(wvec, -1, 0);
			w.calcScores(goal);
			openset.add(w);
			grid.put(w);
		}
		
		// NE
		if(isValid(loc, nevec)){
			ne = new Node(nevec, 1, 1);
			ne.calcScores(goal);
			openset.add(ne);
			grid.put(ne);
		}
		
		// NW
		if(isValid(loc, nwvec)){
			nw = new Node(nwvec, -1, 1);
			nw.calcScores(goal);
			openset.add(nw);
			grid.put(nw);
		}
		
		// SE
		if(isValid(loc, sevec)){
			se = new Node(sevec, 1, -1);
			se.calcScores(goal);
			openset.add(se);
			grid.put(se);
		}
		
		// SW
		if(isValid(loc, swvec)){
			sw = new Node(swvec, -1, -1);
			sw.calcScores(goal);
			openset.add(sw);
			grid.put(sw);
		}
	}
}
