package com.bitwaffle.guts.graphics.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.EntityLayer;
import com.bitwaffle.guts.graphics.render.render2d.Render2D;
import com.bitwaffle.guts.graphics.render.render3d.Render3D;

/**
 * Handles rendering the entire scene
 * 
 * @author TranquilMarmot
 */
public class Renderer {
	public Render2D render2D;
	public Render3D render3D;
	
	public boolean renderDebug = false;
	
	public Renderer(){
		render2D = new Render2D();
		render3D = new Render3D();
	}
	
	public void renderScene(){
		// clear color and depth buffers
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		// render each layer
		for(int i = 0; i < Game.physics.numLayers(); i++){
			EntityLayer layer = Game.physics.getLayer(i);
			
			// render 2D entities for layer
			Gdx.gl20.glDisable(GL20.GL_DEPTH_TEST);
			render2D.program.use();
			render2D.setUpProjectionOrthoWorldCoords();
			render2D.renderEntities(layer.entities2D.values().iterator());
			
			// render 3D entities for layer
			Gdx.gl20.glEnable(GL20.GL_DEPTH_TEST);
			render3D.program.use();
			render3D.setUp3DRender();
			render3D.renderEntities(layer.entities3D.values().iterator());
		}
		
		Gdx.gl20.glDisable(GL20.GL_DEPTH_TEST);
		render2D.program.use();
		render2D.setUpProjectionScreenCoords();
		render2D.sendMatrixToShader();
		Game.gui.render(render2D);
	}
}
