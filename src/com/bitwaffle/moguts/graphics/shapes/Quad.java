package com.bitwaffle.moguts.graphics.shapes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;

import com.bitwaffle.moguts.graphics.render.GLRenderer;

public class Quad {
	private FloatBuffer vertBuffer;
	
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
	
	
	private int positionHandle;
	
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
		
		positionHandle = GLRenderer.render2D.program.getAttribLocation("vPosition");
	}
	
	public void draw(){
		GLES20.glEnableVertexAttribArray(positionHandle);
		
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                0, vertBuffer);
        
        //Random randy = new Random();
        //float[] color = { randy.nextFloat(), randy.nextFloat(), randy.nextFloat(), 1.0f };
        
       // GLRenderer.render2D.program.setUniform("vColor", color[0], color[1], color[2], color[3]);
        
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
	}
}
