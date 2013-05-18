package com.bitwaffle.guts.graphics.shapes.circle;

import java.nio.Buffer;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.BufferUtils;
import com.bitwaffle.guts.graphics.render.Renderer;

/**
 * A circle for rendering
 * 
 * @author TranquilMarmot
 */
public class Circle {
	/** What will be rendering this circle */
	private Renderer renderer;
	
	/** Buffers to hold data */
	private Buffer vertBuffer, texBuffer;
	
	/** Info on coordinates for drawing */
	private static final int COORDS_PER_VERTEX = 3, COORDS_PER_TEXCOORD = 2;
	
	/** Number of indices in circle */
	private int numIndices;
	
	/** @param step How much to increase the angle between each vertex (lower means more vertices, must be between 0.1 and 90.0) */
	public Circle(Renderer renderer, float step){
		this.renderer = renderer;
		
		// temporary lists
		ArrayList<float[]> verts, texCoords;
		verts = new ArrayList<float[]>();
		texCoords = new ArrayList<float[]>();
		
		// go from 0-360 by the given step (a higher step means less vertices)
		for(float angle = 0.0f; angle <= 360.0f; angle += step){
			double rad = Math.toRadians((double) angle);
			
			float x = (float)Math.sin(rad);
			float y = (float)Math.cos(rad);
            float z = 0.0f;
            float u = x * 0.5f + 0.5f;
            float v = y * 0.5f + 0.5f;
            
            verts.add(new float[]{x, y, z});
            texCoords.add(new float[]{u, v});
		}
		
		// grab the number of vertices
		numIndices = verts.size();
		
		// fill vertex buffer
		float[] vertarr = new float[verts.size() * COORDS_PER_VERTEX];
		for(int i = 0; i < verts.size(); i++){
			vertarr[i * 3] = verts.get(i)[0];
			vertarr[(i * 3) + 1] = verts.get(i)[1];
			vertarr[(i * 3) + 2] = verts.get(i)[2];
		}
		vertBuffer = BufferUtils.newByteBuffer(verts.size() * COORDS_PER_VERTEX * 4);
		BufferUtils.copy(vertarr, vertBuffer, vertarr.length, 0);
		vertBuffer.rewind();
		verts.clear();
		
		// fill texture coordinate buffer
		float[] texarr = new float[texCoords.size() * COORDS_PER_VERTEX];
		for(int i = 0; i < texCoords.size(); i++){
			texarr[i * 2] = texCoords.get(i)[0];
			texarr[(i * 2) + 1] = texCoords.get(i)[1];
		}
		texBuffer = BufferUtils.newByteBuffer(texCoords.size() * COORDS_PER_TEXCOORD * 4);
		BufferUtils.copy(texarr, vertBuffer, vertarr.length, 0);
		texBuffer.rewind();
		texCoords.clear();
	}
	
	public void render(float radius, boolean flipHorizontal, boolean flipVertical){
		int positionHandle = renderer.r2D.getVertexPositionHandle();
		int texCoordHandle = renderer.r2D.getTexCoordHandle();
		
		// set position info
		Gdx.gl20.glEnableVertexAttribArray(positionHandle);
        Gdx.gl20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GL20.GL_FLOAT, false, 0, vertBuffer);
        
        // set texture coordinate info
        Gdx.gl20.glEnableVertexAttribArray(texCoordHandle);
        Gdx.gl20.glVertexAttribPointer(texCoordHandle, COORDS_PER_TEXCOORD, GL20.GL_FLOAT, false, 0, texBuffer);
        
        // scale matrix to match radius and flip if needed
        renderer.modelview.scale(radius, radius, 1.0f);
        if(flipHorizontal)
        	renderer.modelview.rotate(0.0f, 1.0f, 0.0f, 180.0f);
        if(flipVertical)
        	renderer.modelview.rotate(0.0f, 0.0f, 1.0f, 180.0f);
        renderer.r2D.sendMatrixToShader();

        // actually draw the circle
        Gdx.gl20.glDrawArrays(GL20.GL_TRIANGLE_FAN, 0, numIndices);
        
        // don't forget to disable the attrib arrays!
        Gdx.gl20.glDisableVertexAttribArray(positionHandle);
        Gdx.gl20.glDisableVertexAttribArray(texCoordHandle);
	}
}
