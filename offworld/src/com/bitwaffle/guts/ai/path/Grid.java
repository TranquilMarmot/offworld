package com.bitwaffle.guts.ai.path;

import java.util.ArrayList;

public class Grid {
	private class GridList extends IntHashMap{
		/** How large to make each column initially */
		private int initcols;
		
		/**
		 * @param initrows Initial number of rows
		 * @param initcols Intitial number of columns
		 */
		public GridList(int initrows, int initcols){
			super(initrows);
			this.initcols = initcols;
		}
		
		/** Returns node at the given spot. Null if no node exists there. */
		public Node get(int row, int col){
			IntHashMap r = (IntHashMap)this.get(row);
			if(r == null)
				return null;
			else
				return (Node)r.get(col);
		}
		
		/** Puts a node in a given spot. Expands grid if necessary. */
		public void put(Node n, int row, int col){
			// TODO Will this overwrite any existing node in given spot ?!?!
			IntHashMap r = (IntHashMap)this.get(row);
			if(r == null){
				r = new IntHashMap(initcols);
				this.put(row, r);
			}
			r.put(col, n);
		}
	}
	
	/** Grids going in all directions */
	private GridList list;
	
	/** How large the grid is */
	private int minRow, maxRow, minCol, maxCol;
	
	/**
	 * @param initrows Initial number of rows (expands as needed)
	 * @param initcols Initial number of columns (expands as necessary)
	 */
	public Grid(int initrows, int initcols){
		list = new GridList(initrows, initcols);
	
		minRow = 0;
		maxRow = 0;
		minCol = 0;
		maxCol = 0;
	}
	
	/** @param n Node to put in this grid. Uses the node's stored row and column. */
	public void put(Node n){
		this.put(n, n.row(), n.col());
	}
	
	/**
	 * Puts a node into this grid.
	 * Row and column can be either positive or negative.
	 * @param n Node to put in grid
	 * @param row Row to put node at
	 * @param col Column to put node at
	 */
	public void put(Node n, int row, int col){
		// check our bounds
		if(row < minRow)
			minRow = row;
		if(row > maxRow)
			maxRow = row;
		if(col < minCol)
			minCol = col;
		if(col > maxCol)
			maxCol = col;
		
		list.put(n, row, col);
	}
	
	public Node get(int row, int col){
		return list.get(row, col);
	}
	
	public ArrayList<Node> getNeighbors(Node n){
		return this.getNeighbors(n.row(), n.col());
	}
	
	public ArrayList<Node> getNeighbors(int row, int col){
		ArrayList<Node> neighbors = new ArrayList<Node>();
		
		Node 
			ne = this.get(row - 1, col + 1),
			n = this.get(row, col + 1),
			nw = this.get(row + 1, col + 1),
			e = this.get(row + 1, col),
			se = this.get(row + 1, col - 1),
			s = this.get(row, col - 1),
			sw = this.get(row - 1, col - 1),
			w = this.get(row - 1, col);
		
		if(ne != null)
			neighbors.add(ne);
		if(n != null)
			neighbors.add(n);
		if(nw != null)
			neighbors.add(nw);
		if(e != null)
			neighbors.add(e);
		if(se != null)
			neighbors.add(se);
		if(s != null)
			neighbors.add(s);
		if(sw != null)
			neighbors.add(sw);
		if(w != null)
			neighbors.add(w);
		
		return neighbors;
	}
	
	public ArrayList<Node> getAll(){
		ArrayList<Node> list = new ArrayList<Node>();
		
		for(int row = minRow; row < maxRow; row++)
			for(int col = minCol; col < maxCol; col++)
				list.add(this.get(row, col));
		
		return list;
	}
	
	public void clear(){		
		for(int row = minRow; row < maxRow; row++)
			((IntHashMap)list.get(row)).clear();
		list.clear();
		
		minRow = 0;
		maxRow = 0;
		minCol = 0;
		maxCol = 0;
	}
}
