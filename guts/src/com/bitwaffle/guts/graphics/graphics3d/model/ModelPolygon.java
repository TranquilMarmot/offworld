package com.bitwaffle.guts.graphics.graphics3d.model;

import java.nio.Buffer;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.graphics.graphics2d.shapes.polygon.Polygon;

/**
 * A model that's attached to a polygon.
 * The polygon holds the collision data for the model,
 * as well as debug rendering info.
 * 
 * @author TranquilMarmot
 */
public class ModelPolygon extends Polygon {
	/** Name of model this polygon is attached to */
	private String modelName;
	
	/**
	 * Create a new model that is attached to a polygon.
	 * @param modelName Name of model, from Game.resources.models
	 * @param geometry Geometry to use for model in physics world
	 * @param type The type of the polygon
	 * @param debugVertBuffer Buffer of vertices to use for rendering debug polygon
	 * @param debugTexCoordBuffer Texture coordinates for rendering debug polygon
	 * @param debugCount Number of vertices for rendering debug polygon
	 */
	public ModelPolygon(String modelName,
			Vector2[] geometry, Polygon.Types type,
			Buffer debugVertBuffer, Buffer debugTexCoordBuffer, int debugCount){
		super(geometry, type, debugVertBuffer, debugTexCoordBuffer, debugCount);
		this.modelName = modelName;
	}
	
	/** @return Name of model to use for this polygon */
	public String modelName(){ return modelName; }
}
