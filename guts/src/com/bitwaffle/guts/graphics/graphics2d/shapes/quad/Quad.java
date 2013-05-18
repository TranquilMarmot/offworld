package com.bitwaffle.guts.graphics.graphics2d.shapes.quad;

import java.nio.Buffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.BufferUtils;
import com.bitwaffle.guts.graphics.Renderer;
import com.bitwaffle.guts.graphics.graphics2d.Render2D;

/**
 * A quad for rendering. Can either draw a quad using an entire texture or can send texture coordinates
 * to only draw a part of a texture.
 * There should only be one instance of this at a time, owned by the {@link Render2D} renderer
 * that it's given in its constructor.
 * 
 * This single instance's draw method should just be called repeatedly.
 * 
 * @author TranquilMarmot
 */
public class Quad {
	/** What will be rendering this quad */
	private Renderer renderer;
	
	/** Buffers to hold data */
	private Buffer vertBuffer, defaultTexBuffer;
	
	/** Info on coordinate */
	private static final int COORDS_PER_VERTEX = 3, COORDS_PER_TEXCOORD = 2;
	
	/** Six indices because we've got 2 triangles */
	private static final int NUM_INDICES = 6;
	
	/** Used to preserve modelview between draws */
	private Matrix4 oldModelview;
	
	/** Position coordinates (quad is scaled when drawn) */
	private static float[] coords = {
		-1.0f, -1.0f, 0.0f,
		1.0f, -1.0f, 0.0f,
		1.0f, 1.0f, 0.0f,
		
		1.0f, 1.0f, 0.0f,
		-1.0f, 1.0f, 0.0f,
		-1.0f, -1.0f, 0.0f
	};
	
	/** Texture coordinates */
	private static float[] defaultTexCoords = {
		0.0f, 0.0f,
		1.0f, 0.0f,
		1.0f, 1.0f,
		
		1.0f, 1.0f,
		0.0f, 1.0f,
		0.0f, 0.0f
	};
	
	/** Create a new quad (there should only be one at a time) */
	public Quad(Renderer renderer){
		this.renderer = renderer;
		
		vertBuffer = BufferUtils.newByteBuffer(coords.length * 4);
		BufferUtils.copy(coords, vertBuffer, coords.length, 0);
		vertBuffer.rewind();

		defaultTexBuffer = BufferUtils.newByteBuffer(defaultTexCoords.length * 4);
		BufferUtils.copy(defaultTexCoords, defaultTexBuffer, defaultTexCoords.length, 0);
		defaultTexBuffer.rewind();
		
		oldModelview = new Matrix4();
	}
	
	/**
	 * @param width Width of quad, from center
	 * @param height Height of quad, from center
	 */
	public void render(float width, float height){
		render(width, height, false, false);
	}
	
	/**
	 * @param width Width of quad, from center
	 * @param height Height of quad, from center
	 */
	public void render(float width, float height, boolean flipHorizontal, boolean flipVertical){
		this.render(width, height, flipHorizontal, flipVertical, defaultTexBuffer);
        
	}
	
	/**
	 * @param width Width of quad, from center
	 * @param height Height of quad, from center
	 * @param texCoords Texture coordinates to use for rendering
	 */
	public void render(float width, float height, boolean flipHorizontal, boolean flipVertical, Buffer texCoords){
		int positionHandle = renderer.r2D.getVertexPositionHandle();
		int texCoordHandle = renderer.r2D.getTexCoordHandle();
		
		// set position info
		Gdx.gl20.glEnableVertexAttribArray(positionHandle);
        Gdx.gl20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GL20.GL_FLOAT, false, 0, vertBuffer);
        
        // set texture coordinate info
        Gdx.gl20.glEnableVertexAttribArray(texCoordHandle);
        Gdx.gl20.glVertexAttribPointer(texCoordHandle, COORDS_PER_TEXCOORD, GL20.GL_FLOAT, false, 0, texCoords);
        
        // save the modelview before changing it
        oldModelview.set(renderer.modelview);
        
        // scale matrix to match width/height and flip if needed
        renderer.modelview.scale(width, height, 1.0f);
        if(flipHorizontal)
        	renderer.modelview.rotate(0.0f, 1.0f, 0.0f, 180.0f);
        if(flipVertical)
        	renderer.modelview.rotate(0.0f, 0.0f, 1.0f,  180.0f);
        renderer.r2D.sendMatrixToShader();

        // actually draw the quad
        Gdx.gl20.glDrawArrays(GL20.GL_TRIANGLES, 0, NUM_INDICES);
        
        // don't forget to disable the attrib arrays!
        Gdx.gl20.glDisableVertexAttribArray(positionHandle);
        Gdx.gl20.glDisableVertexAttribArray(texCoordHandle);
        
        // restore the modelview
        renderer.modelview.set(oldModelview);
	}
}
