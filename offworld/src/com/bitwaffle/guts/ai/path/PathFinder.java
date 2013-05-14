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
	/** Queues to manage nodes */
	PriorityQueue<Node> openset, closedset;
	
	/** The completed path, after updatePath is called */
	LinkedList<Node> path;
	
	/** Grid to keep track of where nodes are */
	SparseMatrix grid;
	
	/** Start and end nodes */
	private Node start, goal;
	
	/** Callback for sweeping physics world for obstacles */
	private static HitCountRayCastCallback callback;
	
	/** How far apart each node is */
	private float nodeDist = 2.5f, goalThreshold = 5.0f;
	
	/** How frequently the path gets updated */
	private float updateFrequency = 0.5f;
	/** Used to time updates*/
	private float timer;
	
	// FIXME debug
	private int step = 1;
	
	/** Current node being looked at */
	private Node current;
	
	public PathFinder(){
		grid = new SparseMatrix(100, 100);
		
		callback = new HitCountRayCastCallback();
		
		start = new Node(new Vector2(), 0, 0);
		goal = new Node(new Vector2(), 0, 0);
		
		openset = new PriorityQueue<Node>(20);
		closedset = new PriorityQueue<Node>(20);
		path = new LinkedList<Node>();
		
		timer = 0.0f;
	}
	
	public void setStart(Vector2 newStart){ this.start.setLocation(newStart); }
	public Node getStart(){ return start; }
	
	public void setGoal(Vector2 newGoal){ this.goal.setLocation(newGoal); }
	public Node getGoal(){ return goal; }
	
	/** @param newFrequency New update frequency, in seconds */
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
			
			// FIXME temp
			step++;
			
			// clear everything
			closedset.clear();
			openset.clear();
			grid.clear();
			buildStartNodes();
			
			// perform A* to rebuild path
			aStar();
		}
	}
	
	/**
	 * Performs the A* search algorithm to find a path from the start goal to the end goal.
	 * After this is called, this PathFinder's path should contain a path to the goal.
	 * 
	 * Straight from Wikipedia.
	 */
	private void aStar(){
		while(!openset.isEmpty()){
		//for(int i = 0; i < step; i++){
			current = openset.remove();
			if(isGoal(current)){
				path = reconstructPath(current);
				return;
			}
			
			closedset.add(current);
			
			current.expand(grid, goal, nodeDist);
			
			for(Node neighbor : grid.getNeighbors(current)){
				// FIXME 'current.dst(neighbor)' should always be the same since its a grid right?
				float tentativeGScore = current.gScore() + current.dst(neighbor);
				if(closedset.contains(neighbor) && tentativeGScore >= neighbor.gScore())
					continue;
				else if(!openset.contains(neighbor) || tentativeGScore > neighbor.gScore()){
					neighbor.setParent(current);
					neighbor.setGScore(tentativeGScore);
					neighbor.calcFScore();
					if(!openset.contains(neighbor))
						openset.add(neighbor);
				}
			}
		}
	}
	
	/** @return Path reconstructed from parents of current node */
	private LinkedList<Node> reconstructPath(Node n) {
		LinkedList<Node> p = new LinkedList<Node>();
		if(n.parent() != null){
			p.addAll(reconstructPath(n.parent()));
			p.add(n);
			return p;
		} else {
			p.add(n);
			return p;
		}
	}
	
	/** Get the path constructed after it's been updated */
	public LinkedList<Node> getPath(){ return path; }
	
	/** @return Whether or not the given goal is close enough to be the goal */
	private boolean isGoal(Node node){
		if(goal == null || node == null || node.loc() == null)
			return false;
		return goal.dst(node) <= goalThreshold;
	}

	/** Pretty much only for debug purposes */
	protected Queue<Node> getOpenset(){ return openset; }
	/** Pretty much only for debug purposes */
	protected Queue<Node> getClosedSet(){ return closedset; }
	/** Pretty much only for debug purposes */
	protected Node getCurrentNode(){ return current; }
	
	/** @return Whether or not between the two vectors is a valid move */
	public static boolean isValidMove(Node from, Node to){ return isValidMove(from.loc(), to.loc()); }
	/** @return Whether or not between the two vectors is a valid move */
	public static boolean isValidMove(Vector2 from, Vector2 to){
		callback.reset();
		Game.physics.rayCast(callback, from, to);
		return callback.hitCount() <= 0;
	}
	
	/** Builds up start nodes in eight directions */
	private void buildStartNodes(){
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
		
		if(isValidMove(loc, nvec)){
			n = new Node(nvec, 0, 1);
			n.calcScores(goal);
			openset.add(n);
			grid.put(n);
		}
		
		if(isValidMove(loc, evec)){
			e = new Node(evec, 1, 0);
			e.calcScores(goal);
			openset.add(e);
			grid.put(e);
		}
		
		if(isValidMove(loc, svec)){
			s = new Node(svec, 0 ,-1);
			s.calcScores(goal);
			openset.add(s);
			grid.put(s);
		}
		
		if(isValidMove(loc, wvec)){
			w = new Node(wvec, -1, 0);
			w.calcScores(goal);
			openset.add(w);
			grid.put(w);
		}
		
		if(isValidMove(loc, nevec)){
			ne = new Node(nevec, 1, 1);
			ne.calcScores(goal);
			openset.add(ne);
			grid.put(ne);
		}
		
		if(isValidMove(loc, nwvec)){
			nw = new Node(nwvec, -1, 1);
			nw.calcScores(goal);
			openset.add(nw);
			grid.put(nw);
		}
		
		if(isValidMove(loc, sevec)){
			se = new Node(sevec, 1, -1);
			se.calcScores(goal);
			openset.add(se);
			grid.put(se);
		}
		
		if(isValidMove(loc, swvec)){
			sw = new Node(swvec, -1, -1);
			sw.calcScores(goal);
			openset.add(sw);
			grid.put(sw);
		}
	}
}
