package com.bitwaffle.offworld.renderers;

import android.opengl.GLES20;

import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.entities.Entity;
import com.bitwaffle.guts.entities.dynamic.BoxEntity;
import com.bitwaffle.guts.graphics.render.EntityRenderer;
import com.bitwaffle.guts.graphics.render.Render2D;

/**
 * Used for rendering wooden boxes
 * 
 * @author TranquilMarmot
 */
public class BoxRenderer implements EntityRenderer{
	public void render(Render2D renderer, Entity ent, boolean renderDebug) {
		BoxEntity box = (BoxEntity) ent;
		if(box.body != null && box.body.getFixtureList().get(0).getDensity() == 0.0f)
			Game.resources.textures.bindTexture("blank");
		else
			Game.resources.textures.bindTexture("box");
		renderer.program.setUniform("vColor", box.color[0], box.color[1], box.color[2], box.color[3]);
		renderer.quad.render(box.getWidth(), box.getHeight());
		
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
		renderer.quad.render(box.getWidth(), box.getHeight());
		GLES20.glDisable(GLES20.GL_BLEND);
	}
}