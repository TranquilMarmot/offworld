package com.bitwaffle.guts.graphics.render.shapes;

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
	
	private float[] color;
	
	private String textureName;
	
	public CircleRenderer(String textureName, float[] color){
		this.textureName = textureName;
		this.color = color;
	}
	
	public void render(Render2D renderer, Entity ent, boolean renderDebug) {
		CircleEntity circ = (CircleEntity) ent;
		
		if(renderDebug)
			renderDebug(renderer, ent);
		else{
			renderer.program.setUniform("vColor", color[0], color[1], color[2], color[3]);
			Game.resources.textures.bindTexture(textureName);
			renderer.circle.render(circ.getRadius(), false, false);
		}
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
