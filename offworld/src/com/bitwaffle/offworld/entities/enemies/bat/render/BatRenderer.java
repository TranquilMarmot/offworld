package com.bitwaffle.offworld.entities.enemies.bat.render;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.ai.path.Node;
import com.bitwaffle.guts.entity.Entity;
import com.bitwaffle.guts.graphics.render.Renderer;
import com.bitwaffle.guts.graphics.render.render2d.EntityRenderer;
import com.bitwaffle.offworld.entities.enemies.bat.Bat;

public class BatRenderer implements EntityRenderer {

	@Override
	public void render(Renderer renderer, Entity ent, boolean renderDebug) {
		Bat bat = (Bat) ent;
		
		if(renderDebug)
			renderDebug(renderer, bat);
		else{
			renderer.r2D.setColor(1.0f, 1.0f, 1.0f, 1.0f);
			Gdx.gl20.glEnable(GL20.GL_BLEND);
			Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			
			if(bat.isSleeping())
				bat.getSleepAnimation().renderCurrentFrame(renderer);
			else
				bat.getFlyAnimation().renderCurrentFrame(renderer, false, false);
			
			Gdx.gl20.glDisable(GL20.GL_BLEND);
		}
	}
	
	private void renderDebug(Renderer renderer, Bat bat){
		renderer.r2D.setColor(0.0f, 1.0f, 1.0f, 0.5f);
		
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Game.resources.textures.bindTexture("blank");
		renderer.r2D.quad.render(bat.getWidth(), bat.getHeight());
		
		// FIXME TEMP
		Game.out.println(bat.pathfinder.getOpenset().size());
		
		renderer.r2D.setColor(1.0f, 0.0f, 0.0f, 0.75f);
		Iterator<Node> it = bat.pathfinder.getOpenset().iterator();
		while(it.hasNext()){
			Vector2 point = it.next().loc();
			renderer.modelview.idt();
			renderer.r2D.translateModelViewToCamera();
			renderer.modelview.translate(point.x, point.y, 0.0f);
			renderer.r2D.sendMatrixToShader();
			renderer.r2D.quad.render(0.25f, 0.25f);
		}
		
		renderer.r2D.setColor(0.75f, 0.5f, 0.0f, 0.75f);
		Iterator<Node> it2 = bat.pathfinder.getClosedSet().iterator();
		while(it2.hasNext()){
			Vector2 point = it2.next().loc();
			renderer.modelview.idt();
			renderer.r2D.translateModelViewToCamera();
			renderer.modelview.translate(point.x, point.y, 0.0f);
			renderer.r2D.sendMatrixToShader();
			renderer.r2D.quad.render(0.25f, 0.25f);
		}
		
		renderer.r2D.setColor(0.0f, 1.0f, 0.0f, 0.8f);
		Node current = bat.pathfinder.getCurrent();
		Vector2 point = current.loc();
		renderer.modelview.idt();
		renderer.r2D.translateModelViewToCamera();
		renderer.modelview.translate(point.x, point.y, 0.0f);
		renderer.r2D.sendMatrixToShader();
		renderer.r2D.quad.render(0.25f, 0.25f);
		
		
		Gdx.gl20.glDisable(GL20.GL_BLEND);
	}

}
