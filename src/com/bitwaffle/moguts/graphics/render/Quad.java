package com.bitwaffle.moguts.graphics.render;

import java.nio.FloatBuffer;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.bitwaffle.moguts.util.BufferUtils;

/**
 * When initialized, this creates a single quad and sends all the appropriate data to OpenGL.
 * There should really only be instance of this at a time, owned by the {@link Render2D} renderer
 * that it's given in its constructor.
 * 
 * This single instance's draw method should just be called repeatedly.
 * 
 * @author TranquilMarmot
 */
public class Quad {
	/** What will be rendering this quad */
	private Render2D renderer;
	
	/** Buffers to hold data */
	private FloatBuffer vertBuffer, defaultTexBuffer;
	
	/** Info on coordinate */
	private static final int COORDS_PER_VERTEX = 3, COORDS_PER_TEXCOORD = 2;
	
	/**
	 * Position coordinates (quad is scaled when drawn)
	 */
	private static float[] coords = {
		-0.5f, -0.5f, 0.0f,
		0.5f, -0.5f, 0.0f,
		0.5f, 0.5f, 0.0f,
		
		0.5f, 0.5f, 0.0f,
		-0.5f, 0.5f, 0.0f,
		-0.5f, -0.5f, 0.0f
	};
	
	/**
	 * Texture coordinates
	 */
	private static float[] defaultTexCoords = {
		0.0f, 0.0f,
		1.0f, 0.0f,
		1.0f, 1.0f,
		
		1.0f, 1.0f,
		0.0f, 1.0f,
		0.0f, 0.0f
	};
	
	/** Handles to send data to when drawing */
	private int positionHandle, texCoordHandle;
	
	/**
	 * Create a new quad (there should only be one at a time)
	 * @param renderer What will be rendering this quad
	 */
	public Quad(Render2D renderer){
		this.renderer = renderer;
		
		vertBuffer = BufferUtils.getFloatBuffer(coords.length);
		vertBuffer.put(coords);
		vertBuffer.rewind();
		
		positionHandle = renderer.program.getAttribLocation("vPosition");

		defaultTexBuffer = BufferUtils.getFloatBuffer(defaultTexCoords.length);
		defaultTexBuffer.put(defaultTexCoords);
		defaultTexBuffer.rewind();
		
		texCoordHandle = renderer.program.getAttribLocation("vTexCoord");
	}
	
	/**
	 * Draw a quad
	 * @param renderer Renderer to use to draw quad (need to know to scale matrices)
	 * @param width Width of quad, from center
	 * @param height Height of quad, from center
	 */
	public void draw(float width, float height){
		draw(width, height, false, false);
	}
	
	/**
	 * Draw a quad, with optional flipping
	 * @param renderer Renderer to use to draw quad (need to know to scale matrices)
	 * @param width Width of quad, from center
	 * @param height Height of quad, from center
	 * @param flipHorizontal Whether or not to flip the image horizontally
	 * @param flipVertical Whether or not to flip the image vertically
	 */
	public void draw(float width, float height, boolean flipHorizontal, boolean flipVertical){
		this.draw(width, height, flipHorizontal, flipVertical, defaultTexBuffer);
        
	}
	
	/**
	 * Draw a quad, with optional flipping
	 * @param renderer Renderer to use to draw quad (need to know to scale matrices)
	 * @param width Width of quad, from center
	 * @param height Height of quad, from center
	 * @param flipHorizontal Whether or not to flip the image horizontally
	 * @param flipVertical Whether or not to flip the image vertically
	 */
	public void draw(float width, float height, boolean flipHorizontal, boolean flipVertical, FloatBuffer texCoords){
		// set position info
		GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, vertBuffer);
        
        // set texture coordinate info
        GLES20.glEnableVertexAttribArray(texCoordHandle);
        GLES20.glVertexAttribPointer(texCoordHandle, COORDS_PER_TEXCOORD, GLES20.GL_FLOAT, false, 0, texCoords);
        
        // scale matrix to match width/height and flip if needed
        Matrix.scaleM(renderer.modelview, 0, width * 2.0f, height * 2.0f, 1.0f);
        if(flipHorizontal)
        	Matrix.rotateM(renderer.modelview, 0, 180, 0.0f, 1.0f, 0.0f);
        if(flipVertical)
        	Matrix.rotateM(renderer.modelview, 0, 180, 0.0f, 0.0f, 1.0f);
        renderer.program.setUniformMatrix4f("ModelView", renderer.modelview);

        // actually draw the quad
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
        
        // don't forget to disable the attrib arrays!
        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(texCoordHandle);
	}
}
