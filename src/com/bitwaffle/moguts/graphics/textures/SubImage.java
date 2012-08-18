package com.bitwaffle.moguts.graphics.textures;

import java.nio.FloatBuffer;

import android.opengl.GLES20;

import com.bitwaffle.moguts.graphics.render.Quad;

public class SubImage {
	/** Handle for binding source image */
	private int sheetHandle;
	
	private FloatBuffer texCoords;
	
	public SubImage(int sheetHandle, FloatBuffer texCoords){
		this.sheetHandle = sheetHandle;
		this.texCoords = texCoords;
	}
	
	public void draw(Quad quad, float width, float height){
		this.draw(quad, width, height, false, false);
	}
	
	public void draw(Quad quad, float width, float height, boolean flipHorizontal, boolean flipVertical){
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, sheetHandle);
		quad.draw(width, height, flipHorizontal, flipVertical, texCoords);
	}

}
