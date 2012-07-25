package com.bitwaffle.moguts.graphics.shapes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.bitwaffle.moguts.graphics.render.Render2D;

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
	/** Buffers to hold data */
	private FloatBuffer vertBuffer, texBuffer;
	
	/** Info on coordinate */
	private static final int COORDS_PER_VERTEX = 3, COORDS_PER_TEXCOORD = 2;
	
	/**
	 * Position coordinates (quad is scaled when drawn)
	 */
	private static float[] coords = {
		-0.5f, 0.5f, 0.0f,
		0.5f, 0.5f, 0.0f,
		0.5f, -0.5f, 0.0f,
		
		-0.5f, 0.5f, 0.0f,
		-0.5f, -0.5f, 0.0f,
		0.5f, -0.5f, 0.0f
	};
	
	/**
	 * Texture coordinates
	 */
	private static float[] texCoords = {
		0.0f, 1.0f,
		1.0f, 1.0f,
		1.0f, 0.0f,
		
		0.0f, 1.0f,
		0.0f, 0.0f,
		1.0f, 0.0f
	};
	
	/** Handles to send data to when drawing */
	private int positionHandle, texCoordHandle;
	
	/**
	 * Create a new quad (there should only be one at a time)
	 * @param renderer What will be rendering this quad
	 */
	public Quad(Render2D renderer){		
		// create vertex buffer (4 bytes per float)
		ByteBuffer vertbb = ByteBuffer.allocateDirect(coords.length * 4);
		vertbb.order(ByteOrder.nativeOrder());
		
		vertBuffer = vertbb.asFloatBuffer();
		vertBuffer.put(coords);
		vertBuffer.rewind();
		
		positionHandle = renderer.program.getAttribLocation("vPosition");
		
		// create texture buffer (4 bytes per float)
		ByteBuffer texbb = ByteBuffer.allocateDirect(texCoords.length * 4);
		texbb.order(ByteOrder.nativeOrder());
		
		texBuffer = texbb.asFloatBuffer();
		texBuffer.put(texCoords);
		texBuffer.rewind();
		
		texCoordHandle = renderer.program.getAttribLocation("vTexCoord");
	}
	
	/**
	 * Draw the quad
	 * @param renderer Renderer to use to draw quad (need to know to scale matrices)
	 * @param width Width of quad, from center
	 * @param height Height of quad, from center
	 */
	public void draw(Render2D renderer, float width, float height){
		// set position info
		GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, vertBuffer);
        
        // set texture coordinate info
        GLES20.glEnableVertexAttribArray(texCoordHandle);
        GLES20.glVertexAttribPointer(texCoordHandle, COORDS_PER_TEXCOORD, GLES20.GL_FLOAT, false, 0, texBuffer);
        
        // scale matrix to match width/height
        Matrix.scaleM(renderer.modelview, 0, width * 2.0f, height * 2.0f, 1.0f);
        renderer.program.setUniformMatrix4f("ModelView", renderer.modelview);

        // actually draw the quad
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
	}
}
