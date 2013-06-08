package com.bitwaffle.guts.ai.path;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.physics.callbacks.HitCountRayCastCallback;
import com.bitwaffle.guts.util.SparseMatrix;

/**
 * Performs the A* search algorithm to find a path between a start and a settings.goal node.
 * updatePath() should be called every frame with the timeStep,
 * and the path will only get updated as often as the updateFrequency is set.
 * The start and settings.goal nodes should be set using the appropriate methods at least once before any
 * updates are done.
 * 
 * @author TranquilMarmot
 */
public class PathFinder {
	// TODO try to speed up with jump point search http://harablog.wordpress.com/2011/09/07/jump-point-search/
	// TODO make diagonal searching optional (got it in settings, now need to honor it)
	// TODO theta*? http://aigamedev.com/open/tutorials/theta-star-any-angle-paths/#Rabin:02
	// TODO Sparse matrix  is a little much- retry it again with N,E,S,W pointer on each node
	
	/** Settings algorithm uses for pathfinding. Can have dramatic effects on speed! */
	private PathFinderSettings settings;
	
	/** Queue of open nodes, sorted by F scores (see Node's compareTo() method) */
	private PriorityQueue<Node> openset;
	
	/** Current node being looked at */
	private Node current;
	
	/** The completed path, after updatePath is called */
	private LinkedList<Node> path;
	
	/** Grid to keep track of where nodes are and to get neighbors */
	private SparseMatrix<Node> grid;
	
	/** Whether or not the path has changed since the last time it was gotten */
	private boolean newPath;
	
	/** Callback for sweeping physics world for obstacles */
	private static HitCountRayCastCallback callback;
	
	/** Used to time updates (no need to find path every frame) */
	private float timer;
	
	/** Current iteration, used for terminating early if given number in settings */
	private int currentIteration;
	
	/**
	 * @param nodeDist Distance to put between each node
	 * @param settings.goalThreshold How close algorithm has to be to consider itself at the settings.goal
	 * @param updateFrequency How often to recalculate the path (in updates per second)
	 */
	public PathFinder(PathFinderSettings settings){
		this.settings = settings;
		grid = new SparseMatrix<Node>(100, 100);
		
		callback = new HitCountRayCastCallback();
		
		openset = new PriorityQueue<Node>(20);
		path = new LinkedList<Node>();
		
		timer = settings.updateFrequency + 0.00001f;
		newPath = false;
	}
	
	public void setSettings(PathFinderSettings settings){ this.settings.set(settings); }
	public PathFinderSettings getCurrentSettings(){ return settings; }
	
	/**
	 * Updates this pathfinder and finds the path again if the timer is up.
	 * @param timeStep Time passed since last update, in seconds
	 */
	public void updatePath(float timeStep){
		timer += timeStep;
		if(timer >= settings.updateFrequency){
			timer -= settings.updateFrequency;
			
			// clear everything
			openset.clear();
			grid.clear();
			buildStartNodes();
			
			// perform A* to rebuild path
			aStar();
			newPath = true;
		}
	}
	
	public void forceUpdate() {
		timer = settings.updateFrequency;
		updatePath(0.0f);
	}
	
	/**
	 * Performs the A* search algorithm to find a path from the start settings.goal to the end settings.goal.
	 * After this is called, this PathFinder's path should contain a path to the settings.goal.
	 * 
	 * Straight from Wikipedia.
	 */
	private void aStar(){
		currentIteration = 0;
		while(!openset.isEmpty()){
			// grabbing the head will give the node with the lowest value
			current = openset.remove();
			
			currentIteration++;
			// return if we've hit the settings.goal or max number of iterations
			if(isGoal(current) || currentIteration >= settings.maxIterations){
				reconstructPath(current);
				return;
			}
			
			// add node to closed set
			current.setStatus(Node.Status.CLOSED);
			
			// expand node out
			current.expand(grid, settings.goal, settings.nodeDist);
			
			// iterate through neighbors and update scores as necessary
			for(Node neighbor : grid.getNeighbors(current.row(), current.col())){
				Node.Status status = neighbor.status();
				// FIXME 'current.dst(neighbor)' should always be the same since its a grid right?
				float tentativeGScore = current.gScore() + current.dst(neighbor);
				if(status == Node.Status.CLOSED && tentativeGScore >= neighbor.gScore())
					continue;
				else if(status != Node.Status.OPEN || tentativeGScore > neighbor.gScore()){
					// parents are used to build path
					neighbor.setParent(current);
					
					// recalculate scores
					neighbor.setGScore(tentativeGScore);
					neighbor.calcFScore();
					
					// add neighbor to the open set
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
	
	/** @return Whether or not the given settings.goal is close enough to be the settings.goal */
	private boolean isGoal(Node node){
		if(settings.goal == null || node == null || node.loc() == null)
			return false;
		return settings.goal.dst(node.loc()) <= settings.goalThreshold;
	}

	/** Only for debug purposes */
	protected Queue<Node> getOpenset(){ return openset; }
	/** Only for debug purposes */
	protected Node getCurrentNode(){ return current; }
	/** Only for debug purposes */
	protected SparseMatrix<Node> getGrid(){ return grid; }
	
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
			loc = settings.start,
			nvec = new Vector2(loc.x, loc.y + settings.nodeDist),
			evec = new Vector2(loc.x + settings.nodeDist, loc.y),
			svec = new Vector2(loc.x, loc.y - settings.nodeDist),
			wvec = new Vector2(loc.x - settings.nodeDist, loc.y),
			swvec = new Vector2(loc.x - settings.nodeDist, loc.y - settings.nodeDist),
			sevec = new Vector2(loc.x + settings.nodeDist, loc.y - settings.nodeDist),
			nevec = new Vector2(loc.x + settings.nodeDist, loc.y + settings.nodeDist),
			nwvec = new Vector2(loc.x - settings.nodeDist, loc.y + settings.nodeDist);
		
		Node n = null, e = null, s = null, w = null, ne = null, nw = null, se = null, sw = null;
		
		// N
		if(isValid(loc, nvec)){
			n = new Node(nvec, 0, 1);
			n.calcScores(settings.goal);
			openset.add(n);
			grid.put(n, n.row(), n.col());
		}
		
		// E
		if(isValid(loc, evec)){
			e = new Node(evec, 1, 0);
			e.calcScores(settings.goal);
			openset.add(e);
			grid.put(e, e.row(), e.col());
		}
		
		// S
		if(isValid(loc, svec)){
			s = new Node(svec, 0 ,-1);
			s.calcScores(settings.goal);
			openset.add(s);
			grid.put(s, s.row(), s.col());
		}
		
		// W
		if(isValid(loc, wvec)){
			w = new Node(wvec, -1, 0);
			w.calcScores(settings.goal);
			openset.add(w);
			grid.put(w, w.row(), w.col());
		}
		
		// NE
		if(isValid(loc, nevec)){
			ne = new Node(nevec, 1, 1);
			ne.calcScores(settings.goal);
			openset.add(ne);
			grid.put(ne, ne.row(), ne.col());
		}
		
		// NW
		if(isValid(loc, nwvec)){
			nw = new Node(nwvec, -1, 1);
			nw.calcScores(settings.goal);
			openset.add(nw);
			grid.put(nw, nw.row(), nw.col());
		}
		
		// SE
		if(isValid(loc, sevec)){
			se = new Node(sevec, 1, -1);
			se.calcScores(settings.goal);
			openset.add(se);
			grid.put(se, se.row(), se.col());
		}
		
		// SW
		if(isValid(loc, swvec)){
			sw = new Node(swvec, -1, -1);
			sw.calcScores(settings.goal);
			openset.add(sw);
			grid.put(sw, sw.row(), sw.col());
		}
	}
}
