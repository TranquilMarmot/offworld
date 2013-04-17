package com.bitwaffle.guts.graphics.shapes.polygon;

import java.nio.Buffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.Entity;
import com.bitwaffle.guts.graphics.EntityRenderer;
import com.bitwaffle.guts.graphics.Render2D;

/**
 * Renders a {@link Polygon}
 * 
 * @author TranquilMarmot
 */
public class PolygonRenderer implements EntityRenderer {
	/** Polygon that this renderer is rendering */
	private Polygon poly;
	
	/** Color to use to render polygon, 4 floats, rgba */
	private float[] color;
	
	/** Handles for where to send info when drawing */
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
		// grab handles if we don't have them
		if(positionHandle == null)
			positionHandle = renderer.program.getAttribLocation("vPosition");
		if(texCoordHandle == null)
			texCoordHandle = renderer.program.getAttribLocation("vTexCoord");
		
		if(renderDebug)
			renderDebug(renderer, ent);
		else{
			renderer.program.setUniform("vColor", color[0], color[1], color[2], color[3]);
			
			Gdx.gl20.glEnable(GL20.GL_BLEND);
			Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	        
	        // only scale if necessary
	        if(scale != 1.0f){
		        renderer.modelview.scale(scale, scale, 1.0f);
		        renderer.sendMatrixToShader();
	        }
	        
			Gdx.gl20.glEnableVertexAttribArray(positionHandle);
	        Gdx.gl20.glEnableVertexAttribArray(texCoordHandle);
			
			for(int i = 0; i < poly.getNumRenderParts(); i++){
				// bind texture
				Game.resources.textures.bindTexture(poly.getTextureName(i));
				
				// set position info
				Gdx.gl20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GL20.GL_FLOAT, false, 0, poly.getVertexBuffer(i));
		        
		        // set texture coordinate info
		        Gdx.gl20.glVertexAttribPointer(texCoordHandle, COORDS_PER_TEXCOORD, GL20.GL_FLOAT, false, 0, poly.getTexCoordBuffer(i));
		        
		        // actually draw the polygon
		        Gdx.gl20.glDrawArrays(GL20.GL_TRIANGLES, 0, poly.getNumIndices(i));
			}
			
	        // don't forget to disable the attrib arrays!
	        Gdx.gl20.glDisableVertexAttribArray(positionHandle);
	        Gdx.gl20.glDisableVertexAttribArray(texCoordHandle);
			
	        Gdx.gl20.glDisable(GL20.GL_BLEND);
		}
	}
	
	private void renderDebug(Render2D renderer, Entity ent){
		Buffer debugVertBuffer = poly.getDebugVertBuffer(), debugTexCoordBuffer = poly.getDebugTexCoordBuffer();
		if(debugVertBuffer != null && debugTexCoordBuffer != null){
			Gdx.gl20.glEnable(GL20.GL_BLEND);
			Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_DST_COLOR);
			
			renderer.program.setUniform("vColor", 0.0f, 1.0f, 1.0f, 0.4f);
			
	        // only scale if necessary
	        if(scale != 1.0f){
		        renderer.modelview.scale(scale, scale, 1.0f);
		        renderer.sendMatrixToShader();
	        }
			
			// bind blank texture
			Game.resources.textures.bindTexture("blank");
			
			// set position info
			Gdx.gl20.glEnableVertexAttribArray(positionHandle);
	        Gdx.gl20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GL20.GL_FLOAT, false,  0, debugVertBuffer);
	        
	        // set texture coordinate info
	        Gdx.gl20.glEnableVertexAttribArray(texCoordHandle);
	        Gdx.gl20.glVertexAttribPointer(texCoordHandle, COORDS_PER_TEXCOORD, GL20.GL_FLOAT, false, 0, debugTexCoordBuffer);
	        
	        // actually draw the polygon
	        Gdx.gl20.glDrawArrays(GL20.GL_TRIANGLES, 0, poly.getDebugVertexCount());
	        
	        // don't forget to disable the attrib arrays!
	        Gdx.gl20.glDisableVertexAttribArray(positionHandle);
	        Gdx.gl20.glDisableVertexAttribArray(texCoordHandle);
	        
	        Gdx.gl20.glDisable(GL20.GL_BLEND);
		}
	}
}
