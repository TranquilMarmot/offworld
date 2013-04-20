package com.bitwaffle.guts.graphics.shapes.model;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class defines a 3D model. A model is a set of vertices to draw and what texture to use when drawing them.
 * @author TranquilMarmot
 *
 */
public class Model {
	/** Texture to use for rendering */
	private String texture;
	/** All the parts of the model (each has a different material) */
	private ArrayList<ModelPart> parts;
	/** Buffers containing model's data */
	private Buffer coordBuffer, texBuffer, normBuffer;
	
	/**
	 * Create a model
	 * @param coordBuffer Buffer with position coordinates
	 * @param texBuffer Buffer with texture coordinates
	 * @param normBuffer Buffer with normal coordinates
	 * @param parts Parts of the model
	 * @param texture Texture that the model uses
	 */
	public Model(Buffer coordBuffer, Buffer texBuffer, Buffer normBuffer,
			ArrayList<ModelPart> parts, String texture){
		// these all come from the model loader
		this.coordBuffer = coordBuffer;
		this.texBuffer = texBuffer;
		this.normBuffer = normBuffer;
		this.texture = texture;
		this.parts = parts;
	}

	/** @return Texture to use to render model */
	public String getTexture() { return texture; }
	
	/** @return Buffer of position coordinates */
	public Buffer getCoordBuffer(){ return coordBuffer; }
	
	/** @return Buffer of texture coordinates */
	public Buffer getTexCoordBuffer(){ return texBuffer; }
	
	/** @return Buffer of normal coordinates */
	public Buffer getNormalBuffer(){ return normBuffer; }
	
	/** @return Iterator of this model's parts */
	public Iterator<ModelPart> partsIterator(){ return parts.iterator(); }
}
