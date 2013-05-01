package com.bitwaffle.guts.graphics.shapes.model;

import java.nio.Buffer;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.GL20;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entity.Entity;
import com.bitwaffle.guts.graphics.render.Renderer;
import com.bitwaffle.guts.graphics.render.render3d.EntityRenderer3D;

/**
 * Renders a {@link Model}
 * 
 * @author TranquilMarmot
 */
public class ModelPolygonRenderer extends EntityRenderer3D {
	/** Info on coordinates */
	private static final int COORDS_PER_VERTEX = 3, COORDS_PER_TEXCOORD = 2;
	
	/** Polygon holding physics/debug data */
	private ModelPolygon modelPoly;
	
	/** Model being rendered */
	private Model model;
	
	public ModelPolygonRenderer(ModelPolygon modelPoly){
		this.modelPoly = modelPoly;
		this.model = Game.resources.models.getModel(modelPoly.modelName());
	}
	
	public ModelPolygonRenderer(Model model){
		this.model = model;
	}
	
	@Override
	public void render(Renderer renderer, Entity ent, boolean renderDebug){
		// debug rendering data comes from model polygon
		if(modelPoly != null && renderDebug){
			renderDebug(renderer, ent);
		} else{
			int positionHandle = renderer.r3D.getVertexPositionHandle();
			int texCoordHandle = renderer.r3D.getTexCoordHandle();
			int normHandle = renderer.r3D.getVertexNormalHandle();
			
			Gdx.gl20.glEnableVertexAttribArray(positionHandle);
			Gdx.gl20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GL20.GL_FLOAT, false, 0, model.getCoordBuffer());
			
	        Gdx.gl20.glEnableVertexAttribArray(texCoordHandle);
	        Gdx.gl20.glVertexAttribPointer(texCoordHandle, COORDS_PER_TEXCOORD, GL20.GL_FLOAT, false, 0, model.getTexCoordBuffer());
	        
	        Gdx.gl20.glEnableVertexAttribArray(normHandle);
	        Gdx.gl20.glVertexAttribPointer(normHandle, COORDS_PER_VERTEX, GL20.GL_FLOAT, false, 0, model.getNormalBuffer());
			
			Game.resources.textures.bindTexture(model.getTexture());
			
			Iterator<ModelPart> it = model.partsIterator();
			while(it.hasNext()){
				ModelPart p = it.next();
				renderer.r3D.setCurrentMaterial(p.getMaterial());
				// draw them triangles
				Gdx.gl20.glDrawArrays(GL11.GL_TRIANGLES, p.startIndex(), p.count());
			}
			
			Gdx.gl20.glDisableVertexAttribArray(positionHandle);
	        Gdx.gl20.glDisableVertexAttribArray(texCoordHandle);
	        Gdx.gl20.glDisableVertexAttribArray(normHandle);
		}
	}


	private void renderDebug(Renderer renderer, Entity ent){
		Buffer debugVertBuffer = modelPoly.getDebugVertBuffer(), debugTexCoordBuffer = modelPoly.getDebugTexCoordBuffer();
		if(debugVertBuffer != null && debugTexCoordBuffer != null){
			renderer.r2D.switchTo2DWorldCoords();
			renderer.r2D.prepareToRenderEntity(ent);
			int positionHandle = renderer.r2D.getVertexPositionHandle();
			int texCoordHandle = renderer.r2D.getTexCoordHandle();
			
			Gdx.gl20.glEnable(GL20.GL_BLEND);
			Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_DST_COLOR);
			
			renderer.r2D.setColor(0.0f, 1.0f, 1.0f, 0.4f);
			
			// bind blank texture
			Game.resources.textures.bindTexture("blank");
			
			// set position info
			Gdx.gl20.glEnableVertexAttribArray(positionHandle);
	        Gdx.gl20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GL20.GL_FLOAT, false,  0, debugVertBuffer);
	        
	        // set texture coordinate info
	        Gdx.gl20.glEnableVertexAttribArray(texCoordHandle);
	        Gdx.gl20.glVertexAttribPointer(texCoordHandle, COORDS_PER_TEXCOORD, GL20.GL_FLOAT, false, 0, debugTexCoordBuffer);
	        
	        // actually draw the polygon
	        Gdx.gl20.glDrawArrays(GL20.GL_TRIANGLES, 0, modelPoly.getDebugVertexCount());
	        
	        // don't forget to disable the attrib arrays!
	        Gdx.gl20.glDisableVertexAttribArray(positionHandle);
	        Gdx.gl20.glDisableVertexAttribArray(texCoordHandle);
	        
	        Gdx.gl20.glDisable(GL20.GL_BLEND);
		}
	}
}
