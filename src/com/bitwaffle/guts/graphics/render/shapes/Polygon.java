package com.bitwaffle.guts.graphics.render.shapes;

import java.nio.FloatBuffer;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;

// TODO seiralize this?

public class Polygon {
	/**
	 * There's three different types of polygons, in terms of Box2D
	 * PolygonShapes can only have a max of 8 vertices and interact normally with the physics world
	 * ChainShapes and Loops can have a ton of vertices and pretty much just act as walls or static objects
	 */
	public enum Types{
		POLYGON,
		CHAIN,
		LOOP
	}
	
	/** Buffers to hold datas */
	private FloatBuffer vertBuffer, texCoordBuffer;
	
	/** Number of indices */
	private int numIndices;

	/** Shape in physics world */
	private Vector2[] geometry;
	
	/** What type of geometry this polygon uses */
	private Types geometryType;
	
	/** Name of texture to bind for drawing */
	private String textureName;
	
	/**
	 * Create a new polygon
	 * @param vertices Vertices of polygon
	 * @param texCoords Texture coordinates of polygon
	 * @param numIndices Number of indices in polygon
	 */
	public Polygon(String textureName, FloatBuffer vertices, FloatBuffer texCoords, int numIndices, Vector2[] geometry, Types type){
		this.textureName = textureName;
		this.vertBuffer = vertices;
		this.texCoordBuffer = texCoords;
		this.numIndices = numIndices;
		this.geometry = geometry;
		this.geometryType = type;
	}
	
	/** @return Number of indices this polygon has (for rendering) */
	public int getNumIndices(){
		return numIndices;
	}
	
	/** @return Name of texture to use to render this polygon */
	public String getTextureName(){
		return textureName;
	}
	
	/** @return Buffer filled with vertex data for rendering */
	public FloatBuffer getVertexBuffer(){
		return vertBuffer;
	}
	
	/** @return Buffer filled with texture coordinate data for rendering */
	public FloatBuffer getTexCoordBuffer(){
		return texCoordBuffer;
	}
	
	/** @return Shape to use for this polygon */
	public Shape getShape(){
		switch(geometryType){
		case POLYGON:
			PolygonShape poly = new PolygonShape();
			poly.set(geometry);
			return poly;
			
		case CHAIN:
			ChainShape chain = new ChainShape();
			chain.createChain(geometry);
			return chain;
			
		case LOOP:
			ChainShape loop = new ChainShape();
			loop.createLoop(geometry);
			return loop;
			
		default:
			return null;
		}
	}
}
