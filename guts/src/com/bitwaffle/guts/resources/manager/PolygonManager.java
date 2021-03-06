package com.bitwaffle.guts.resources.manager;

import java.util.HashMap;

import com.badlogic.gdx.physics.box2d.Shape;
import com.bitwaffle.guts.graphics.graphics2d.shapes.polygon.Polygon;
import com.bitwaffle.guts.graphics.graphics3d.model.ModelPolygon;

/**
 * Manages a list of polygons
 * 
 * @author TranquilMarmot
 */
public class PolygonManager {
	/** All of the polygons */
	private HashMap<String, Polygon> polygons;
	
	public PolygonManager(){
		polygons = new HashMap<String, Polygon>();
	}
	
	/** Add a polygon to the map */
	public void addPolygon(String polyName, Polygon poly) {
		polygons.put(polyName, poly);
	}
	
	/**
	 * Get the physics shape of a polygon
	 * @return Shape of polygon (null if polygon has no shape)
	 */
	public Shape getShape(String polygonName){
		return polygons.get(polygonName).getShape();
	}

	public Polygon get(String polygonName) {
		return polygons.get(polygonName);
	}
	
	/** Get a polygon that has a model attached to it.  */
	public ModelPolygon getModelPolygon(String polygonName){
		return (ModelPolygon) polygons.get(polygonName);
	}
	
	public void remove(String polygonName){
		// TODO cleanup here?
		polygons.remove(polygonName);
	}
}
