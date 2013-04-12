package com.bitwaffle.guts.graphics.shapes.polygon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.BufferUtils;
import com.bitwaffle.guts.Game;

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
	 * @param renderObjLoc Location of obj file to use for rendering
	 * @param collisiionObjLoc Location of obj file to use for geometry
	 * @return Polygon object containing necessary data for physics and rendering
	 */
	public static Polygon loadPolygon(float xScale, float yScale, ArrayList<String> renderObjLocs, ArrayList<String> textureNames, String collisiionObjLoc, Polygon.Types shapeType, String debugObjLoc){
		return new PolygonLoader(xScale, yScale).loadPolygon(renderObjLocs, textureNames, collisiionObjLoc, shapeType, debugObjLoc);
	}
	
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
		ArrayList<Buffer> vertBuffers = new ArrayList<Buffer>(), texCoordBuffers = new ArrayList<Buffer>();
		ArrayList<Integer> counts = new ArrayList<Integer>();
		for(String renderObjLoc : renderObjLocs){
			// this fills up the ArrayLists and increments count
			parseRenderObj(renderObjLoc);
			// done parsing! let's fill some float buffers!
			Buffer vertexBuffer = putVerticesIntoBuffer();
			Buffer texCoordBuffer = putTextureCoordsIntoBuffer();
			
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
		
		Buffer debugVertexBuffer = null, debugTexCoordBuffer = null;
		int debugCount = -1;
		if(!debugObjLoc.equals("")){
			parseRenderObj(debugObjLoc);
			
			debugVertexBuffer = putVerticesIntoBuffer();
			debugTexCoordBuffer = putTextureCoordsIntoBuffer();
			debugCount = count;
		}
		
		Vector2[] geom = null;
		if(!collisionObjLoc.equals(""))
			geom = parseCollisionObj(collisionObjLoc);
		
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
					Gdx.app.debug(LOGTAG, "Attempted to load mtllib in render obj (not supported)");
				
				// new material
				if(line.startsWith("usemtl"))
					Gdx.app.debug(LOGTAG, "Attempted to use material in render obj (not supported)");
				
				// object name
				if(lineType.equals("o")){
					Gdx.app.debug(LOGTAG, "Loading \"" + toker.nextToken() + "\" from \"" + renderObjLoc + "\"");
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
						Gdx.app.debug(LOGTAG, "Error! Count " + numVertices + " in PolygonLoader face! (Exptected 3 or 4)");
					}
				}
			}
			reader.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Add a quad to a list of indices as two triangles.
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
	
	/** Steps through the given indices and adds their corresponding vertices to a buffer */
	private Buffer putVerticesIntoBuffer(){
		// 9, since there's three vertices per index and three floats per vertex
		// then * 4 since there's 4 bytes/float
		Buffer buff = BufferUtils.newByteBuffer(vertexIndices.size() * 9 * 4);
		
		float[] tmp = new float[vertexIndices.size() * 9];
		
		// each index corresponds to a triangle, which is three points
		for(int i = 0; i < vertexIndices.size(); i++){
			int[] verts = vertexIndices.get(i);
			int ind = i * 9;
			
			Vector2 first = vertices.get(verts[0]);
			tmp[ind + 0] = first.x;
			tmp[ind + 1] = first.y;
			tmp[ind + 2] = 0.0f; // always use 0 for Z since we're 2D
			
			Vector2 second = vertices.get(verts[1]);
			tmp[ind + 3] = second.x;
			tmp[ind + 4] = second.y;
			tmp[ind + 5] = 0.0f;
			
			Vector2 third = vertices.get(verts[2]);
			tmp[ind + 6] = third.x;
			tmp[ind + 7] = third.y;
			tmp[ind + 8] = 0.0f;
		}
		
		BufferUtils.copy(tmp, buff, tmp.length, 0);
		buff.rewind();
		return buff;
	}
	
	/** Steps through the given indices and adds their corresponding vertices to a buffer */
	private Buffer putTextureCoordsIntoBuffer(){
		// 6, since there's three vertices per index and two floats per vertex (no Z for texture)
		// then * 4 since there's 4 bytes/float
		Buffer buff = BufferUtils.newByteBuffer(textureIndices.size() * 6 * 4);
		
		float[] tmp = new float[textureIndices.size() * 6];
		
		// each index corresponds to a triangle, which is three points
		for(int i = 0; i < textureIndices.size(); i++){
			int[] verts = textureIndices.get(i);
			int ind = i * 6; 
			
			Vector2 first = textureCoords.get(verts[0]);
			tmp[ind + 0] = first.x;
			tmp[ind + 1] = 1.0f - first.y;
			
			Vector2 second = textureCoords.get(verts[1]);
			tmp[ind + 2] = second.x;
			tmp[ind + 3] = 1.0f - second.y;
			
			Vector2 third = textureCoords.get(verts[2]);
			tmp[ind + 4] = third.x;
			tmp[ind + 5] = 1.0f - third.y;
		}
		
		BufferUtils.copy(tmp, buff, tmp.length, 0);
		
		buff.rewind();
		return buff;
	}
	

	/**
	 * Parses an obj file with geometry data in it.
	 * NOTE:
	 * Geometry data in obj file MUST be in counter-clockwise draw order
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
