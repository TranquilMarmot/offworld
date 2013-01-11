package com.bitwaffle.guts.graphics.font;

import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.StringTokenizer;

import org.lwjgl.util.vector.Vector3f;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.render.Render2D;
import com.bitwaffle.guts.resources.ResourceLoader;
import com.bitwaffle.guts.util.BufferUtils;

/**
 * Pack my box with five dozen liquor jugs!
 * 
 * This only properly works with monospace fonts!
 * You can try others, but they ain't gonna work right.
 * In the font texture, the first character should be ' ',
 * followed by '!' and the rest of the ASCII character set
 * in row major order. The number of rows and columns will be
 * determined by the size of the font's texture and how big
 * each cell is.
 * 
 * When printing, each character is mapped to its ASCII value.
 * 
 * The font texture being used for this was generated by
 * Codehead's Bitmap Font generator, which can be found at
 * <a href=http://www.codehead.co.uk/cbfg/>codehead.co.uk/cbfg/</a>
 * 
 * @author TranquilMarmot
 */
public class BitmapFont {
	/** Info for font */
	private static final String FONT_LOCATION = "font.png";
	public static final int 
		/** How big each cell is */
		FONT_CELL_WIDTH = 128, FONT_CELL_HEIGHT = 128,
		/** How wide each glyph actually is and how much space to put between each glyph */
		FONT_GLYPH_WIDTH = 31, FONT_GLYPH_HEIGHT = 31;
	
	/** 
	 * ASCII value of chars[0], so that the index in chars can be calculated.
	 * Since the first 31 values in ASCII are pretty much useless to us (look it up)
	 * we simply skip them and offset every value by this.
	 */
	private static final int START_CHAR = 32;
	
	/** Handle for the texture for this font */
	private int texHandle;
	
	/** Array of every character for this font */
	FontChar[] chars;
	
	/**
	 * Create a new font
	 * @param imagePath Path to image in assets folder
	 * @param FONT_CELL_WIDTH Width of each cell
	 * @param FONT_CELL_HEIGHT Height of each cell
	 * @param FONT_GLYPH_WIDTH Width of each glyph
	 * @param FONT_GLYPH_HEIGHT How far apart each glyph is (typically, same as FONT_GLYPH_WIDTH)
	 */
	public BitmapFont(){
		try{
			// intialize texture
			InputStream in = Game.resources.openAsset(FONT_LOCATION);
			Bitmap bitmap = BitmapFactory.decodeStream(in);
			texHandle = ResourceLoader.initTexture(bitmap, GLES20.GL_NEAREST, GLES20.GL_NEAREST);
			
			// find out how many rows and columns we have and initialize the character array
			int numRows = bitmap.getWidth() / FONT_CELL_WIDTH;
			int numCols = bitmap.getHeight() / FONT_CELL_HEIGHT;
			chars = new FontChar[numRows * numCols];
			
			// go through each row/column and create its character
			for(int row = 0; row < numRows; row++)
				for(int col = 0; col < numCols; col++)
					chars[(numCols * col) + row] = initFontChar(bitmap, row, col);
			
			// clean up after ourselves
			in.close();
			
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Initialize a FontChar at a given row/column for a given bitmap
	 * @param source Bitmap that character lies on
	 * @param row Row of character
	 * @param col Column of character
	 * @return Initialized FontChar
	 */
	private FontChar initFontChar(Bitmap source, int row, int col){
		// find our texture coordinates
		float texX = (float)(row * FONT_CELL_WIDTH) / (float)source.getWidth();
		float texY = (float)(col * FONT_CELL_HEIGHT) / (float)source.getHeight();
		float texWidth = (float)FONT_CELL_WIDTH / (float)source.getWidth();
		float texHeight = (float)FONT_CELL_HEIGHT / (float)source.getHeight();
		
		// send texture coordinates to a float buffer (buffer is used when glyph is drawn)
		float[] texCoords = {
				texX, texY,
				texX + texWidth, texY,
				texX + texWidth, texY + texHeight,
				
				texX + texWidth, texY + texHeight,
				texX, texY + texHeight,
				texX, texY
		};
		FloatBuffer buff = BufferUtils.getFloatBuffer(texCoords.length);
		buff.put(texCoords);
		buff.rewind();
		
		return new FontChar(buff);
	}
	
	/**
	 * Draw a string!
	 * @param text Text to draw
	 * @param renderer What to draw text with
	 * @param x X location of text
	 * @param y Y location of text
	 */
	public void drawString(String text, Render2D renderer, float x, float y){
		this.drawString(text, renderer, x, y, 1.0f);
	}
	
	/**
	 * Draw a string!
	 * @param text Text to draw
	 * @param renderer What to draw text with
	 * @param x X location of text
	 * @param y Y location of text
	 * @param scale Scale to draw text at
	 */
	public void drawString(String text, Render2D renderer, float x, float y, float scale){
		// draw white text by default
		this.drawString(text, renderer, x, y, scale, new float[]{1.0f, 1.0f, 1.0f, 1.0f});
	}
	
	/**
	 * Draw a string!
	 * @param text Text to draw
	 * @param renderer What to draw text with
	 * @param x X location of text
	 * @param y Y location of text
	 * @param scale Scale to draw text at
	 * @param color Color to draw font in- must have at least 4 elements, between 0 and 1
	 */
	public void drawString(String text, Render2D renderer, float x, float y, float scale, float[] color){
		// bind the font texture and set the color
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texHandle);
		renderer.program.setUniform("vColor", color[0], color[1], color[2], color[3]);
		
		// this gets advanced with every character drawn and reset to 0 on newlines
		float xOffset = FONT_GLYPH_WIDTH * scale;
		// in case we hit any newlines
		int lineNum = 0;
		
		// go through each character in the string
		char[] charArr = text.toCharArray();
		for(int i = 0; i < charArr.length; i++){
			// look for any carriage returns or line feeds
			if(charArr[i] == '\n' || charArr[i] == '\r'){
				lineNum++;
				xOffset = FONT_GLYPH_WIDTH * scale;
				continue;
			}
			
			// casting a char to an int returns its ASCII value
			int index = (int)charArr[i] - START_CHAR;
			// skip tabs TODO handle tabs? (just use spaces?)
			if(index < 0)
				continue;
			
			// scale and move the modelview to translate to the char's location
			renderer.modelview.setIdentity();
			renderer.modelview.translate(new Vector3f(x + xOffset, y + ((BitmapFont.FONT_GLYPH_HEIGHT * 2.0f) * scale * lineNum), 0.0f));
			renderer.modelview.scale(new Vector3f(scale, scale, 1.0f));
			renderer.sendModelViewToShader();
			
			// draw character
			chars[index].draw(renderer, FONT_GLYPH_WIDTH * 2.0f, FONT_GLYPH_HEIGHT * 2.0f);
			
			// advance to next char
			xOffset += FONT_GLYPH_WIDTH * scale;
		}
	}
	
	/**
	 * Get the width of a string
	 * @param string String to check width of
	 * @return Width of string
	 */
	public float stringWidth(String string){
		return stringWidth(string, 1.0f);
	}
	
	/**
	 * Get the width of a string, with scaling
	 * @param string String to check width of
	 * @param scale Scale string is being drawn at
	 * @return Width of string
	 */
	public float stringWidth(String string, float scale){
		// at the end of the longest line, that's where I will always be
		int longestLine = 0;
		
		// go through every line and find out how long it is, keeping track of the longest
		StringTokenizer toker = new StringTokenizer(string, "\n");
		while(toker.hasMoreTokens()){
			String line = toker.nextToken();
			int lineSize = line.length();
			if(lineSize > longestLine)
				longestLine = lineSize;
		}
		
		return (longestLine - 1) * FONT_GLYPH_WIDTH * scale;
	}
	
	/**
	 * Get the height of a string
	 * @param string String to check height of
	 * @return Height of string
	 */
	public float stringHeight(String string){
		return stringHeight(string, 1.0f);
	}
	
	/**
	 * Get the height of a string, with scaling
	 * @param string String to check height of
	 * @param scale Scale string is being drawn at
	 * @return Height of string
	 */
	public float stringHeight(String string, float scale){
		int numLines = new StringTokenizer(string, "\n").countTokens();
		return numLines * FONT_GLYPH_HEIGHT * scale;
	}
}
