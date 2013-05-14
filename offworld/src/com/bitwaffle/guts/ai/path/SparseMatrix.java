package com.bitwaffle.guts.ai.path;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * A sparse two-dimensional matrix that can go big any direction
 * and expands automagically.
 * 
 * @author TranquilMarmot
 */
public class SparseMatrix {
	/** This is a HashMap of HashMaps of Nodes */
	private IntHashMap list;
	
	/** How large the grid is, useful for iteration */
	private int minRow, maxRow, minCol, maxCol;
	
	/** How large to make each column initially */
	private int initcols;
	
	/**
	 * @param initrows Initial number of rows (expands as needed)
	 * @param initcols Initial number of columns (expands as necessary)
	 */
	public SparseMatrix(int initrows, int initcols){
		list = new IntHashMap(initrows);
		this.initcols = initcols;
	
		minRow = 0;
		maxRow = 0;
		minCol = 0;
		maxCol = 0;
	}
	
	/** @param n Node to put in this grid. Uses the node's stored row and column. */
	public void put(Node n){ this.put(n, n.row(), n.col()); }
	
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
		
		IntHashMap r = (IntHashMap)list.get(row);
		// create new row if necessary
		if(r == null){
			r = new IntHashMap(initcols);
			list.put(row, r);
		}
		// put node in given column in given row
		r.put(col, n);
	}
	
	/** @return Node at given row and column, null if no node exists. */
	public Node get(int row, int col){
		IntHashMap r = (IntHashMap)list.get(row);
		// if row doesn't exist, node doesn't exist
		if(r == null)
			return null;
		else
			return (Node)r.get(col);
	}
	
	/** @return Neighbors of this node in all eight directions */
	public ArrayList<Node> getNeighbors(Node n){
		return this.getNeighbors(n.row(), n.col());
	}
	
	/** @return Nodes next to given row and column in the eight cardinal directions */
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
	
	/** @return List of every node in this matrix */
	public LinkedList<Node> getAll(){
		LinkedList<Node> list = new LinkedList<Node>();
		
		for(int row = minRow; row < maxRow; row++)
			for(int col = minCol; col < maxCol; col++)
				list.add(this.get(row, col));
		
		return list;
	}
	
	/** Get rid of everything in this matrix */
	public void clear(){
		// clear every row
		for(int row = minRow; row < maxRow; row++)
			((IntHashMap)list.get(row)).clear();
		list.clear();
		
		minRow = 0;
		maxRow = 0;
		minCol = 0;
		maxCol = 0;
	}
}
