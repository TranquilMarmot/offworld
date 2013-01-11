package com.bitwaffle.offworld.renderers;

import android.opengl.GLES20;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.Entity;
import com.bitwaffle.guts.entities.dynamic.CircleEntity;
import com.bitwaffle.guts.graphics.render.EntityRenderer;
import com.bitwaffle.guts.graphics.render.Render2D;

/**
 * Renders an {@link CircleEntity}
 * 
 * @author TranquilMarmot
 */
public class CircleRenderer implements EntityRenderer {
	public void render(Render2D renderer, Entity ent, boolean renderDebug) {
		CircleEntity circ = (CircleEntity) ent;
		
		renderer.program.setUniform("vColor", circ.color[0], circ.color[1], circ.color[2], circ.color[3]);
		Game.resources.textures.bindTexture("box");
		renderer.circle.render(circ.getRadius(), false, false);
		
		if(renderDebug)
			renderDebug(renderer, ent);
	}

	public void renderDebug(Render2D renderer, Entity ent) {
		renderer.prepareToRenderEntity(ent);
		CircleEntity circ = (CircleEntity) ent;
		Game.resources.textures.bindTexture("blank");
		
		float[] col = new float[4];
		col[0] = (circ.body != null) ? (circ.body.isAwake() ? 0.0f : 1.0f) : 0.0f;
		col[1] = (circ.body != null) ? (circ.body.isAwake() ? 1.0f : 0.0f) : 0.0f;
		col[2] = 0.0f;
		col[3] = 0.2f;
		
		renderer.program.setUniform("vColor", col[0], col[1], col[2], col[3]);
		
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_DST_COLOR);
		renderer.circle.render(circ.getRadius(), false, false);
		GLES20.glDisable(GLES20.GL_BLEND);
	}
}
