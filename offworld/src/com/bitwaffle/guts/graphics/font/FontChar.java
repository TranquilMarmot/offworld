package com.bitwaffle.guts.graphics.font;

import java.nio.Buffer;

import com.bitwaffle.guts.graphics.Render2D;

/**
 * A character in a font
 * Each character keeps track of it's own texture
 * coordinates within the font texture.
 * 
 * @author TranquilMarmot
 */
public class FontChar {
	/**
	 * This will contain texture coordinate info for
	 * the location of this char in the font's
	 * texture image
	 */
	private Buffer texCoordBuff;
	
	/**
	 * Create a new font character
	 * @param texCoordBuff Buffer containing texture coordinates for character
	 */
	public FontChar(Buffer texCoordBuff){
		this.texCoordBuff = texCoordBuff;
	}
	
	/**
	 * Draw a character
	 * @param renderer Renderer to use for drawing
	 * @param width Width of character
	 * @param height Height of character
	 */
	public void draw(Render2D renderer, float width, float height){
		renderer.quad.render(width, height, false, false, texCoordBuff);
	}
}