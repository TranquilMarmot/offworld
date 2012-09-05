package com.bitwaffle.offworld.renderers;

import android.opengl.GLES20;

import com.bitwaffle.moguts.entities.Entity;
import com.bitwaffle.moguts.entities.dynamic.BoxEntity;
import com.bitwaffle.moguts.graphics.render.EntityRenderer;
import com.bitwaffle.moguts.graphics.render.Render2D;
import com.bitwaffle.offworld.Game;

/**
 * Used for rendering wooden boxes
 * 
 * @author TranquilMarmot
 */
public class BoxRenderer implements EntityRenderer{
	public void render(Render2D renderer, Entity ent, boolean renderDebug) {
		BoxEntity box = (BoxEntity) ent;
		Game.resources.textures.bindTexture("box");
		renderer.program.setUniform("vColor", box.color[0], box.color[1], box.color[2], box.color[3]);
		renderer.quad.draw(box.getWidth(), box.getHeight());
		
		if(renderDebug)
			renderDebug(renderer, ent);
	}
	
	public void renderDebug(Render2D renderer, Entity ent){
		renderer.prepareToRenderEntity(ent);
		BoxEntity box = (BoxEntity) ent;
		Game.resources.textures.bindTexture("blank");
		
		float[] col = new float[4];
		col[0] = (box.body != null) ? (box.body.isAwake() ? 0.0f : 1.0f) : 0.0f;
		col[1] =(box.body != null) ? (box.body.isAwake() ? 1.0f : 0.0f) : 0.0f;
		col[2] = 0.0f;
		col[3] = 0.2f;
		
		renderer.program.setUniform("vColor", col[0], col[1], col[2], col[3]);
		
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_DST_COLOR);
		renderer.quad.draw(box.getWidth(), box.getHeight());
		GLES20.glDisable(GLES20.GL_BLEND);
	}
}