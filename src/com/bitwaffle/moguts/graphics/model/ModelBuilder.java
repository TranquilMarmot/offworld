package com.bitwaffle.moguts.graphics.model;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.bitwaffle.moguts.graphics.render.GLRenderer;

import android.opengl.GLES20;

/**
 * Used to build a model being read in from a file. One of these should be
 * created for every model being created, and the ultimate outcome is getting a
 * {@link Model} from calling <code>makeModel</code>
 * 
 * @author TranquilMarmot
 * @see ModelLoader
 */
public class ModelBuilder {
	/** the vertices of the model */
	private ArrayList<Vector3f> vertices;

	/** the normals of the model */
	private ArrayList<Vector3f> normals;

	/** the texture coordinates of the model */
	private ArrayList<Vector2f> textureCoords;

	/** which vertices to call */
	private ArrayList<int[]> vertexIndices;

	/** which normals to call */
	private ArrayList<int[]> normalIndices;

	/** which texture coordinates to call */
	private ArrayList<int[]> textureIndices;
	
	/** max and min values for the model being built */
	public float maxX, minX, maxY, minY, maxZ, minZ = 0.0f;
	
	/**
	 * See {@link ModelPart}
	 */
	private int currentIndex = 0, count = 0;
	/** material to use for current ModelPart*/
	private Material currentMaterial;
	
	/** all the model parts */
	private ArrayList<ModelPart> modelParts;
	
	/** Whether or not we're in the middle of making a ModelPart (endModelPart hasn't been called after beginModelPart) */
	private boolean makingModelPart = false;

	/**
	 * ModelBuilder initializer
	 */
	public ModelBuilder() {
		/*
		 * We have to add a blank element to the beginning of each list, as the obj
		 * file starts referencing elements at 1, but ArrayLists start at 0
		 */
		vertices = new ArrayList<Vector3f>();
		vertices.add(new Vector3f(0.0f, 0.0f, 0.0f));

		normals = new ArrayList<Vector3f>();
		normals.add(new Vector3f(0.0f, 0.0f, 0.0f));

		textureCoords = new ArrayList<Vector2f>();
		textureCoords.add(new Vector2f(0.0f, 0.0f));

		// these just store which vertices to grab, don't need to add a blank
		// element to them
		vertexIndices = new ArrayList<int[]>();
		normalIndices = new ArrayList<int[]>();
		textureIndices = new ArrayList<int[]>();
		
		// initialize model parts array
		modelParts = new ArrayList<ModelPart>();
	}

	/**
	 * Add a vertex to the model being built
	 * 
	 * @param vertex
	 *            The vertex to add
	 */
	public void addVertex(Vector3f vertex) {
		// check for max and min values
		if(vertex.x > maxX)
			maxX = vertex.x;
		
		if(vertex.x < minX)
			minX = vertex.x;
		
		if(vertex.y > maxY)
			maxY = vertex.y;
		if(vertex.y < minY)
			minY = vertex.y;
		
		if(vertex.z > maxZ)
			maxZ = vertex.z;
		if(vertex.z < minZ)
			minZ = vertex.z;
		
		vertices.add(vertex);
	}

	/**
	 * Add vertex indices to the model being built. Automatically splits quads
	 * into triangles for simplicity.
	 * 
	 * @param indices The indices to add
	 */
	public void addVertexIndices(int[] indices) {
		// add if it's just a triangle
		if (indices.length == 3){
			vertexIndices.add(indices);
			
			// increase the count of indices for the current model part by 3 (one triangle)
			count += 3;
		}
		// else split the quad into two triangles
		else if (indices.length == 4) {
			/*
			 * NOTE
			 * It might be important to note that, for every quad split into a triangle,
			 * two of its vertex indices are duplicated. So what was originally
			 * 4 indices become 6 indices.
			 * This makes drawing much simpler, as OpenGL can only draw in one mode
			 * (i.e. GL_TRIANGLES or GL_QUADS). So we don't have to keep switching
			 * between the two (just draw all triangles).
			 */
			/*
			 * Given indices 0,1,2,3 a quad can be split into two triangles:
			 * 0,1,2 and 2,3,0
			 */
			int[] tri1 = new int[3];
			tri1[0] = indices[0];
			tri1[1] = indices[1];
			tri1[2] = indices[2];

			vertexIndices.add(tri1);

			int[] tri2 = new int[3];
			tri2[0] = indices[2];
			tri2[1] = indices[3];
			tri2[2] = indices[0];

			vertexIndices.add(tri2);
			
			// increase the count of indices for the current model part by 6 (two triangle)
			count += 6;
		} else {
			System.out
					.println("Error! Array not a triangle or a quad! (ModelBuilder)");
		}
	}

	/**
	 * Add a normal to the model being built.
	 * 
	 * @param vertex
	 *            The vertex to add
	 */
	public void addNormal(Vector3f vertex) {
		normals.add(vertex);
	}

	/**
	 * Add normal indices to the model being built. Automatically splits quads
	 * into triangles for simplicity.
	 * 
	 * @param indices The indices to add
	 */
	public void addNormalIndices(int[] indices) {
		// add if it's just a triangle
		if (indices.length == 3)
			normalIndices.add(indices);
		// else split the quad into two triangles
		else if (indices.length == 4) {
			int[] tri1 = new int[3];
			tri1[0] = indices[0];
			tri1[1] = indices[1];
			tri1[2] = indices[2];

			normalIndices.add(tri1);

			int[] tri2 = new int[3];
			tri2[0] = indices[2];
			tri2[1] = indices[3];
			tri2[2] = indices[0];

			normalIndices.add(tri2);
		} else {
			System.out
					.println("Error! Array not a triangle or a quad! (ModelBuilder)");
		}
	}

	/**
	 * Add texture coordinates to the model being built. Only 2D texture coordinates are supported right now.
	 * @param point The texture coordinates to add
	 */
	public void addTextureCoords(Vector2f point) {
		textureCoords.add(point);
	}

	/**
	 * Add texture coordinate indices to the model being built. Automatically splits quads
	 * into triangles for simplicity.
	 * @param indices The indices to add
	 */
	public void addTetxureIndices(int[] indices) {
		if (indices.length == 3)
			textureIndices.add(indices);
		else if (indices.length == 4) {
			int[] tri1 = new int[3];
			tri1[0] = indices[0];
			tri1[1] = indices[1];
			tri1[2] = indices[2];

			textureIndices.add(tri1);

			int[] tri2 = new int[3];
			tri2[0] = indices[2];
			tri2[1] = indices[3];
			tri2[2] = indices[0];

			textureIndices.add(tri2);
		} else {
			System.out
					.println("Error! Array not a triangle or a quad! (ModelBuilder)");
		}
	}

	/**
	 * This method should be called after all the vertices, faces, and normals
	 * and their respective indices have been added.
	 * @return A model built using the current indices
	 */
	public Model makeModel(/*Textures texture*/) {
		if(makingModelPart){
			endModelPart();
		}
		int[] handles = fillVertexArray();
		return new Model(/*buildCollisionShape(),*/ handles[0], handles[1], handles[2], modelParts/*, texture*/);
	}
	
	/**
	 * Fills an array buffer with the given data
	 * @return The vertex array object handle
	 */
	private int[] fillVertexArray(){
		// get a handle for a VAO
		//int vaoHandle = GLES20.glGenVertexArrays();
		//GLES20.glBindVertexArray(vaoHandle);
		
		// create buffers and fill them with data
		FloatBuffer vertBuffer = ByteBuffer.allocateDirect(vertexIndices.size() * 9).asFloatBuffer();
		FloatBuffer normBuffer = ByteBuffer.allocateDirect(normalIndices.size() * 9).asFloatBuffer();
		FloatBuffer texBuffer = ByteBuffer.allocateDirect(textureIndices.size() * 6).asFloatBuffer();
		for(int i = 0; i < vertexIndices.size(); i++){
			int[] triVerts = vertexIndices.get(i); 
			int[] triNorms = normalIndices.get(i);
			int[] triTex = textureIndices.get(i);
			
			Vector3f firstVert = vertices.get(triVerts[0]);
			vertBuffer.put(firstVert.x);
			vertBuffer.put(firstVert.y);
			vertBuffer.put(firstVert.z);
			Vector3f firstNorm = normals.get(triNorms[0]);
			normBuffer.put(firstNorm.x);
			normBuffer.put(firstNorm.y);
			normBuffer.put(firstNorm.z);
			Vector2f firstTex = textureCoords.get(triTex[0]);
			texBuffer.put(firstTex.x);
			texBuffer.put(1 - firstTex.y);
			
			Vector3f secondVert = vertices.get(triVerts[1]);
			vertBuffer.put(secondVert.x);
			vertBuffer.put(secondVert.y);
			vertBuffer.put(secondVert.z);
			Vector3f secondNorm = normals.get(triNorms[1]);
			normBuffer.put(secondNorm.x);
			normBuffer.put(secondNorm.y);
			normBuffer.put(secondNorm.z);
			Vector2f secondTex = textureCoords.get(triTex[1]);
			texBuffer.put(secondTex.x);
			texBuffer.put(1 - secondTex.y);
			
			
			Vector3f thirdVert = vertices.get(triVerts[2]);
			vertBuffer.put(thirdVert.x);
			vertBuffer.put(thirdVert.y);
			vertBuffer.put(thirdVert.z);
			Vector3f thirdNorm = normals.get(triNorms[2]);
			normBuffer.put(thirdNorm.x);
			normBuffer.put(thirdNorm.y);
			normBuffer.put(thirdNorm.z);
			Vector2f thirdTex = textureCoords.get(triTex[2]);
			texBuffer.put(thirdTex.x);
			texBuffer.put(1 - thirdTex.y);
		}
		// be kind, please rewind()!
		vertBuffer.rewind();
		normBuffer.rewind();
		texBuffer.rewind();
		
		// handles for filling buffer objects
		IntBuffer vboHandles = ByteBuffer.allocateDirect(3).asIntBuffer();/*BufferUtils.createIntBuffer(3);*/
		//GL15.glGenBuffers(vboHandles);
		GLES20.glGenBuffers(3, vboHandles);
		
		
		
		// actually fill the buffers
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboHandles.get(0));
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertBuffer.capacity(), vertBuffer, GLES20.GL_STATIC_DRAW);
		// FIXME should this be null?!
		GLES20.glVertexAttribPointer(GLRenderer.render3D.getVertexLocationPosition(), 3, GLES20.GL_FLOAT, false, 0, null);
		//GLES20.glEnableVertexAttribArray(0);
		
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboHandles.get(1));
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, normBuffer.capacity(), normBuffer, GLES20.GL_STATIC_DRAW);
		GLES20.glVertexAttribPointer(GLRenderer.render3D.getVertexNormalPosition(), 3, GLES20.GL_FLOAT, false, 0, null); // FIXME null?!
		//GLES20.glEnableVertexAttribArray(1);
		
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboHandles.get(2));
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, texBuffer.capacity(), texBuffer, GLES20.GL_STATIC_DRAW);
		GLES20.glVertexAttribPointer(GLRenderer.render3D.getVertexTexCoordPosition(), 2, GLES20.GL_FLOAT, false, 0, null); //FIXME Null?!
		//GLES20.glEnableVertexAttribArray(2);
		
		//GLES20.glBindVertexArray(0);
		
		int[] handles = { vboHandles.get(0), vboHandles.get(1), vboHandles.get(2) };
		
		return handles;
	}
	
	/**
	 * This should be called whenever a new set of vertices with a different material needs to be created.
	 * This should be called after all vertices have been added and while vertex indices are being added
	 * @param mat Material to use for incoming vertex indices
	 */
	public void startModelPart(Material mat){
		// end the current model part if we're making one
		if(isMakingModelPart())
			endModelPart();
		
		// set the current material
		currentMaterial = mat;
		// let everyone know that we're now making a model part
		makingModelPart = true;
	}
	
	/**
	 * Ends the current model part
	 */
	public void endModelPart(){
		// add the model part
		modelParts.add(new ModelPart(currentMaterial, currentIndex, count));
		
		// advance the current index and set count to 0 for the next model part
		currentIndex += count;
		count = 0;
		
		// let everyone know that we're done making the current model part
		makingModelPart = false;
	}
	
	/**
	 * @return Whether or not a model part is being made right now
	 */
	public boolean isMakingModelPart(){
		return makingModelPart;
	}
	
	/**
	 * Builds a collision shape for this model
	 * @return A convex hull collision shape representing this model
	 */
	/*
	private CollisionShape buildCollisionShape(){
		ConvexShape originalConvexShape = new ConvexHullShape(vertices);
		
		// create a hull based on the vertices
		ShapeHull hull = new ShapeHull(originalConvexShape);
		float margin = originalConvexShape.getMargin();
		hull.buildHull(margin);
		
		return new ConvexHullShape(hull.getVertexPointer());
	}*/
}
