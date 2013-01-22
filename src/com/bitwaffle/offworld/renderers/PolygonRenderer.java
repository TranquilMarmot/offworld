package com.bitwaffle.offworld.renderers;

import java.nio.FloatBuffer;

import org.lwjgl.util.vector.Vector3f;

import android.opengl.GLES20;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.Entity;
import com.bitwaffle.guts.graphics.render.EntityRenderer;
import com.bitwaffle.guts.graphics.render.Render2D;
import com.bitwaffle.guts.polygon.Polygon;

public class PolygonRenderer implements EntityRenderer {
	/** Polygon that this renderer is rendering */
	private Polygon poly;
	
	/** Color to use to render polygon */
	private float[] color;
	
	/** Handles for where to send info when drawing - must be [r,g,b,a] */
	private Integer positionHandle, texCoordHandle;
	
	/** Scale to render polygon at */
	private float scale;
	
	/** Info on coordinates */
	private static final int COORDS_PER_VERTEX = 3, COORDS_PER_TEXCOORD = 2;
	
	public PolygonRenderer(Polygon poly){
		this(poly, 1.0f, new float[]{1.0f, 1.0f, 1.0f, 1.0f});
	}
	
	public PolygonRenderer(Polygon poly, float[] color){
		this(poly, 1.0f, color);
	}
	
	public PolygonRenderer(Polygon poly, float scale, float[] color){
		this.poly = poly;
		this.color = color;
		this.scale = scale;
	}
	
	
	@Override
	public void render(Render2D renderer, Entity ent, boolean renderDebug) {
		if(renderDebug)
			renderDebug(renderer, ent);
		else{
			if(positionHandle == null)
				positionHandle = renderer.program.getAttribLocation("vPosition");
			if(texCoordHandle == null)
				texCoordHandle = renderer.program.getAttribLocation("vTexCoord");
			
			renderer.program.setUniform("vColor", color[0], color[1], color[2], color[3]);
			
			//GLES20.glEnable(GLES20.GL_BLEND);
			//GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE);
	        
	        // only scale if necessary
	        if(scale != 1.0f){
		        renderer.modelview.scale(new Vector3f(scale, scale, 1.0f));
		        renderer.sendModelViewToShader();
	        }
	        
			GLES20.glEnableVertexAttribArray(positionHandle);
	        GLES20.glEnableVertexAttribArray(texCoordHandle);
			
			for(int i = 0; i < poly.getNumRenderParts(); i++){
				// bind texture
				Game.resources.textures.bindTexture(poly.getTextureName(i));
				
				// set position info
				GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, poly.getVertexBuffer(i));
		        
		        // set texture coordinate info
		        GLES20.glVertexAttribPointer(texCoordHandle, COORDS_PER_TEXCOORD, GLES20.GL_FLOAT, false, 0, poly.getTexCoordBuffer(i));
		        
		        // actually draw the polygon
		        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, poly.getNumIndices(i));
			}
			
	        // don't forget to disable the attrib arrays!
	        GLES20.glDisableVertexAttribArray(positionHandle);
	        GLES20.glDisableVertexAttribArray(texCoordHandle);
			
	        //GLES20.glDisable(GLES20.GL_BLEND);
		}
	}
	
	private void renderDebug(Render2D renderer, Entity ent){
		FloatBuffer debugVertBuffer = poly.getDebugVertBuffer(), debugTexCoordBuffer = poly.getDebugTexCoordBuffer();
		if(debugVertBuffer != null && debugTexCoordBuffer != null){
			renderer.program.setUniform("vColor", 0.0f, 1.0f, 1.0f, 0.4f);
			
			GLES20.glEnable(GLES20.GL_BLEND);
			GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_DST_COLOR);
			
			
			if(positionHandle == null)
				positionHandle = renderer.program.getAttribLocation("vPosition");
			if(texCoordHandle == null)
				texCoordHandle = renderer.program.getAttribLocation("vTexCoord");
			
			renderer.program.setUniform("vColor", 0.0f, 1.0f, 1.0f, 0.4f);
			
	        // only scale if necessary
	        if(scale != 1.0f){
		        renderer.modelview.scale(new Vector3f(scale, scale, 1.0f));
		        renderer.sendModelViewToShader();
	        }
			
			// bind texture
			Game.resources.textures.bindTexture("blank");
			
			// set position info
			GLES20.glEnableVertexAttribArray(positionHandle);
	        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false,  0, debugVertBuffer);
	        
	        // set texture coordinate info
	        GLES20.glEnableVertexAttribArray(texCoordHandle);
	        GLES20.glVertexAttribPointer(texCoordHandle, COORDS_PER_TEXCOORD, GLES20.GL_FLOAT, false, 0, debugTexCoordBuffer);
	        
	        // actually draw the polygon
	        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, poly.getDebugVertexCount());
	        
	        // don't forget to disable the attrib arrays!
	        GLES20.glDisableVertexAttribArray(positionHandle);
	        GLES20.glDisableVertexAttribArray(texCoordHandle);
	        
	        GLES20.glDisable(GLES20.GL_BLEND);
		}
	}
}
