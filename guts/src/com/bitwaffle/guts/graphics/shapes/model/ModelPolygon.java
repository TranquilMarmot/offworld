package com.bitwaffle.guts.graphics.shapes.model;

import java.nio.Buffer;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.graphics.shapes.polygon.Polygon;

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
	
	public ModelPolygon(String modelName,
			Vector2[] geometry, Polygon.Types type,
			Buffer debugVertBuffer, Buffer debugTexCoordBuffer, int debugCount){
		super(geometry, type, debugVertBuffer, debugTexCoordBuffer, debugCount);
		this.modelName = modelName;
	}
	
	/** @return Name of model to use for this polygon */
	public String modelName(){ return modelName; }
}
