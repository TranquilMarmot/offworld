package com.bitwaffle.moguts.graphics.render.renderers;

import android.opengl.GLES20;

import com.bitwaffle.moguts.entities.Entity;
import com.bitwaffle.moguts.entities.dynamic.CircleEntity;
import com.bitwaffle.moguts.graphics.render.Render2D;
import com.bitwaffle.offworld.Game;

/**
 * Renders an {@link CircleEntity}
 * 
 * @author TranquilMarmot
 */
public class CircleRenderer implements EntityRenderer {
	public void render(Render2D renderer, Entity ent) {
		CircleEntity circ = (CircleEntity) ent;
		
		renderer.program.setUniform("vColor", circ.color[0], circ.color[1], circ.color[2], circ.color[3]);
		Game.resources.textures.bindTexture("box");
		renderer.circle.render(circ.getRadius(), false, false);
	}

	public void renderDebug(Render2D renderer, Entity ent) {
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
