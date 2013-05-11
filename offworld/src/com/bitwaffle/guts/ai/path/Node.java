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
	public void expand(Node goal, float sweepResolution){
		// create new nodes
		if(this.n == null){
			Vector2 nvec = new Vector2(loc.x, loc.y + sweepResolution);
			if(PathFinder.isValidMove(loc, nvec)){
				Node nor = new Node(nvec);
				nor.calcScores(goal);
				nor.s = this;
				this.n = nor;
			}
		}
		
		if(this.e == null){
			Vector2 evec = new Vector2(loc.x + sweepResolution, loc.y);
			if(PathFinder.isValidMove(loc, evec)){
				Node eas = new Node(evec);
				eas.calcScores(goal);
				eas.w = this;
				this.e = eas;
			}
		}
		
		if(this.s == null){
			Vector2 svec = new Vector2(loc.x, loc.y - sweepResolution);
			if(PathFinder.isValidMove(loc, svec)){
				Node sou = new Node(svec);
				sou.calcScores(goal);
				sou.n = this;
				this.s = sou;
			}
		}
		
		if(this.w == null){
			Vector2 wvec = new Vector2(loc.x - sweepResolution, loc.y);
			if(PathFinder.isValidMove(loc, wvec)){
				Node wes = new Node(wvec);
				wes.calcScores(goal);
				wes.e = this;
				this.w = wes;
			}
		}
		
		if(this.ne == null){
			Vector2 nevec = new Vector2(loc.x + sweepResolution, loc.y + sweepResolution);
			if(PathFinder.isValidMove(loc, nevec)){
				Node noreas = new Node(nevec);
				noreas.calcScores(goal);
				noreas.se = this;
				this.ne = noreas;
			}
		}
		
		if(this.nw == null){
			Vector2 nwvec = new Vector2(loc.x - sweepResolution, loc.y + sweepResolution);
			if(PathFinder.isValidMove(loc, nwvec)){
				Node norwes = new Node(nwvec);
				norwes.calcScores(goal);
				norwes.sw = this;
				this.nw = norwes;
			}
		}
		
		if(this.sw == null){
			Vector2 swvec = new Vector2(loc.x - sweepResolution, loc.y - sweepResolution);
			if(PathFinder.isValidMove(loc, swvec)){
				Node souwes = new Node(swvec);
				souwes.calcScores(goal);
				souwes.nw = this;
				this.sw = souwes;
			}
		}
		
		if(this.se == null){
			Vector2 sevec = new Vector2(loc.x + sweepResolution, loc.y - sweepResolution);
			if(PathFinder.isValidMove(loc, sevec)){
				Node soueas = new Node(sevec);
				soueas.calcScores(goal);
				soueas.ne = this;
				this.se = soueas;
			}
		}
		
		// link nodes together
		if(n != null){
			n.setParent(this);
			
			if(nw != null){
				nw.e = n;
				n.w = nw;
			}
			if(ne != null){
				ne.w = n;
				n.e = ne;
			}
		}
		
		if(s != null){
			s.setParent(this);
			
			if(sw != null){
				sw.e = s;
				s.w = sw;
			}
			if(se != null){
				se.w = s;
				s.e = se;
			}
		}
		
		if(e != null){
			e.setParent(this);
			
			if(ne != null){
				ne.s = e;
				e.n = ne;
			}
			if(se != null){
				se.n = e;
				e.s = se;
			}
		}
		
		if(w != null){
			w.setParent(this);
			
			if(nw != null){
				nw.s = w;
				w.n = nw;
			}
			if(sw != null){
				sw.n = w;
				w.s = sw;
			}
		}
	}
}
