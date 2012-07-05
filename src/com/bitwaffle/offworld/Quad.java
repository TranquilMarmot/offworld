package com.bitwaffle.offworld;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;

import android.opengl.GLES20;

import com.bitwaffle.offworld.moguts.graphics.render.GLRenderer;

public class Quad {
	private FloatBuffer vertBuffer;
	
	public float[] mat;
	
	static final int COORDS_PER_VERTEX = 3;
	/*
	private static float[] coords = {
		0.5f, 0.5f, 0.0f, // top right
		-0.5f, 0.5f, 0.0f, // top left
		-0.5f, -0.5f, 0.0f, // bottom left
		0.5f, -0.5f, 0.0f // top right
	};
	*/
	
	private static float[] coords = {
		-0.5f, 0.5f, 0.0f,
		0.5f, 0.5f, 0.0f,
		0.5f, -0.5f, 0.0f,
		
		-0.5f, 0.5f, 0.0f,
		-0.5f, -0.5f, 0.0f,
		0.5f, -0.5f, 0.0f
	};
	
	private int positionHandle;
	
	public Quad(){
		// 4 bytes per float!
		ByteBuffer bb = ByteBuffer.allocateDirect(coords.length * 4);
		bb.order(ByteOrder.nativeOrder());
		
		vertBuffer = bb.asFloatBuffer();
		vertBuffer.put(coords);
		vertBuffer.rewind();
		
		positionHandle = GLRenderer.render2D.program.getAttribLocation("vPosition");
		
		mat = new float[16];
	}
	
	public void draw(){
		GLES20.glEnableVertexAttribArray(positionHandle);
		
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                0, vertBuffer);
        
        Random randy = new Random();
        float[] color = { randy.nextFloat(), randy.nextFloat(), randy.nextFloat(), 1.0f };
        
        GLRenderer.render2D.program.setUniform("vColor", color[0], color[1], color[2], color[3]);
        
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
	}
}
