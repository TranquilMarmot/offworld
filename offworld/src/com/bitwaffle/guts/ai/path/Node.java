package com.bitwaffle.guts.ai.path;

import java.util.ArrayList;

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
	
	public Node n, s, e, w, ne, nw, se, sw;
	
	
	private float 
		/** Distance straight to goal */
		hScore, 
		/** Distance to start point via parent nodes */
		gScore,
		/** gScore + hScore (used to sort) */
		fScore;
	
	public Node(Vector2 vector){
		this.loc = vector;
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
		double otherFScore = other.fScore();
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
	
	public Node parent(){ return parent; }
	public void setParent(Node newParent){ parent = newParent; }
	
	public ArrayList<Node> neighbors(){
		ArrayList<Node> neighbors = new ArrayList<Node>();
		
		if(n != null)
			neighbors.add(n);
		if(e != null)
			neighbors.add(e);
		if(s != null)
			neighbors.add(s);
		if(w != null)
			neighbors.add(w);
		if(ne != null)
			neighbors.add(ne);
		if(nw != null)
			neighbors.add(nw);
		if(se != null)
			neighbors.add(se);
		if(sw != null)
			neighbors.add(sw);
		
		return neighbors;
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
}
