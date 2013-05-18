package com.bitwaffle.guts.graphics.shapes.model;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import com.badlogic.gdx.utils.BufferUtils;

/**
 * Used to build a model being read in from a file. One of these should be
 * created for every model being created, and the ultimate outcome is getting a
 * {@link Model} from calling <code>makeModel</code>
 * 
 * @author TranquilMarmot
 * @see ObjParser
 */
public class ModelBuilder {
	/** the vertices of the model */
	private ArrayList<float[]> vertices;

	/** the normals of the model */
	private ArrayList<float[]> normals;

	/** the texture coordinates of the model */
	private ArrayList<float[]> textureCoords;

	/** which vertices to call */
	private ArrayList<int[]> vertexIndices;

	/** which normals to call */
	private ArrayList<int[]> normalIndices;

	/** which texture coordinates to call */
	private ArrayList<int[]> textureIndices;
	
	/** max and min values for the model being built */
	public float maxX, minX, maxY, minY, maxZ, minZ = 0.0f;
	
	/** See {@link ModelPart} */
	private int currentIndex = 0, count = 0;
	/** material to use for current ModelPart*/
	private Material currentMaterial;
	
	/** all the model parts */
	private ArrayList<ModelPart> modelParts;
	
	/** Whether or not we're in the middle of making a ModelPart (endModelPart hasn't been called after beginModelPart) */
	private boolean makingModelPart = false;

	public ModelBuilder() {
		/*
		 * We have to add a blank element to the beginning of each list, as the obj
		 * file starts referencing elements at 1, but ArrayLists start at 0
		 */
		vertices = new ArrayList<float[]>();
		vertices.add(new float[]{0.0f, 0.0f, 0.0f});

		normals = new ArrayList<float[]>();
		normals.add(new float[]{0.0f, 0.0f, 0.0f});

		textureCoords = new ArrayList<float[]>();
		textureCoords.add(new float[]{0.0f, 0.0f});

		// these just store which vertices to grab, don't need to add a blank
		// element to them
		vertexIndices = new ArrayList<int[]>();
		normalIndices = new ArrayList<int[]>();
		textureIndices = new ArrayList<int[]>();
		
		// initialize model parts array
		modelParts = new ArrayList<ModelPart>();
	}

	/** Add a vertex to the model being built */
	public void addVertex(float[] vertex) {
		// check for max and min values
		if(vertex[0] > maxX)
			maxX = vertex[0];
		if(vertex[0] < minX)
			minX = vertex[0];
		
		if(vertex[1] > maxY)
			maxY = vertex[1];
		if(vertex[1] < minY)
			minY = vertex[1];
		
		if(vertex[2] > maxZ)
			maxZ = vertex[2];
		if(vertex[2] < minZ)
			minZ = vertex[2];
		
		vertices.add(vertex);
	}

	/**
	 * Add vertex indices to the model being built. Automatically splits quads
	 * into triangles for simplicity.
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
			System.out.println("Error! Array not a triangle or a quad! (ModelBuilder)");
		}
	}

	/** Add a normal to the model being built. */
	public void addNormal(float[] vertex) {
		normals.add(vertex);
	}

	/**
	 * Add normal indices to the model being built. Automatically splits quads
	 * into triangles for simplicity.
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
			System.out.println("Error! Array not a triangle or a quad! (ModelBuilder)");
		}
	}

	/** Add texture coordinates to the model being built. Only 2D texture coordinates are supported right now. */
	public void addTextureCoords(float[] point) {
		textureCoords.add(point);
	}

	/**
	 * Add texture coordinate indices to the model being built. Automatically splits quads
	 * into triangles for simplicity.
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
			System.out.println("Error! Array not a triangle or a quad! (ModelBuilder)");
		}
	}

	/**
	 * This method should be called after all the vertices, faces, and normals
	 * and their respective indices have been added.
	 * @return A model built using the current indices
	 */
	public Model makeModel(String texture) {
		if(makingModelPart){
			endModelPart();
		}
		
		// create buffers and fill them with data
		ByteBuffer vertBuffer = BufferUtils.newByteBuffer(vertexIndices.size() * 9 * 4);
		ByteBuffer normBuffer = BufferUtils.newByteBuffer(normalIndices.size() * 9 * 4);
		ByteBuffer texBuffer = BufferUtils.newByteBuffer(textureIndices.size() * 6 * 4);
		
		float[] tempVerts = new float[vertexIndices.size() * 9];
		float[] tempNorms = new float[normalIndices.size() * 9];
		float[] tempTex = new float[textureIndices.size() * 6];
		
		for(int i = 0; i < vertexIndices.size(); i++){
			int[] triVerts = vertexIndices.get(i); 
			int[] triNorms = normalIndices.get(i);
			int[] triTex = textureIndices.get(i);
			
			int ind = i * 9;
			int texInd = i * 6;
			
			float[] firstVert = vertices.get(triVerts[0]);
			tempVerts[ind + 0] = firstVert[0];
			tempVerts[ind + 1] = firstVert[1];
			tempVerts[ind + 2] = firstVert[2];
			float[] firstNorm = normals.get(triNorms[0]);
			tempNorms[ind + 0] = firstNorm[0];
			tempNorms[ind + 1] = firstNorm[1];
			tempNorms[ind + 2] = firstNorm[2];
			float[] firstTex = textureCoords.get(triTex[0]);
			tempTex[texInd + 0] = firstTex[0];
			tempTex[texInd + 1] = 1.0f - firstTex[1];
			
			float[] secondVert = vertices.get(triVerts[1]);
			tempVerts[ind + 3] = secondVert[0];
			tempVerts[ind + 4] = secondVert[1];
			tempVerts[ind + 5] = secondVert[2];
			float[] secondNorm = normals.get(triNorms[1]);
			tempNorms[ind + 3] = secondNorm[0];
			tempNorms[ind + 4] = secondNorm[1];
			tempNorms[ind + 5] = secondNorm[2];
			float[] secondTex = textureCoords.get(triTex[1]);
			tempTex[texInd + 2] = secondTex[0];
			tempTex[texInd + 3] = 1.0f - secondTex[1];
			
			float[] thirdVert = vertices.get(triVerts[2]);
			tempVerts[ind + 6] = thirdVert[0];
			tempVerts[ind + 7] = thirdVert[1];
			tempVerts[ind + 8] = thirdVert[2];
			float[] thirdNorm = normals.get(triNorms[2]);
			tempNorms[ind + 6] = thirdNorm[0];
			tempNorms[ind + 7] = thirdNorm[1];
			tempNorms[ind + 8] = thirdNorm[2];
			float[] thirdTex = textureCoords.get(triTex[2]);
			tempTex[texInd + 4] = thirdTex[0];
			tempTex[texInd + 5] = 1.0f - thirdTex[1];
		}
		
		BufferUtils.copy(tempVerts, vertBuffer, tempVerts.length, 0);
		BufferUtils.copy(tempNorms, normBuffer, tempNorms.length, 0);
		BufferUtils.copy(tempTex, texBuffer, tempTex.length, 0);
		
		// be kind, please rewind()!
		vertBuffer.rewind();
		normBuffer.rewind();
		texBuffer.rewind();
		
		return new Model(vertBuffer, texBuffer, normBuffer, modelParts, texture);
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
	
	/** Ends the current model part */
	public void endModelPart(){
		// add the model part
		modelParts.add(new ModelPart(currentMaterial, currentIndex, count));
		
		// advance the current index and set count to 0 for the next model part
		currentIndex += count;
		count = 0;
		
		// let everyone know that we're done making the current model part
		makingModelPart = false;
	}
	
	/** @return Whether or not a model part is being made right now */
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
	}
	*/
}
