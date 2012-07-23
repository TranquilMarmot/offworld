package com.bitwaffle.moguts.resources;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

public class Textures {
	// TODO make this an enum or something (maybe do XML asset lists?)
	public static int boxTex = 0;
	public static int leftArrowTex = 0;
	public static int rightArrowTex = 0;
	public static int upArrowTex = 0;
	public static int cameraTex = 0;
	
	// FIXME all textures are loaded upside-down!
	public static int loadTexture(InputStream in) {
		// loading texture
		Bitmap bitmap = BitmapFactory.decodeStream(in);
		
		int[] handles = new int[1];
		
		GLES20.glGenTextures(1, handles, 0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, handles[0]);
		
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
		
		int error = GLES20.glGetError();
		if(error != GLES20.GL_NO_ERROR)
			Log.e("Render2D", "Error loading texture! " + GLES20.glGetString(error));
		
		bitmap.recycle();
		
		return handles[0];
	}
	
	
	// FIXME temp
	public static void initTextures(AssetManager assets){
		try {
			boxTex = loadTexture(assets.open("game/textures/box.png"));
			leftArrowTex = loadTexture(assets.open("game/textures/leftarrow.png"));
			rightArrowTex = loadTexture(assets.open("game/textures/rightarrow.png"));
			upArrowTex = loadTexture(assets.open("game/textures/uparrow.png"));
			cameraTex = loadTexture(assets.open("game/textures/camera.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
