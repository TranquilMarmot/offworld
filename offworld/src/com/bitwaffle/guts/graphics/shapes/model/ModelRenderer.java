package com.bitwaffle.guts.graphics.shapes.model;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.GL20;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entity.Entity;
import com.bitwaffle.guts.entity.EntityRenderer3D;
import com.bitwaffle.guts.graphics.render.Renderer;

/**
 * Renders a {@link Model}
 * 
 * @author TranquilMarmot
 */
public class ModelRenderer extends EntityRenderer3D {
	/** Info on coordinates */
	private static final int COORDS_PER_VERTEX = 3, COORDS_PER_TEXCOORD = 2;
	
	/** Model this renderer renders */
	private Model model;
	
	public ModelRenderer(Model model){
		this.model = model;
	}
	
	@Override
	public void render(Renderer renderer, Entity ent, boolean renderDebug){
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
