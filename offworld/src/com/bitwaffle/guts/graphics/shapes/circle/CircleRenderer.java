package com.bitwaffle.guts.graphics.shapes.circle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entity.Entity;
import com.bitwaffle.guts.entity.EntityRenderer;
import com.bitwaffle.guts.entity.dynamic.CircleEntity;
import com.bitwaffle.guts.graphics.render.Renderer;

/**
 * Renders an {@link CircleEntity}
 * 
 * @author TranquilMarmot
 */
public class CircleRenderer implements EntityRenderer {
	/** Color to render at, rgba */
	private float[] color;
	
	/** Name of texture to bind */
	private String textureName;
	
	public CircleRenderer(String textureName, float[] color){
		this.textureName = textureName;
		this.color = color;
	}
	
	public void render(Renderer renderer, Entity ent, boolean renderDebug) {
		CircleEntity circ = (CircleEntity) ent;
		
		if(renderDebug)
			renderDebug(renderer, ent);
		else{
			renderer.r2D.setColor(color);
			Game.resources.textures.bindTexture(textureName);
			renderer.r2D.circle.render(circ.getRadius(), false, false);
		}
	}

	public void renderDebug(Renderer renderer, Entity ent) {
		CircleEntity circ = (CircleEntity) ent;
		Game.resources.textures.bindTexture("blank");
		
		float[] col = new float[4];
		col[0] = (circ.body != null) ? (circ.body.isAwake() ? 0.0f : 1.0f) : 0.0f;
		col[1] = (circ.body != null) ? (circ.body.isAwake() ? 1.0f : 0.0f) : 0.0f;
		col[2] = 0.0f;
		col[3] = 0.2f;
		
		renderer.r2D.setColor(col);
		
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_DST_COLOR);
		renderer.r2D.circle.render(circ.getRadius(), false, false);
		Gdx.gl20.glDisable(GL20.GL_BLEND);
	}
}
