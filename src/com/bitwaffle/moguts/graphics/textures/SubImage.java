package com.bitwaffle.moguts.graphics.textures;

import java.nio.FloatBuffer;

import android.opengl.GLES20;

import com.bitwaffle.moguts.graphics.render.shapes.Quad;

/**
 * A sub-image within a larger texture
 * 
 * @author TranquilMarmot
 */
public class SubImage {
	/** Handle for binding source image */
	private int sheetHandle;
	
	/** Texture coordinates to use for drawing sub-image */
	private FloatBuffer texCoords;
	
	/**
	 * Create a new sub-image
	 * @param sheetHandle Handle for source image
	 * @param texCoords Texture coordinates to use for this sub-image
	 */
	public SubImage(int sheetHandle, FloatBuffer texCoords){
		this.sheetHandle = sheetHandle;
		this.texCoords = texCoords;
	}
	
	/**
	 * Draws a sub-image
	 * @param quad Quad to use for drawing
	 * @param width Width to draw sub-image as
	 * @param height Height to draw sub-image as
	 */
	public void draw(Quad quad, float width, float height){
		this.render(quad, width, height, false, false);
	}
	
	/**
	 * Draws a sub-image
	 * @param quad Quad to use for drawing
	 * @param width Width to draw sub-image as
	 * @param height Height to draw sub-image as
	 * @param flipHorizontal Whether or not the flip the sub-image horizontally
	 * @param flipVertical Whether or not to flip the sub-image vertically
	 */
	public void render(Quad quad, float width, float height, boolean flipHorizontal, boolean flipVertical){
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, sheetHandle);
		quad.draw(width, height, flipHorizontal, flipVertical, texCoords);
	}

}
