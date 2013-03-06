package com.bitwaffle.guts.graphics;

import java.nio.Buffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.bitwaffle.guts.graphics.shapes.Quad;

/**
 * A sub-image within a larger texture
 * 
 * @author TranquilMarmot
 */
public class SubImage {
	/** Handle for binding source image */
	private int sheetHandle;
	
	/** Texture coordinates to use for drawing sub-image */
	private Buffer texCoords;
	
	/** Size of this sub-image, in pixels */
	private float width, height;
	
	private float renderWidth;
	
	/**
	 * Create a new sub-image
	 * @param sheetHandle Handle for source image
	 * @param texCoords Texture coordinates to use for this sub-image
	 */
	public SubImage(int sheetHandle, float width, float height, Buffer texCoords){
		this.sheetHandle = sheetHandle;
		this.texCoords = texCoords;
		this.width = width;
		this.height = height;
		this.renderWidth = (float)width / (float)this.height;
	}
	
	/**
	 * @return Width to render this frame at
	 */
	public float getRenderWidth(){
		return renderWidth;
	}
	
	/**
	 * @return Height to render this frame on
	 */
	public float getRenderHeight(){
		return 1.0f;
	}
	
	public float getPixelWidth(){
		return this.width;
	}
	
	public float getPixelHeight(){
		return this.height;
	}
	
	public void render(Render2D renderer){
		this.render(renderer, 1.0f, 1.0f);
	}
	
	/**
	 * Draws a sub-image
	 * @param quad Quad to use for drawing
	 * @param width Width to draw sub-image as
	 * @param height Height to draw sub-image as
	 */
	public void render(Render2D renderer, float xScale, float yScale){
		this.render(renderer, xScale, yScale, false, false);
	}
	
	public void render(Render2D renderer, boolean flipHorizontal, boolean flipVertical){
		this.render(renderer, 1.0f, 1.0f, flipHorizontal, flipVertical);
	}
	
	/**
	 * Draws a sub-image
	 * @param quad Quad to use for drawing
	 * @param width Width to draw sub-image as
	 * @param height Height to draw sub-image as
	 * @param flipHorizontal Whether or not the flip the sub-image horizontally
	 * @param flipVertical Whether or not to flip the sub-image vertically
	 */
	public void render(Render2D renderer,  float xScale, float yScale, boolean flipHorizontal, boolean flipVertical){
		Gdx.gl20.glBindTexture(GL20.GL_TEXTURE_2D, sheetHandle);
		renderer.quad.render(getRenderWidth() * xScale, getRenderHeight() * yScale, flipHorizontal, flipVertical, texCoords);
	}

}
