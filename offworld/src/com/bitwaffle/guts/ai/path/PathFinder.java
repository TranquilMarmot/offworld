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
	
	ArrayList<Node> neighbors;
	
	/** Start and end nodes */
	private Node start, goal;
	
	/** Callback for sweeping physics world for obstacles */
	private HitCountRayCastCallback callback;
	
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
			
			Vector2[] directions = {
					new Vector2(-sweepResolution, -sweepResolution),
					new Vector2(sweepResolution, -sweepResolution),
					new Vector2(sweepResolution, sweepResolution),
					new Vector2(-sweepResolution, sweepResolution),
					new Vector2(sweepResolution, 0.0f),
					new Vector2(-sweepResolution, 0.0f),
					new Vector2(0.0f, sweepResolution),
					new Vector2(0.0f, -sweepResolution)
			};
			// TODO
			
			neighbors = new ArrayList<Node>();
			for(Vector2 vec : directions){
				vec = vec.add(current.loc());
				
				callback.reset();
				Game.physics.rayCast(callback, current.loc(), vec);
				if(callback.hitCount() <= 0){
					Node n = new Node(vec);
					n.setParent(current);
					n.calcHScore(goal);
					n.calcFScore();
					n.calcGScore();
					neighbors.add(n);
				}
			}
			
			for(Node neighbor : neighbors){
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
			n.calcHScore(goal);
			n.calcGScore();
			n.calcFScore();
			openset.add(n);
		}
		
		if(isValidMove(start, e)){
			ne.s = e;
			se.n = e;
			e.calcHScore(goal);
			e.calcGScore();
			e.calcFScore();
			openset.add(e);
		}
		
		if(isValidMove(start, s)){
			se.s = s;
			sw.s = s;
			s.calcHScore(goal);
			s.calcGScore();
			s.calcFScore();
			openset.add(s);
		}
		
		if(isValidMove(start, w)){
			nw.w = w;
			sw.n = w;
			w.calcHScore(goal);
			w.calcGScore();
			w.calcFScore();
			openset.add(w);
		}
		
		if(isValidMove(start, ne)){
			n.e = ne;
			e.n = ne;
			ne.calcHScore(goal);
			ne.calcGScore();
			ne.calcFScore();
			openset.add(ne);
		}
		
		if(isValidMove(start, nw)){
			n.w = nw;
			w.n = nw;
			nw.calcHScore(goal);
			nw.calcGScore();
			nw.calcFScore();
			openset.add(nw);
		}
		
		if(isValidMove(start, se)){
			s.e = se;
			e.s = se;
			se.calcHScore(goal);
			se.calcGScore();
			se.calcFScore();
			openset.add(se);
		}
		
		if(isValidMove(start, sw)){
			s.w = sw;
			w.s = sw;
			sw.calcHScore(goal);
			sw.calcGScore();
			sw.calcFScore();
			openset.add(sw);
		}
	}
	
	/** Creates new nodes for any empty spots around this node */
	private void expandNode(Node node){
		if(node.n == null){
			
		}
	}
	
	private boolean isGoal(Node node){
		// FIXME temp?
		if(goal == null || node == null || node.loc() == null)
			return false;
		return goal.dst(node) <= 1.0f;
	}
	
	public boolean isValidMove(Node from, Node to){
		callback.reset();
		Game.physics.rayCast(callback, from.loc(), to.loc());
		return callback.hitCount() <= 0;
	}
}
