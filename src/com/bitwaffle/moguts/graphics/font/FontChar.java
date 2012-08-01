package com.bitwaffle.moguts.graphics.font;

import java.nio.FloatBuffer;

import com.bitwaffle.moguts.graphics.render.Render2D;

/**
 * Character in a font
 * 
 * @author TranquilMarmot
 */
public class FontChar {
	/**
	 * This will contain texture coordinate info for
	 * the location of this char in the font's
	 * texture image
	 */
	private FloatBuffer texCoordBuff;
	
	/**
	 * Create a new font character
	 * @param texCoordBuff Buffer containing texture coordinates for character
	 */
	public FontChar(FloatBuffer texCoordBuff){
		this.texCoordBuff = texCoordBuff;
	}
	
	/**
	 * Draw a character
	 * @param renderer Renderer to use for drawing
	 * @param width Width of character
	 * @param height Height of character
	 */
	public void draw(Render2D renderer, float width, float height){
		renderer.quad.draw(renderer, width, height, false, false, texCoordBuff);
	}
}