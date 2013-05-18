package com.bitwaffle.guts.ai.path;

import com.badlogic.gdx.math.Vector2;

/**
 * A node used for pathfinding.
 * Each node has a Vector2 associated with it,
 * as well as a parent node,
 * and an h,g and f score used for A*.
 * 
 * @author TranquilMarmot
 */
public class Node implements Comparable<Node> {
	/** Possible status of nodes */
	public static enum Status {
		/** Node is in open set (can still be expanded and hasn't been visited) */
		OPEN,
		/** Node is in the closed set (has been expanded and visited) */
		CLOSED,
		/** Unknown default status */
		UNKNOWN
	}
	/** Status of this node- OPEN, CLOSED, or UNKNOWN */
	private Status status;
	
	/** Location of this Node in the world */
	private Vector2 loc;
	/** Parent of this node (where it came from, to reconstruct path) */
	private Node parent;
	/** Row and column location of this node */
	private int row, col;
	
	private float 
		/** Distance straight to goal */
		hScore, 
		/** Distance to start point via parent nodes */
		gScore,
		/** gScore + hScore (used to sort) */
		fScore;
	
	public Node(Vector2 vector, int row, int col){
		this.loc = vector;
		this.row = row;
		this.col = col;
		hScore = 0.0f;
		fScore = 0.0f;
		gScore = 0.0f;
		this.status = Status.UNKNOWN;
	}
	
	/** @return Distance from this node to another one */
	public float dst(Node other){ return this.loc().dst(other.loc()); }
	
	@Override
	public int compareTo(Node other) {
		// sort based on f score
		float otherFScore = other.fScore();
		if (fScore > otherFScore)
			return 1;
		else if (fScore < otherFScore)
			return -1;
		else
			return 0;
	}
	
	@Override
	public int hashCode(){ return this.loc.hashCode(); }
	
	public void setStatus(Status newStatus){ this.status = newStatus; }
	public Status status(){ return status; }
	
	public Vector2 loc(){ return location(); }
	public Vector2 location(){ return loc; }
	public void setLocation(Vector2 location) { loc.set(location); }
	
	public int row(){ return row; }
	public void setRow(int row){ this.row = row; }
	public int col(){ return col; }
	public void setCol(int col){ this.col = col; }
	
	public Node parent(){ return parent; }
	public void setParent(Node newParent){ parent = newParent; }
	
	public void calcScores(Node goal){
		calcHScore(goal);
		calcGScore();
		calcFScore();
	}
	
	public float hScore(){ return hScore; }
	public void calcHScore(Node goal){ 
		hScore = this.dst(goal);
	}
	
	public float gScore(){ return gScore; }
	public void setGScore(float score){ this.gScore = score; }
	public void calcGScore(){
		if(parent == null)
			gScore = 0.0f;
		else
			gScore = this.dst(parent) + parent.gScore();
	}
	
	public float fScore(){ return fScore; }
	public void setFScore(float score){ this.fScore = score; }
	public void calcFScore(){
		fScore = gScore() + hScore();
	}
	
	/** 
	 * Creates new nodes for any empty spots around this node
	 * @param grid Grid of nodes
	 * @param goal Goal of new nodes, for calculating costs 
	 * @param nodeDist How far to expand node in physical world
	 */
	public void expand(SparseMatrix grid, Node goal, float nodeDist){
		Vector2
			nvec = new Vector2(loc.x, loc.y + nodeDist),
			evec = new Vector2(loc.x + nodeDist, loc.y),
			svec = new Vector2(loc.x, loc.y - nodeDist),
			wvec = new Vector2(loc.x - nodeDist, loc.y),
			swvec = new Vector2(loc.x - nodeDist, loc.y - nodeDist),
			sevec = new Vector2(loc.x + nodeDist, loc.y - nodeDist),
			nevec = new Vector2(loc.x + nodeDist, loc.y + nodeDist),
			nwvec = new Vector2(loc.x - nodeDist, loc.y + nodeDist);
		
		// N
		if(grid.get(row, col + 1) == null
		&& PathFinder.isValid(loc, nvec)){
			Node n = new Node(nvec, row, col + 1);
			n.calcScores(goal);
			grid.put(n);
		}
		
		// E
		if(grid.get(row + 1, col) == null
		&& PathFinder.isValid(loc, evec)){
			Node e = new Node(evec, row + 1, col);
			e.calcScores(goal);
			grid.put(e);
		}
		
		// S
		if(grid.get(row, col - 1) == null
		&& PathFinder.isValid(loc, svec)){
			Node s = new Node(svec, row, col - 1);
			s.calcScores(goal);
			grid.put(s);
		}
		
		// W
		if(grid.get(row - 1, col) == null
		&& PathFinder.isValid(loc, wvec)){
			Node w = new Node(wvec, row - 1, col);
			w.calcScores(goal);
			grid.put(w);
		}
		
		// NW
		if(grid.get(row - 1, col + 1) == null
		&& PathFinder.isValid(loc, nwvec)){
			Node nw = new Node(nwvec, row - 1, col + 1);
			nw.calcScores(goal);
			grid.put(nw);
		}
		
		// NE
		if(grid.get(row + 1, col + 1) == null
		&& PathFinder.isValid(loc, nevec)){
			Node ne = new Node(nevec, row + 1, col + 1);
			ne.calcScores(goal);
			grid.put(ne);
		}
		
		// SW
		if(grid.get(row - 1, col - 1) == null
		&& PathFinder.isValid(loc, swvec)){
			Node sw = new Node(swvec, row - 1, col - 1);
			sw.calcScores(goal);
			grid.put(sw);
		}
		
		// SE
		if(grid.get(row + 1, col - 1) == null
		&& PathFinder.isValid(loc, sevec)){
			Node se = new Node(sevec, row + 1, col - 1);
			se.calcScores(goal);
			grid.put(se);
		}
	}
}
