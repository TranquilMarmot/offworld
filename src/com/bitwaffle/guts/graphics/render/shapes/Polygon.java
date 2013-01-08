package com.bitwaffle.guts.graphics.render.shapes;

import java.nio.FloatBuffer;

import org.lwjgl.util.vector.Vector3f;

import android.opengl.GLES20;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.graphics.render.Render2D;
import com.bitwaffle.guts.util.MathHelper;

// TODO seiralize this!!!

public class Polygon {
	/** Info on coordinates */
	private static final int COORDS_PER_VERTEX = 3, COORDS_PER_TEXCOORD = 2;
	
	/** Buffers to hold datas */
	private FloatBuffer vertBuffer, texCoordBuffer;
	
	/** Number of indices */
	private int numIndices;
	
	/** Handles for where to send info when drawing */
	private Integer positionHandle, texCoordHandle;
	
	/** Shape in physics world */
	private Vector2[] geometry;
	
	/** Name of texture to bind for drawing */
	private String textureName;
	
	/**
	 * Create a new polygon
	 * @param vertices Vertices of polygon
	 * @param texCoords Texture coordinates of polygon
	 * @param numIndices Number of indices in polygon
	 */
	public Polygon(String textureName, FloatBuffer vertices, FloatBuffer texCoords, int numIndices, float xScale, float yScale, Vector2[] geometry){
		this.textureName = textureName;
		this.vertBuffer = vertices;
		this.texCoordBuffer = texCoords;
		this.numIndices = numIndices;
		this.geometry = geometry;
	}
	
	/**
	 * Draw a quad, with optional flipping
	 * @param xScale Width of quad, from center
	 * @param yScale Height of quad, from center
	 * @param flipHorizontal Whether or not to flip the image horizontally
	 * @param flipVertical Whether or not to flip the image vertically
	 */
	public void render(Render2D renderer, boolean flipHorizontal, boolean flipVertical){
		if(positionHandle == null)
			positionHandle = renderer.program.getAttribLocation("vPosition");
		if(texCoordHandle == null)
			texCoordHandle = renderer.program.getAttribLocation("vTexCoord");
		
		// bind texture
		Game.resources.textures.bindTexture(textureName);
		
		// set position info
		GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false,  0, vertBuffer);
        
        // set texture coordinate info
        GLES20.glEnableVertexAttribArray(texCoordHandle);
        GLES20.glVertexAttribPointer(texCoordHandle, COORDS_PER_TEXCOORD, GLES20.GL_FLOAT, false, 0, texCoordBuffer);
        
        // scale matrix and flip if needed
       // renderer.modelview.scale(new Vector3f(xScale, yScale, 1.0f));
        if(flipHorizontal)
        	renderer.modelview.rotate(MathHelper.PI, new Vector3f(0.0f, 1.0f, 0.0f));
        if(flipVertical)
        	renderer.modelview.rotate(MathHelper.PI, new Vector3f(0.0f, 0.0f, 1.0f));
        renderer.sendModelViewToShader();
        
        // actually draw the polygon
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, numIndices);
        
        // don't forget to disable the attrib arrays!
        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(texCoordHandle);
	}
	
	public void renderDebug(Render2D renderer, boolean flipHorizontal, boolean flipVertical){
		if(positionHandle == null)
			positionHandle = renderer.program.getAttribLocation("vPosition");
		if(texCoordHandle == null)
			texCoordHandle = renderer.program.getAttribLocation("vTexCoord");
		
		// bind texture
		Game.resources.textures.bindTexture("blank");
		
		// set position info
		GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false,  0, vertBuffer);
        
        // set texture coordinate info
        GLES20.glEnableVertexAttribArray(texCoordHandle);
        GLES20.glVertexAttribPointer(texCoordHandle, COORDS_PER_TEXCOORD, GLES20.GL_FLOAT, false, 0, texCoordBuffer);
        
        // scale matrix and flip if needed
       // renderer.modelview.scale(new Vector3f(xScale, yScale, 1.0f));
        if(flipHorizontal)
        	renderer.modelview.rotate(MathHelper.PI, new Vector3f(0.0f, 1.0f, 0.0f));
        if(flipVertical)
        	renderer.modelview.rotate(MathHelper.PI, new Vector3f(0.0f, 0.0f, 1.0f));
        renderer.sendModelViewToShader();
        
        // actually draw the polygon
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, numIndices);
        
        // don't forget to disable the attrib arrays!
        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(texCoordHandle);
	}
	
	/**
	 * @return Shape to use for this polygon in the physics world
	 */
	public ChainShape getShapeAsChain(){
		// create a chain shape from the read vertices
		ChainShape chain = new ChainShape();
		chain.createLoop(geometry); // TODO have a setting for whether or not a shape is a loop! Also destory these!
		return chain;
	}
	
	public PolygonShape getShapeAsPolygon(){
		PolygonShape shape = new PolygonShape();
		shape.set(geometry);
		return shape;
	}
}
