package com.bitwaffle.guts.polygon;

import java.nio.Buffer;
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;


/**
 * Each polygon consists of a series of rendering parts and a geometry part
 * that represents it in the physics world.
 * 
 * @author TranquilMarmot
 */
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
		EDGE,
		CIRCLE
	}
	
	/** Buffers to hold datas */
	private ArrayList<Buffer> vertBuffers, texCoordBuffers;
	
	/** Name of textures to bind for drawing */
	private ArrayList<String> textureNames;
	
	/** Number of indices */
	private ArrayList<Integer> numIndices;

	/** 
	 * Shape in physics world
	 * If shape is a circle, geometry[0].x will be the radius
	 * If shape is an edge, geometry[0] and geometry[1] will be the edges
	 */
	private Vector2[] geometry;
	
	/** What type of geometry this polygon uses */
	private Types geometryType;
	
	/** Buffers to hold data for debug drawing */
	private Buffer debugVertBuffer, debugTexCoordBuffer;
	
	/** Number of vertices to use for debug drawing */
	private int debugVertexCount;
	

	/**
	 * Create a new polygon
	 * @param textureNames List of names of textures to use for render parts
	 * @param vertices List of vertex buffers to use for rendering parts
	 * @param texCoords List of texture coordinates to use for rendering parts
	 * @param numIndices List of number of indices to use for rendering parts
	 * @param geometry Array of geometry to use for physics data
	 * @param type Type (from Polygon.Types) of shape in the geometry
	 * @param debugVertBuffer Buffer full of vertices to use to render debug info
	 * @param debugTexCoordBuffer Buffer full of texture coordinates to use to render debug info
	 * @param debugCount Number of indices that debug rendering has
	 */
	public Polygon(ArrayList<String> textureNames, ArrayList<Buffer> vertices, ArrayList<Buffer> texCoords, ArrayList<Integer> numIndices,
	               Vector2[] geometry, Types type,
	               Buffer debugVertBuffer, Buffer debugTexCoordBuffer, int debugCount){
		this.textureNames = textureNames;
		this.vertBuffers = vertices;
		this.texCoordBuffers = texCoords;
		this.numIndices = numIndices;
		this.geometry = geometry;
		this.geometryType = type;
		this.debugVertBuffer = debugVertBuffer;
		this.debugTexCoordBuffer = debugTexCoordBuffer;
		this.debugVertexCount = debugCount;
	}
	
	/**
	 * When rendering a polygon,the renderer should start at 0 and
	 * go up to this, calling getVertexBuffer, getTexCoordBuffer,
	 * getNumIndices, getTextureName to get rendering data.
	 * @return How many parts there are to rendering this polygon
	 */
	public int getNumRenderParts(){
		return vertBuffers.size();
	}
	
	/**
	 * Get a vertex buffer for a render part
	 * @param index Index of render part to get buffer for
	 * @return Buffer for given render part
	 */
	public Buffer getVertexBuffer(int index){
		return vertBuffers.get(index);
	}
	
	/**
	 * Get a texture coordinate buffer for a render part
	 * @param index Index of render part to get buffer for
	 * @return Buffer for given render part
	 */
	public Buffer getTexCoordBuffer(int index){
		return texCoordBuffers.get(index);
	}
	
	/**
	 * Get the number of indices needed to draw a given render part
	 * @param index Index of render part to get index count for
	 * @return Number of indices to use to draw given render part
	 */
	public int getNumIndices(int index){
		return numIndices.get(index);
	}
	
	/**
	 * Get the name of a texture to use for a given render part
	 * @param index Index of render part to get texture name for
	 * @return Name of texture to use for index part
	 */
	public String getTextureName(int index){
		return textureNames.get(index);
	}
	
	/**
	 * @return Buffer filled with indices to use for debug rendering
	 */
	public Buffer getDebugVertBuffer(){
		return debugVertBuffer;
	}
	
	/**
	 * @return Buffer filled with texture coordinates to use for debug rendering
	 */
	public Buffer getDebugTexCoordBuffer(){
		return debugTexCoordBuffer;
	}
	
	/**
	 * @return Number of indices to use for debug rendering
	 */
	public int getDebugVertexCount(){
		return debugVertexCount;
	}
	
	/** @return  Shape to use for this polygon*/
	public Shape getShape(){
		return getShape(1.0f);
	}
	
	/** 
	 * @param scale Scale to get shape at
	 * @return Shape to use for this polygon
	 */
	public Shape getShape(float scale){
		Vector2[] scaledGeom = geometry;
		
		// only scale if necessary
		if(scale != 1.0f){
			scaledGeom = new Vector2[geometry.length];
			for(int i = 0; i < geometry.length; i++){
				scaledGeom[i] = new Vector2(geometry[i].x * scale, geometry[i].y * scale);
			}
		}
		
		
		if(geometryType == null)
			return null;
		else switch(geometryType){
		case POLYGON:
			PolygonShape poly = new PolygonShape();
			poly.set(scaledGeom);
			return poly;
			
		case CHAIN:
			ChainShape chain = new ChainShape();
			chain.createChain(scaledGeom);
			return chain;
			
		case LOOP:
			ChainShape loop = new ChainShape();
			loop.createLoop(scaledGeom);
			return loop;
			
		case CIRCLE:
			CircleShape circ = new CircleShape();
			circ.setRadius(scaledGeom[0].x);
			return circ;
			
		case EDGE:
			EdgeShape edge = new EdgeShape();
			edge.set(scaledGeom[0], scaledGeom[1]);
			return edge;
			
		default:
			return null;
		}
	}
}
