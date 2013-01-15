package com.bitwaffle.guts.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.render.shapes.Polygon;

/**
 * Loads in obj files to create Polygons
 * 
 * @author TranquilMarmot
 */
public class PolygonLoader {
	private static final String LOGTAG = "PolygonLoader";
	
	/** scale polygon is being loaded in at */
	private float xScale, yScale;
	
	/** Count of how many vertices there are */
	private int count = 0;
	
	/** which vertices to grab out of vertices and textureCoords */
	private ArrayList<int[]> vertexIndices, textureIndices;
	
	/** list of all vertices and texture coordinates (order matters!) */
	private ArrayList<Vector2> vertices, textureCoords;
	
	/**
	 * Load a polygon
	 * @param renderObjLoc Location of obj file to use for rendering
	 * @param collisiionObjLoc Location of obj file to use for geometry
	 * @param xScale X scale to load polygon in at
	 * @param yScale Y scale to load polygon in at
	 * @return Polygon object containing necessary data for physics and rendering
	 */
	public static Polygon loadPolygon(float xScale, float yScale, ArrayList<String> renderObjLocs, ArrayList<String> textureNames, String collisiionObjLoc, Polygon.Types shapeType, String debugObjLoc){
		return new PolygonLoader(xScale, yScale).loadPolygon(renderObjLocs, textureNames, collisiionObjLoc, shapeType, debugObjLoc);
	}
	
	/**
	 * Create a new polygon loader
	 * @param xScale X scale to load polygon in at
	 * @param yScale Y scale to load polygon in at
	 */
	private PolygonLoader(float xScale, float yScale){
		this.xScale = xScale;
		this.yScale = yScale;
		
		vertexIndices = new ArrayList<int[]>();
		textureIndices = new ArrayList<int[]>();
		
		vertices = new ArrayList<Vector2>();
		textureCoords = new ArrayList<Vector2>();
		
		// obj references things with 1 origin instead of 0 (wtf?! who decided that? asshole)
		vertices.add(new Vector2(0.0f, 0.0f));
		textureCoords.add(new Vector2(0.0f, 0.0f));
	}
	
	
	/**
	 * Load a polygon from a given obj file
	 * @param renderObjLoc Location of file to load
	 * @param scale Scale to load polygon in at
	 * @return Polygon from obj file
	 */
	private Polygon loadPolygon(ArrayList<String> renderObjLocs, ArrayList<String> textureNames, String collisionObjLoc, Polygon.Types shapeType, String debugObjLoc){
		ArrayList<FloatBuffer> vertBuffers = new ArrayList<FloatBuffer>(), texCoordBuffers = new ArrayList<FloatBuffer>();
		ArrayList<Integer> counts = new ArrayList<Integer>();
		for(String renderObjLoc : renderObjLocs){
			// this fills up the ArrayLists and increments count
			parseRenderObj(renderObjLoc);
			// done parsing! let's fill some float buffers!
			FloatBuffer vertexBuffer = putVerticesIntoBuffer();
			FloatBuffer texCoordBuffer = putTextureCoordsIntoBuffer();
			
			vertBuffers.add(vertexBuffer);
			texCoordBuffers.add(texCoordBuffer);
			counts.add(count);
			
			count = 0;
			vertexIndices.clear();
			textureIndices.clear();
			
			vertices.clear();
			textureCoords.clear();
			// obj references things with 1 origin instead of 0 (wtf?! who decided that? asshole)
			vertices.add(new Vector2(0.0f, 0.0f));
			textureCoords.add(new Vector2(0.0f, 0.0f));
		}
		
		parseRenderObj(debugObjLoc);
		
		FloatBuffer debugVertexBuffer = putVerticesIntoBuffer();
		FloatBuffer debugTexCoordBuffer = putTextureCoordsIntoBuffer();
		int debugCount = count;
			
		Vector2[] geom = parseCollisionObj(collisionObjLoc);
		
		return new Polygon(textureNames, vertBuffers, texCoordBuffers, counts, geom, shapeType, debugVertexBuffer, debugTexCoordBuffer, debugCount);
	}
	
	/**
	 * Parses an object for being rendered. After being called:
	 * count will contain the number of vertices in the polygon and
	 * vertexIndices, textureIndices, vertices and textureCoords
	 * will all be filled with data
	 * @param renderObjLoc Location of obj file to load
	 */
	private void parseRenderObj(String renderObjLoc){
		try{
			// for to reading through file
			BufferedReader reader = new BufferedReader(new InputStreamReader(Game.resources.openAsset(renderObjLoc)));
			// current line
			String line;
			
			while((line = reader.readLine()) != null){
				// split the line up at spaces
				StringTokenizer toker = new StringTokenizer(line, " ");
				// grab the line's type
				String lineType = toker.nextToken();
				
				// material library
				if(lineType.equals("mtllib"))
					Log.d(LOGTAG, "Attempted to load mtllib in render obj (not supported)");
				
				// new material
				if(line.startsWith("usemtl"))
					Log.d(LOGTAG, "Attempted to use material in render obj (not supported)");
				
				// object name
				if(lineType.equals("o")){
					Log.d(LOGTAG, "Loading \"" + toker.nextToken() + "\" from \"" + renderObjLoc + "\"");
				}
				
				// vertex
				if (lineType.equals("v")) {
					// grab the coordinates
					float x = Float.parseFloat(toker.nextToken()) * xScale;
					
					// ignore z since we're 2D
					toker.nextToken();
					float y = Float.parseFloat(toker.nextToken()) * yScale;
	
					vertices.add(new Vector2(x, y));
				}
				
				// texture coord
				if (line.startsWith("vt")) {
					float u = Float.parseFloat(toker.nextToken());
					float v = Float.parseFloat(toker.nextToken());
	
					textureCoords.add(new Vector2(u, v));
				}
				
				// normal
				if (lineType.equals("vn")) {
					// we're not interested in normals here (... yet)
					// grab the coordinates
					//float x = Float.parseFloat(toker.nextToken());
					//float y = Float.parseFloat(toker.nextToken());
					//float z = Float.parseFloat(toker.nextToken());
				}
				
				// face
				if (line.startsWith("f")) {
					// to see if we're dealing with a triangle or a quad (the ModelBuilder automatically splits quads into triangles)
					int numVertices = toker.countTokens();
	
					int[] vertIndices = new int[numVertices];
					int[] texIndices = new int[numVertices];
					
					for(int i = 0; i < numVertices; i++){
						String indices = toker.nextToken();
						StringTokenizer split = new StringTokenizer(indices, "/");
						// the obj file goes vertex/texture-coordinate/normal
						vertIndices[i] = Integer.parseInt(split.nextToken());
						texIndices[i] = Integer.parseInt(split.nextToken());
					}
					
					
					// if it's a triangle, add and increment count by 3
					if(numVertices == 3){
						vertexIndices.add(vertIndices);
						textureIndices.add(texIndices);
						count += 3;
						
					// else split into two triangles and increment by 6
					} else if(numVertices == 4){
						addAsTriangles(vertIndices, vertexIndices);
						addAsTriangles(texIndices, textureIndices);
						count += 6;
					} else {
						Log.d(LOGTAG, "Error! Count " + numVertices + " in PolygonLoader face! (Exptected 3 or 4)");
					}
				}
			}
			reader.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Add a quad to a list of indices as two triangle.
	 * It might be important to note that, for every quad split into a triangle,
	 * two of its vertex indices are duplicated. So what was originally
	 * 4 indices become 6 indices.
	 * This makes drawing much simpler, as OpenGL can only draw in one mode
	 *	(i.e. GL_TRIANGLES or GL_QUADS). So we don't have to keep switching
	 * between the two (just draw all triangles).
	 * @param quad Quad to add
	 * @param list List to add to
	 */
	private void addAsTriangles(int[] quad, ArrayList<int[]> list){
		/*
		 * Given indices 0,1,2,3 a quad can be split into two triangles:
		 * 0,1,2 and 2,3,0
		 */
		int[] tri1 = new int[3];
		tri1[0] = quad[0];
		tri1[1] = quad[1];
		tri1[2] = quad[2];

		list.add(tri1);

		int[] tri2 = new int[3];
		tri2[0] = quad[2];
		tri2[1] = quad[3];
		tri2[2] = quad[0];

		list.add(tri2);
	}
	
	/**
	 * Steps through the given indices and adds their corresponding vertices to a buffer
	 * @param vertices List of all vertices
	 * @param vertexIndices List of indices to call in vertices
	 * @return Buffer filled with vertices
	 */
	private FloatBuffer putVerticesIntoBuffer(){
		// 9, since there's three vertices per index and three floats per vertex
		FloatBuffer buff = BufferUtils.getFloatBuffer(vertexIndices.size() * 9);
		
		// each index corresponds to a triangle, which is three points
		for(int i = 0; i < vertexIndices.size(); i++){
			int[] verts = vertexIndices.get(i);
			
			Vector2 first = vertices.get(verts[0]);
			buff.put(first.x);
			buff.put(first.y);
			buff.put(0.0f); // always use 0 for Z since we're 2D
			
			Vector2 second = vertices.get(verts[1]);
			buff.put(second.x);
			buff.put(second.y);
			buff.put(0.0f);
			
			Vector2 third = vertices.get(verts[2]);
			buff.put(third.x);
			buff.put(third.y);
			buff.put(0.0f);
		}
		
		buff.rewind();
		return buff;
	}
	
	/**
	 * Steps through the given indices and adds their corresponding vertices to a buffer
	 * @param textureCoords List of all texture coordinates
	 * @param textureIndices List of indices to call in coordinates
	 * @return Buffer with with texture coordinates
	 */
	private FloatBuffer putTextureCoordsIntoBuffer(){
		// 6, since there's three vertices per index and two floats per vertex (no Z for texture)
		FloatBuffer buff = BufferUtils.getFloatBuffer(textureIndices.size() * 6);
		
		// each index corresponds to a triangle, which is three points
		for(int i = 0; i < textureIndices.size(); i++){
			int[] verts = textureIndices.get(i);
			
			Vector2 first = textureCoords.get(verts[0]);
			buff.put(first.x);
			buff.put(1.0f - first.y);
			
			Vector2 second = textureCoords.get(verts[1]);
			buff.put(second.x);
			buff.put(1.0f - second.y);
			
			Vector2 third = textureCoords.get(verts[2]);
			buff.put(third.x);
			buff.put(1.0f - third.y);
		}
		
		buff.rewind();
		return buff;
	}
	

	/**
	 * Parses an obj file with geometry data in it.
	 * NOTE:
	 * Geometry data in obj file MUST be in counter-clockwise draw order
	 * @param collisionObjLoc Location of obj file
	 * @return Shape from obj file
	 */
	private Vector2[] parseCollisionObj(String collisionObjLoc){
		try{
			// for to reading through file
			BufferedReader reader = new BufferedReader(new InputStreamReader(Game.resources.openAsset(collisionObjLoc)));
			// current line
			String line;
			
			ArrayList<Vector2> verts = new ArrayList<Vector2>();
			
			while((line = reader.readLine()) != null){
				// split the line up at spaces
				StringTokenizer toker = new StringTokenizer(line, " ");
				// grab the line's type
				String lineType = toker.nextToken();
				
				// vertex
				if (lineType.equals("v")) {
					// grab the coordinates
					float x = Float.parseFloat(toker.nextToken()) * xScale;
					
					// ignore z since we're 2D
					toker.nextToken();
					float y = Float.parseFloat(toker.nextToken()) * yScale;
					
					verts.add(new Vector2(x, y));
				}
			}
			reader.close();
			
			// put ArrayList into an array
			Vector2[] arr = new Vector2[verts.size()];
			int i = 0;
			Iterator<Vector2> it = verts.iterator();
			while(it.hasNext()){
				arr[i] = it.next();
				i++;
			}
			
			return arr;
		} catch(IOException e){
			e.printStackTrace();
		}
		
		return null;
	}
}
