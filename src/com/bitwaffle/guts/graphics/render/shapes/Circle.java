package com.bitwaffle.guts.graphics.render.shapes;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import android.opengl.GLES20;
import android.util.FloatMath;

import com.bitwaffle.guts.graphics.render.Render2D;
import com.bitwaffle.guts.util.BufferUtils;
import com.bitwaffle.guts.util.MathHelper;

public class Circle {
	/** What will be rendering this quad */
	private Render2D renderer;
	
	/** Buffers to hold data */
	private FloatBuffer vertBuffer, texBuffer;
	
	/** Info on coordinate */
	private static final int COORDS_PER_VERTEX = 3, COORDS_PER_TEXCOORD = 2;
	
	/** Handles to send data to when drawing */
	private int positionHandle, texCoordHandle;
	
	private int numIndices;
	
	public Circle(Render2D renderer, float step){
		this.renderer = renderer;
		
		// temporary lists
		ArrayList<float[]> verts, texCoords;
		verts = new ArrayList<float[]>();
		texCoords = new ArrayList<float[]>();
		
		// go from 0-360 by the given step (a higher step means less vertices)
		for(float angle = 0.0f; angle <= 360.0f; angle += step){
			float rad = MathHelper.toRadians(angle);
			
			float x = FloatMath.sin(rad);
			float y = FloatMath.cos(rad);
            float z = 0.0f;
            float u = x * 0.5f + 0.5f;
            float v = y * 0.5f + 0.5f;
            
            verts.add(new float[]{x, y, z});
            texCoords.add(new float[]{u, v});
		}
		
		// grab the number of vertices
		numIndices = verts.size();
		
		// fill vertex buffer
		vertBuffer = BufferUtils.getFloatBuffer(verts.size() * COORDS_PER_VERTEX);
		for(float[] flo : verts){
			vertBuffer.put(flo[0]);
			vertBuffer.put(flo[1]);
			vertBuffer.put(flo[2]);
		}
		vertBuffer.rewind();
		verts.clear();
		
		// fill texture coordinate buffer
		texBuffer = BufferUtils.getFloatBuffer(texCoords.size() * COORDS_PER_TEXCOORD);
		for(float[] flo : texCoords){
			texBuffer.put(flo[0]);
			texBuffer.put(flo[1]);
		}
		texBuffer.rewind();
		texCoords.clear();
		
		// get handles for rendering
		positionHandle = renderer.program.getAttribLocation("vPosition");
		texCoordHandle = renderer.program.getAttribLocation("vTexCoord");
	}
	
	public void render(float radius, boolean flipHorizontal, boolean flipVertical){
		// set position info
		GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, vertBuffer);
        
        // set texture coordinate info
        GLES20.glEnableVertexAttribArray(texCoordHandle);
        GLES20.glVertexAttribPointer(texCoordHandle, COORDS_PER_TEXCOORD, GLES20.GL_FLOAT, false, 0, texBuffer);
        
        // scale matrix to match radius and flip if needed
        Matrix4f.scale(new Vector3f(radius, radius, 1.0f), renderer.modelview, renderer.modelview);
        if(flipHorizontal)
        	renderer.modelview.rotate(MathHelper.PI, new Vector3f(0.0f, 1.0f, 0.0f));
        if(flipVertical)
        	renderer.modelview.rotate(MathHelper.PI, new Vector3f(0.0f, 0.0f, 1.0f));
        renderer.sendModelViewToShader();

        // actually draw the quad
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, numIndices);
        
        // don't forget to disable the attrib arrays!
        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(texCoordHandle);
	}
}
