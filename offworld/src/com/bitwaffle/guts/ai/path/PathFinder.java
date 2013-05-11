package com.bitwaffle.guts.ai.path;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.physics.callbacks.HitCountRayCastCallback;

public class PathFinder {

	PriorityQueue<Node> openset, closedset;
	
	LinkedList<Node> path;
	
	HashMap<Node, Node> cameFrom;
	
	/** Start and end nodes */
	private Node start, goal;
	
	/** Callback for sweeping physics world for obstacles */
	private static HitCountRayCastCallback callback;
	
	/** How far out to sweep for obstacles */
	private float sweepRadius = 5.0f;
	
	/** When finder sweeps in a circle (0 to 360) it steps by this amount */
	private float sweepResolution = 2.0f;
	
	int speed = 100, timer = 0;
	private int step = 1;
	
	public PathFinder(){
		callback = new HitCountRayCastCallback();
		
		openset = new PriorityQueue<Node>(20);
		closedset = new PriorityQueue<Node>(20);
		cameFrom = new HashMap<Node, Node>();
		path = new LinkedList<Node>();
	}
	
	public void updatePath(Vector2 startLoc, Vector2 goalLoc){
		this.start = new Node(startLoc);
		this.goal = new Node(goalLoc);
		
		closedset.clear();
		openset.clear();
		cameFrom.clear();
		buildStartNodes();
		
		
		//while(!openset.isEmpty()){
		timer++;
		if(timer > speed){
			timer -= speed;
			step++;
		}
		for(int i = 0; i < step; i++){
			// FIXME should this be a poll? or a peek?
			Node current = openset.remove();
			if(isGoal(current)){
				reconstructPath(cameFrom, goal);
				return;
			}
			
			closedset.add(current);
			
			current.expand(goal, sweepResolution);
			for(Node neighbore : current.neighbors()){
				openset.add(neighbore);
			}
			
			/*
			for(Node neighbor : current.neighbors()){
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
			*/
		}
		//}
	}
	
	private void reconstructPath(HashMap<Node, Node> cameFrom2, Node n) {
		path.clear();
		if(cameFrom2.containsKey(n)){
			reconstructPath(cameFrom2, cameFrom2.get(n));
			path.add(n);
		} else{
			path.add(n);
		}
	}

	public Queue<Node> getPath(){
		return openset;
	}
	
	private void buildStartNodes(){
		Vector2 loc = start.loc();
		Node
		n = new Node(new Vector2(loc.x, loc.y + sweepResolution)),
		e = new Node(new Vector2(loc.x + sweepResolution, loc.y)),
		s = new Node(new Vector2(loc.x, loc.y - sweepResolution)),
		w = new Node(new Vector2(loc.x - sweepResolution, loc.y)),
		sw = new Node(new Vector2(loc.x - sweepResolution, loc.y - sweepResolution)),
		se = new Node(new Vector2(loc.x + sweepResolution, loc.y - sweepResolution)),
		ne = new Node(new Vector2(loc.x + sweepResolution, loc.y + sweepResolution)),
		nw = new Node(new Vector2(loc.x - sweepResolution, loc.y + sweepResolution));
		
		if(isValidMove(start, n)){
			nw.n = n;
			ne.n = n;
			n.calcScores(goal);
			openset.add(n);
		}
		
		if(isValidMove(start, e)){
			ne.s = e;
			se.n = e;
			e.calcScores(goal);
			openset.add(e);
		}
		
		if(isValidMove(start, s)){
			se.s = s;
			sw.s = s;
			s.calcScores(goal);
			openset.add(s);
		}
		
		if(isValidMove(start, w)){
			nw.w = w;
			sw.n = w;
			w.calcScores(goal);
			openset.add(w);
		}
		
		if(isValidMove(start, ne)){
			n.e = ne;
			e.n = ne;
			ne.calcScores(goal);
			openset.add(ne);
		}
		
		if(isValidMove(start, nw)){
			n.w = nw;
			w.n = nw;
			nw.calcScores(goal);
			openset.add(nw);
		}
		
		if(isValidMove(start, se)){
			s.e = se;
			e.s = se;
			se.calcScores(goal);
			openset.add(se);
		}
		
		if(isValidMove(start, sw)){
			s.w = sw;
			w.s = sw;
			sw.calcScores(goal);
			openset.add(sw);
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
