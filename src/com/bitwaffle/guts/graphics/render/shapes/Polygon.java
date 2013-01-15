package com.bitwaffle.guts.graphics.render.shapes;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
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
		LOOP,
		EDGE, // TODO
		CIRCLE // TODO
	}
	
	/** Buffers to hold datas */
	private ArrayList<FloatBuffer> vertBuffers, texCoordBuffers;
	
	/** Name of texture to bind for drawing */
	private ArrayList<String> textureNames;
	
	/** Number of indices */
	private ArrayList<Integer> numIndices;

	/** Shape in physics world */
	private Vector2[] geometry;
	
	/** What type of geometry this polygon uses */
	private Types geometryType;
	
	/**
	 * Create a new polygon
	 * @param vertices Vertices of polygon
	 * @param texCoords Texture coordinates of polygon
	 * @param numIndices Number of indices in polygon
	 */
	public Polygon(ArrayList<String> textureNames, ArrayList<FloatBuffer> vertices, ArrayList<FloatBuffer> texCoords, ArrayList<Integer> numIndices, Vector2[] geometry, Types type){
		this.textureNames = textureNames;
		this.vertBuffers = vertices;
		this.texCoordBuffers = texCoords;
		this.numIndices = numIndices;
		this.geometry = geometry;
		this.geometryType = type;
	}
	
	public int getNumRenderParts(){
		assert (vertBuffers.size() == texCoordBuffers.size() && texCoordBuffers.size() == textureNames.size() && textureNames.size() == numIndices.size());
		return vertBuffers.size();
	}
	
	public FloatBuffer getVertexBuffer(int index){
		return vertBuffers.get(index);
	}
	
	public FloatBuffer getTexCoordBuffer(int index){
		return texCoordBuffers.get(index);
	}
	
	public int getNumIndices(int index){
		return numIndices.get(index);
	}
	
	public String getTextureName(int index){
		return textureNames.get(index);
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
			
		case CIRCLE:
			CircleShape circ = new CircleShape();
			// TODO circ.setRadius(radius);
			//return circ;
			
		case EDGE:
			EdgeShape edge = new EdgeShape();
			// TODO edge.set(v1, v2);
			//return edge;
			
		default:
			return null;
		}
	}
}
