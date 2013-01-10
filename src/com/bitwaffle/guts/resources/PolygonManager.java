package com.bitwaffle.guts.resources;

import java.util.HashMap;

import com.badlogic.gdx.physics.box2d.Shape;
import com.bitwaffle.guts.graphics.render.shapes.Polygon;

/**
 * Manages a list of polygons
 * 
 * @author TranquilMarmot
 */
public class PolygonManager {
	/** All of the polygons */
	private HashMap<String, Polygon> polygons;
	
	/**
	 * Create a new PolygonManager
	 */
	public PolygonManager(){
		polygons = new HashMap<String, Polygon>();
	}
	
	/**
	 * Add a polygon to the map
	 * @param polyName Nam of polygon being added
	 * @param poly Polygon to add
	 */
	public void addPolygon(String polyName, Polygon poly) {
		polygons.put(polyName, poly);
	}
	
	/**
	 * Get the physics shape of a polygon
	 * @param polygonName Name of polygon to get shape of
	 * @return Shape of polygon (null if polygon has no shape)
	 */
	public Shape getShape(String polygonName){
		return polygons.get(polygonName).getShape();
	}
	
	public Polygon get(String polygonName){
		return polygons.get(polygonName);
	}
}
