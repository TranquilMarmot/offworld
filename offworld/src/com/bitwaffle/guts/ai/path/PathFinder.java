package com.bitwaffle.guts.ai.path;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.physics.callbacks.HitCountRayCastCallback;

public class PathFinder {

	PriorityQueue<Node> openset, closedset;
	
	LinkedList<Node> path;
	
	HashMap<Node, Node> cameFrom;
	
	Grid grid;
	
	/** Start and end nodes */
	private Node start, goal;
	
	/** Callback for sweeping physics world for obstacles */
	private static HitCountRayCastCallback callback;
	
	/** How far apart each node is */
	private float nodeDist = 1.0f;
	
	// FIXME temp
	int speed = 60, timer = 0;
	private int step = 1;
	Node current;
	
	public PathFinder(){
		callback = new HitCountRayCastCallback();
		
		openset = new PriorityQueue<Node>(20);
		closedset = new PriorityQueue<Node>(20);
		cameFrom = new HashMap<Node, Node>();
		path = new LinkedList<Node>();
		grid = new Grid(100, 100);
	}
	
	public void updatePath(Vector2 startLoc, Vector2 goalLoc){
		this.start = new Node(startLoc, 0, 0);
		this.goal = new Node(goalLoc, 0, 0);
		
		closedset.clear();
		openset.clear();
		cameFrom.clear();
		grid.clear();
		buildStartNodes();
		
		
		//while(!openset.isEmpty()){
		timer++;
		if(timer > speed){
			timer -= speed;
			step++;
		}
		for(int i = 0; i < step; i++){
			// FIXME should this be a poll? or a peek?
			current = openset.remove();
			if(isGoal(current)){
				Game.out.println("Made path!");
				reconstructPath(cameFrom, goal);
				return;
			}
			
			closedset.add(current);
			
			current.expand(goal, nodeDist, grid);
			
			// FIXME temp
			//for(Node neighbor : current.neighbors()){
			//	if(!openset.contains(neighbor) && !closedset.contains(neighbor))
			//		openset.add(neighbor);
			//}
			
			//Iterator<Node> it = openset.iterator();
			//while(it.hasNext()){
			//	Node n = it.next();
			//	n.calcScores(goal);
			//}
			
			
			for(Node neighbor : grid.getNeighbors(current)){
				// FIXME 'current.dst(neighbor)' should always be the same since its a grid right?
				float tentativeGScore = current.gScore() + current.dst(neighbor);
				if(closedset.contains(neighbor) && tentativeGScore >= neighbor.gScore())
					continue;
				else if(!openset.contains(neighbor) || tentativeGScore > neighbor.gScore()){
					cameFrom.put(neighbor, current);
					neighbor.setGScore(tentativeGScore);
					neighbor.calcFScore();
					if(!openset.contains(neighbor))
						openset.add(neighbor);
				}
			}
			
		}
		//}
	}
	
	private void reconstructPath(HashMap<Node, Node> cameFrom2, Node n) {
		path.clear();
		if(cameFrom2.containsKey(n)){
			reconstructPath(cameFrom2, cameFrom2.get(n));
			path.add(n);
		} else
			path.add(n);
	}

	// FIXME TEMP
	public Queue<Node> getOpenset(){
		return openset;
	}
	
	// FIXME TEMP
	public Queue<Node> getClosedSet(){
		return closedset;
	}
	
	// FIXME TEMP
	public Map<Node, Node> getCameFrom(){
		return cameFrom;
	}
	
	public Node getCurrent(){
		return current;
	}
	
	public LinkedList<Node> getPath(){
		return path;
	}
	
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
	
	private boolean isGoal(Node node){
		// FIXME temp?
		if(goal == null || node == null || node.loc() == null)
			return false;
		return goal.dst(node) <= 1.0f;
	}
	
	public static boolean isValidMove(Node from, Node to){ return isValidMove(from.loc(), to.loc()); }
	public static boolean isValidMove(Vector2 from, Vector2 to){
		callback.reset();
		Game.physics.rayCast(callback, from, to);
		return callback.hitCount() <= 0;
	}
}
