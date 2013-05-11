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
		/* create new nodes */
		// north
		if(this.n == null){
			Vector2 nvec = new Vector2(loc.x, loc.y + sweepResolution);
			if(PathFinder.isValidMove(loc, nvec))
				n = new Node(nvec);
		}
		
		// east
		if(this.e == null){
			Vector2 evec = new Vector2(loc.x + sweepResolution, loc.y);
			if(PathFinder.isValidMove(loc, evec))
				e = new Node(evec);
		}
		
		// south
		if(this.s == null){
			Vector2 svec = new Vector2(loc.x, loc.y - sweepResolution);
			if(PathFinder.isValidMove(loc, svec))
				s = new Node(svec);
		}
		
		// west
		if(this.w == null){
			Vector2 wvec = new Vector2(loc.x - sweepResolution, loc.y);
			if(PathFinder.isValidMove(loc, wvec))
				w = new Node(wvec);
		}
		
		// northeast
		if(this.ne == null){
			Vector2 nevec = new Vector2(loc.x + sweepResolution, loc.y + sweepResolution);
			if(PathFinder.isValidMove(loc, nevec))
				ne = new Node(nevec);
		}
		
		// northwest
		if(this.nw == null){
			Vector2 nwvec = new Vector2(loc.x - sweepResolution, loc.y + sweepResolution);
			if(PathFinder.isValidMove(loc, nwvec))
				nw = new Node(nwvec);
		}
		
		// southwest
		if(this.sw == null){
			Vector2 swvec = new Vector2(loc.x - sweepResolution, loc.y - sweepResolution);
			if(PathFinder.isValidMove(loc, swvec))
				sw = new Node(swvec);
		}
		
		// southeast
		if(this.se == null){
			Vector2 sevec = new Vector2(loc.x + sweepResolution, loc.y - sweepResolution);
			if(PathFinder.isValidMove(loc, sevec))
				se = new Node(sevec);
		}
		
		// link nodes together
		if(n != null){
			n.setParent(this);
			n.calcScores(goal);
			n.s = this;
			
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
			s.calcScores(goal);
			s.n = this;
			
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
			e.calcScores(goal);
			e.w = this;
			
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
			w.calcScores(goal);
			w.e = this;
			
			if(nw != null){
				nw.s = w;
				w.n = nw;
			}
			if(sw != null){
				sw.n = w;
				w.s = sw;
			}
		}
		
		if(nw != null){
			nw.setParent(this);
			nw.calcScores(goal);
			nw.se = this;
			
			if(n != null){
				nw.e = w;
				n.w = nw;
			}
			if(w != null){
				nw.s = w;
				w.n = nw;
			}
		}
		
		if(ne != null){
			ne.setParent(this);
			ne.calcScores(goal);
			ne.sw = this;
			
			if(n != null){
				ne.w = n;
				n.e = ne;
			}
			if(e != null){
				ne.s = e;
				e.n = ne;
			}
		}
		
		if(sw != null){
			sw.setParent(this);
			sw.calcScores(goal);
			sw.ne = this;
			
			if(w != null){
				w.s = sw;
				sw.n = w;
			}
			if(s != null){
				s.w = sw;
				sw.e = s;
			}
		}
		
		if(se != null){
			se.setParent(this);
			se.calcScores(goal);
			se.nw = this;
			
			if(s != null){
				se.w = s;
				s.e = se;
			}
			if(e != null){
				e.s = se;
				se.n = e;
			}
		}
	}
}
