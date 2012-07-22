package com.bitwaffle.moguts.graphics.shapes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;

import com.bitwaffle.moguts.Game;

public class Quad {
	private FloatBuffer vertBuffer, texBuffer;
	
	static final int COORDS_PER_VERTEX = 3;
	/*
	private static float[] coords = {
		0.5f, 0.5f, 0.0f, // top right
		-0.5f, 0.5f, 0.0f, // top left
		-0.5f, -0.5f, 0.0f, // bottom left
		0.5f, -0.5f, 0.0f // top right
	};
	*/
	
	/*
	private static float[] coords = {
		-0.5f, 0.5f, 0.0f,
		0.5f, 0.5f, 0.0f,
		0.5f, -0.5f, 0.0f,
		
		-0.5f, 0.5f, 0.0f,
		-0.5f, -0.5f, 0.0f,
		0.5f, -0.5f, 0.0f
	};
	*/
	
	private static float[] texCoords = {
		0.0f, 0.0f,
		1.0f, 0.0f,
		1.0f, 1.0f,
		
		0.0f, 0.0f,
		0.0f, 1.0f,
		1.0f, 1.0f
	};
	
	
	private int positionHandle, texCoordHandle;
	
	public Quad(float width, float height){
		// FIXME this should really just scale the matrix to it's width/height, not change the actual coords!
		
		//width /= 2.0f;
		//height /= 2.0f;
		
		//float width = width / 2.0f;
		//float height = height / 2.0f;
		
		
		float[] coords = {
				-width, height, 0.0f,
				width, height, 0.0f,
				width, -height, 0.0f,
				
				-width, height, 0.0f,
				-width, -height, 0.0f,
				width, -height, 0.0f
		};
		
		// 4 bytes per float!
		ByteBuffer bb = ByteBuffer.allocateDirect(coords.length * 4);
		bb.order(ByteOrder.nativeOrder());
		
		vertBuffer = bb.asFloatBuffer();
		vertBuffer.put(coords);
		vertBuffer.rewind();
		
		positionHandle = Game.render2D.program.getAttribLocation("vPosition");
		
		ByteBuffer bb2 = ByteBuffer.allocateDirect(texCoords.length * 4);
		bb2.order(ByteOrder.nativeOrder());
		
		texBuffer = bb2.asFloatBuffer();
		texBuffer.put(texCoords);
		texBuffer.rewind();
		
		texCoordHandle = Game.render2D.program.getAttribLocation("vTexCoord");
	}
	
	public void draw(){
		GLES20.glEnableVertexAttribArray(positionHandle);
		GLES20.glEnableVertexAttribArray(texCoordHandle);
		
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                0, vertBuffer);
        GLES20.glVertexAttribPointer(texCoordHandle, 2, GLES20.GL_FLOAT, false, 0, texBuffer);
        
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
	}
}
