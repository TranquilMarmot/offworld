package com.bitwaffle.guts.graphics.render.shapes;

import java.nio.FloatBuffer;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import android.opengl.GLES20;

import com.bitwaffle.guts.graphics.render.Render2D;
import com.bitwaffle.guts.util.BufferUtils;
import com.bitwaffle.guts.util.MathHelper;

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
	
	/** Six indices because we've got 2 triangles */
	private static final int NUM_INDICES = 6;
	
	/** Used to preserve modelview between draws */
	private Matrix4f oldModelview;
	
	/**
	 * Position coordinates (quad is scaled when drawn)
	 */
	private static float[] coords = {
		-1.0f, -1.0f, 0.0f,
		1.0f, -1.0f, 0.0f,
		1.0f, 1.0f, 0.0f,
		
		1.0f, 1.0f, 0.0f,
		-1.0f, 1.0f, 0.0f,
		-1.0f, -1.0f, 0.0f
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

		defaultTexBuffer = BufferUtils.getFloatBuffer(defaultTexCoords.length);
		defaultTexBuffer.put(defaultTexCoords);
		defaultTexBuffer.rewind();
		
		positionHandle = renderer.program.getAttribLocation("vPosition");
		texCoordHandle = renderer.program.getAttribLocation("vTexCoord");
		
		oldModelview = new Matrix4f();
	}
	
	/**
	 * Draw a quad
	 * @param width Width of quad, from center
	 * @param height Height of quad, from center
	 */
	public void render(float width, float height){
		render(width, height, false, false);
	}
	
	/**
	 * Draw a quad, with optional flipping
	 * @param width Width of quad, from center
	 * @param height Height of quad, from center
	 * @param flipHorizontal Whether or not to flip the image horizontally
	 * @param flipVertical Whether or not to flip the image vertically
	 */
	public void render(float width, float height, boolean flipHorizontal, boolean flipVertical){
		this.render(width, height, flipHorizontal, flipVertical, defaultTexBuffer);
        
	}
	
	/**
	 * Draw a quad, with optional flipping
	 * @param width Width of quad, from center
	 * @param height Height of quad, from center
	 * @param flipHorizontal Whether or not to flip the image horizontally
	 * @param flipVertical Whether or not to flip the image vertically
	 */
	public void render(float width, float height, boolean flipHorizontal, boolean flipVertical, FloatBuffer texCoords){
		// set position info
		GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, vertBuffer);
        
        // set texture coordinate info
        GLES20.glEnableVertexAttribArray(texCoordHandle);
        GLES20.glVertexAttribPointer(texCoordHandle, COORDS_PER_TEXCOORD, GLES20.GL_FLOAT, false, 0, texCoords);
        
        // save the modelview before changing it
        oldModelview.load(renderer.modelview);
        
        // scale matrix to match width/height and flip if needed
        renderer.modelview.scale(new Vector3f(width, height, 1.0f));
        if(flipHorizontal)
        	renderer.modelview.rotate(MathHelper.PI, new Vector3f(0.0f, 1.0f, 0.0f));
        if(flipVertical)
        	renderer.modelview.rotate(MathHelper.PI, new Vector3f(0.0f, 0.0f, 1.0f));
        renderer.sendModelViewToShader();
        //
        // actually draw the quad
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, NUM_INDICES);
        
        // don't forget to disable the attrib arrays!
        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(texCoordHandle);
        
        // restore the modelview
        renderer.modelview.load(oldModelview);
	}
}
