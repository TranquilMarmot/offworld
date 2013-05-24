package com.bitwaffle.guts.util;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * A sparse two-dimensional matrix that can go big any direction
 * and expands automagically.
 * 
 * Implemented as a hashmap of hashmaps, to (sort of) give O(1) access.
 * 
 * 
 * @author TranquilMarmot
 */
public class SparseMatrix<T> {
	/** This is a HashMap of HashMaps of Nodes */
	private IntHashMap list;
	
	/** How large the grid is, for iteration */
	private int minRow, maxRow, minCol, maxCol;
	
	/** How many columns to initially make on each row */
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
	
	/**
	 * Puts an T into this sparse matrix.
	 * Row and column can be either positive or negative.
	 * @param n Node to put in grid
	 * @param row Row to put node at
	 * @param col Column to put node at
	 */
	public void put(T n, int row, int col){
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
	@SuppressWarnings("unchecked")
	public T get(int row, int col){
		IntHashMap r = (IntHashMap)list.get(row);
		// if row doesn't exist, node doesn't exist
		if(r == null)
			return null;
		else
			return (T)r.get(col);
	}
	
	/** @return List of every node in this matrix */
	public LinkedList<T> getAll(){
		LinkedList<T> list = new LinkedList<T>();
		
		for(int row = minRow; row < maxRow; row++){
			for(int col = minCol; col < maxCol; col++){
				T n = this.get(row, col);
				if(n != null)
					list.add(n);
			}
		}
		
		return list;
	}
	
	/** @return Nodes next to given row and column in the eight cardinal directions */
	public ArrayList<T> getNeighbors(int row, int col){
		ArrayList<T> neighbors = new ArrayList<T>();
		
		T 
			ne = this.get(row - 1, col + 1),
			n = this.get(row, col + 1),
			nw = this.get(row + 1, col + 1),
			e = this.get(row + 1, col),
			se = this.get(row + 1, col - 1),
			s = this.get(row, col - 1),
			sw = this.get(row - 1, col - 1),
			w = this.get(row - 1, col);
		
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
	
	/** Get rid of everything in this matrix */
	public void clear(){
		// clear every row
		for(int row = minRow; row < maxRow; row++){
			IntHashMap m = (IntHashMap)list.get(row);
			if(m != null)
				m.clear();
		}
		list.clear();
		
		minRow = 0;
		maxRow = 0;
		minCol = 0;
		maxCol = 0;
	}
}
