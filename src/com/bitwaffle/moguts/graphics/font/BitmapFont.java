package com.bitwaffle.moguts.graphics.font;

import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.bitwaffle.moguts.graphics.render.Render2D;
import com.bitwaffle.moguts.resources.TextureManager;
import com.bitwaffle.moguts.util.BufferUtils;
import com.bitwaffle.offworld.Game;

/**
 * Pack my box with five dozen liquor jugs!
 * 
 * @author TranquilMarmot
 */
public class BitmapFont {
	/** 
	 * ASCII value of chars[0], so that the index in chars can be calculated
	 * Since the first 31 values in ASCII are pretty much useless to us (look it up)
	 * we simply skip them. 
	 */
	private static final int START_CHAR = 32;
	
	/** Handle for the texture for this font */
	private int texHandle;
	
	/** Array of every character for this font */
	FontChar[] chars;
	
	/** How big each cell is */
	int cellWidth, cellHeight;
	
	/** How wide each glyph actually is */
	int glyphWidth;
	
	/**
	 * Create a new font
	 * @param imagePath Path to image in assets folder
	 * @param cellWidth Width of each cell
	 * @param cellHeight Height of each cell
	 */
	public BitmapFont(String imagePath, int cellWidth, int cellHeight, int glyphWidth){
		this.cellWidth = cellWidth;
		this.cellHeight = cellHeight;
		this.glyphWidth = glyphWidth;
		try{
			// intialize texture
			InputStream in = Game.resources.openAsset(imagePath);
			Bitmap bitmap = BitmapFactory.decodeStream(in);
			texHandle = TextureManager.initTexture(bitmap, GLES20.GL_NEAREST, GLES20.GL_NEAREST);
			
			// find out how many rows and columns we have
			int numRows = bitmap.getWidth() / cellWidth;
			int numCols = bitmap.getHeight() / cellHeight;
			
			chars = new FontChar[numRows * numCols];
			
			// go through each row/column and create the character there
			for(int row = 0; row < numRows; row++){
				for(int col = 0; col < numCols; col++){
					int index = (numCols * col) + row; // thanks, data structures class, for this nifty tip!
					chars[index] = initFontChar(bitmap, row, col);
				}
			}
			
			bitmap.recycle();
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
		float texX = (float)(row * cellWidth) / (float)source.getWidth();
		float texY = (float)(col * cellHeight) / (float)source.getHeight();
		float texWidth = (float)cellWidth / (float)source.getWidth();
		float texHeight = (float)cellHeight / (float)source.getHeight();
		
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
	public void drawString(String text, Render2D renderer, int x, int y){
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
	public void drawString(String text, Render2D renderer, int x, int y, float scale){
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
	public void drawString(String text, Render2D renderer, int x, int y, float scale, float[] color){
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texHandle);
		renderer.program.setUniform("vColor", color[0], color[1], color[2], color[3]);
		
		// compensate for scale FIXME is this the right way to do this?
		x /= scale;
		y /= scale;
		
		// this gets advanced with every character drawn, reset to 0 on newlines
		int xOffset = 0;
		// in case we hit any newlines
		int lineNum = 0;
		
		// go through each character in the string
		char[] charArr = text.toCharArray();
		for(int i = 0; i < charArr.length; i++){
			// look for any carriage returns or line feeds
			if(charArr[i] == '\n' || charArr[i] == '\r'){
				lineNum++;
				xOffset = 0;
				continue;
			}
			
			// casting a char to an int returns its ASCII value
			int index = (int)charArr[i] - START_CHAR;
			
			// scale and move the modelview to get to the char's location
			Matrix.setIdentityM(renderer.modelview, 0);
			Matrix.scaleM(renderer.modelview, 0, scale, scale, 1.0f);
			Matrix.translateM(renderer.modelview, 0, x + xOffset, y + (cellHeight * lineNum), 0.0f);
			renderer.program.setUniformMatrix4f("ModelView", renderer.modelview);
			
			// draw character
			chars[index].draw(renderer, cellWidth, cellHeight);
			
			// advance to next char
			xOffset += cellWidth - glyphWidth;
		}
	}
}
