package com.bitwaffle.offworld.renderers;

import org.lwjgl.util.vector.Vector3f;

import android.opengl.GLES20;

import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.entities.Entity;
import com.bitwaffle.guts.graphics.render.EntityRenderer;
import com.bitwaffle.guts.graphics.render.Render2D;
import com.bitwaffle.offworld.entities.dynamic.Bullet;

public class BulletRenderer implements EntityRenderer {
	@Override
	public void render(Render2D renderer, Entity ent, boolean renderDebug) {
		//Bullet bullet = (Bullet) ent;
		renderer.program.setUniform("vColor", 1.0f, 1.0f, 1.0f, 1.0f);
		renderer.modelview.scale(new Vector3f(Bullet.SCALE, Bullet.SCALE, 1.0f));
		renderer.sendModelViewToShader();
		Game.resources.textures.getSubImage("pistolbullet").render(renderer.quad, 1.0f, 0.379f);
		
		if(renderDebug)
			renderDebug(renderer, ent);
	}

	public void renderDebug(Render2D renderer, Entity ent){
		renderer.prepareToRenderEntity(ent);
		Bullet box = (Bullet) ent;
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
