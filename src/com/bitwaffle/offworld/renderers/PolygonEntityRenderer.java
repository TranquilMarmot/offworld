package com.bitwaffle.offworld.renderers;

import android.opengl.GLES20;

import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.entities.Entity;
import com.bitwaffle.guts.entities.dynamic.PolygonEntity;
import com.bitwaffle.guts.graphics.render.EntityRenderer;
import com.bitwaffle.guts.graphics.render.Render2D;

public class PolygonEntityRenderer implements EntityRenderer {
	@Override
	public void render(Render2D renderer, Entity ent, boolean renderDebug) {
		PolygonEntity poly = (PolygonEntity)ent;
		
		if(renderDebug)
			renderDebug(renderer, poly);
		else{
			renderer.program.setUniform("vColor", 1.0f, 1.0f, 1.0f, 1.0f);
			Game.resources.polygons.renderPolygon(renderer, poly.getPolygonName());
		}
	}
	
	private void renderDebug(Render2D renderer, PolygonEntity poly){
		renderer.program.setUniform("vColor", 0.0f, 1.0f, 1.0f, 0.4f);
		
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_DST_COLOR);
		Game.resources.polygons.renderPolygonDebug(renderer, poly.getPolygonName());
		GLES20.glDisable(GLES20.GL_BLEND);
	}
}
