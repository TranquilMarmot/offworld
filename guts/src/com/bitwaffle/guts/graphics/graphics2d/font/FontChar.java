package com.bitwaffle.guts.graphics.graphics2d.font;

import java.nio.Buffer;

import com.bitwaffle.guts.graphics.graphics2d.Render2D;

/**
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
	
	/** @param texCoordBuff Buffer containing texture coordinates for character */
	public FontChar(Buffer texCoordBuff){
		this.texCoordBuff = texCoordBuff;
	}
	
	/** Draw a character */
	public void draw(Render2D renderer, float width, float height){
		renderer.quad.render(width, height, false, false, texCoordBuff);
	}
}