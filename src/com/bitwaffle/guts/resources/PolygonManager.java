package com.bitwaffle.guts.resources;

import java.util.HashMap;

import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.bitwaffle.guts.graphics.render.Render2D;
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
	 * Render a given polygon
	 * @param renderer Renderer to use
	 * @param polygonName Name of polygon to use (from resource file)
	 */
	public void renderPolygon(Render2D renderer, String polygonName){
		polygons.get(polygonName).render(renderer, false, false);
	}
	
	public void renderPolygonDebug(Render2D renderer, String polygonName){
		polygons.get(polygonName).renderDebug(renderer, false, false);
	}
	
	/**
	 * Get the physics shape of a polygon
	 * @param polygonName Name of polygon to get shape of
	 * @return Shape of polygon (null if polygon has no shape)
	 */
	public ChainShape getShapeAsChain(String polygonName){
		return polygons.get(polygonName).getShapeAsChain();
	}
	
	public PolygonShape getShapeAsPolygon(String polygonName){
		return polygons.get(polygonName).getShapeAsPolygon();
	}
}
