package com.bitwaffle.guts.graphics;

import java.nio.Buffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

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
	private float pixelWidth, pixelHeight;
	
	/** How wide to render this sub-image so its the right raio (height is always 1) */
	private float renderWidth;
	
	/**
	 * Create a new sub-image
	 * @param sheetHandle Handle for source image
	 * @param texCoords Texture coordinates to use for this sub-image
	 */
	public SubImage(int sheetHandle, float width, float height, Buffer texCoords){
		this.sheetHandle = sheetHandle;
		this.texCoords = texCoords;
		this.pixelWidth = width;
		this.pixelHeight = height;
		this.renderWidth = (float)width / (float)height;
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
	
	/**
	 * @return Width of this subimage is, in pixels
	 */
	public float getPixelWidth(){
		return this.pixelWidth;
	}
	
	/**
	 * @return Height of this subimage is, in pixels
	 */
	public float getPixelHeight(){
		return this.pixelHeight;
	}
	
	/**
	 * Renders this subimage
	 * @param renderer Rendere to use
	 */
	public void render(Render2D renderer){
		this.render(renderer, 1.0f, 1.0f);
	}
	

	/**
	 * Renders this subimage at a given scale
	 * @param renderer Renderer to use
	 * @param xScale X scale to render at
	 * @param yScale Y scale to render at
	 */
	public void render(Render2D renderer, float xScale, float yScale){
		this.render(renderer, xScale, yScale, false, false);
	}
	
	/**
	 * Renders this subimage
	 * @param renderer Renderer to use
	 * @param flipHorizontal Whether or not to flip the image horizontally
	 * @param flipVertical Whether or not to flip the image vertically
	 */
	public void render(Render2D renderer, boolean flipHorizontal, boolean flipVertical){
		this.render(renderer, 1.0f, 1.0f, flipHorizontal, flipVertical);
	}
	

	/**
	 * Renders this subimage
	 * @param renderer Renderer to use
	 * @param xScale X scale to render at
	 * @param yScale Y scale to render at
	 * @param flipHorizontal Whether or not to flip horizontally
	 * @param flipVertical Whether or not to flip vertically
	 */
	public void render(Render2D renderer,  float xScale, float yScale, boolean flipHorizontal, boolean flipVertical){
		Gdx.gl20.glBindTexture(GL20.GL_TEXTURE_2D, sheetHandle);
		renderer.quad.render(getRenderWidth() * xScale, getRenderHeight() * yScale, flipHorizontal, flipVertical, texCoords);
	}

}
