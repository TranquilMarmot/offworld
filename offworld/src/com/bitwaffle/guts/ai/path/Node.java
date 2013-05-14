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
	
	private Vector2 loc;
	private Node parent;
	
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
	}
	
	/** Find distance from this node to another one*/
	public float dst(Node other){
		return this.loc().dst(other.loc());
	}
	
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
	
	public Vector2 loc(){ return location(); }
	public Vector2 location(){ return loc; }
	
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
	
	/** Creates new nodes for any empty spots around this node */
	public void expand(Node goal, float nodeDist, Grid grid){
		// TODO
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
		&& PathFinder.isValidMove(loc, nvec)){
			Node n = new Node(nvec, row, col + 1);
			n.setParent(this);
			n.calcScores(goal);
			grid.put(n);
		}
		
		// E
		if(grid.get(row + 1, col) == null
		&& PathFinder.isValidMove(loc, evec)){
			Node e = new Node(evec, row + 1, col);
			e.setParent(this);
			e.calcScores(goal);
			grid.put(e);
		}
		
		// S
		if(grid.get(row, col - 1) == null
		&& PathFinder.isValidMove(loc, svec)){
			Node s = new Node(svec, row, col - 1);
			s.setParent(this);
			s.calcScores(goal);
			grid.put(s);
		}
		
		// W
		if(grid.get(row - 1, col) == null
		&& PathFinder.isValidMove(loc, wvec)){
			Node w = new Node(wvec, row - 1, col);
			w.setParent(this);
			w.calcScores(goal);
			grid.put(w);
		}
		
		// NW
		if(grid.get(row - 1, col + 1) == null
		&& PathFinder.isValidMove(loc, nwvec)){
			Node nw = new Node(nwvec, row - 1, col + 1);
			nw.setParent(this);
			nw.calcScores(goal);
			grid.put(nw);
		}
		
		// NE
		if(grid.get(row + 1, col + 1) == null
		&& PathFinder.isValidMove(loc, nevec)){
			Node ne = new Node(nevec, row + 1, col + 1);
			ne.setParent(this);
			ne.calcScores(goal);
			grid.put(ne);
		}
		
		// SW
		if(grid.get(row - 1, col - 1) == null
		&& PathFinder.isValidMove(loc, swvec)){
			Node sw = new Node(swvec, row - 1, col - 1);
			sw.setParent(this);
			sw.calcScores(goal);
			grid.put(sw);
		}
		
		// SE
		if(grid.get(row + 1, col - 1) == null
		&& PathFinder.isValidMove(loc, sevec)){
			Node se = new Node(sevec, row + 1, col - 1);
			se.setParent(this);
			se.calcScores(goal);
			grid.put(se);
		}
	}
}
