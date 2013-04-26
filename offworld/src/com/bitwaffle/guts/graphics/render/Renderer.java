package com.bitwaffle.guts.graphics.render;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Matrix4;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entity.Entity;
import com.bitwaffle.guts.entity.EntityRenderer3D;
import com.bitwaffle.guts.graphics.render.render2d.Render2D;
import com.bitwaffle.guts.graphics.render.render3d.Render3D;

/**
 * Handles rendering the entire scene
 * 
 * @author TranquilMarmot
 */
public class Renderer {
	/** Matrices for use with shaders */
	public Matrix4 modelview, projection;
	
	/** 2D Renderer */
	public Render2D r2D;
	
	/** 2D Renderer */
	public Render3D r3D;
	
	/** Whether or not debug is being rendered */
	public static boolean renderDebug = false;
	
	public Renderer(){
		Gdx.gl.glViewport(0, 0, Game.windowWidth, Game.windowHeight);
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
		modelview = new Matrix4();
		projection = new Matrix4();
		
		r2D = new Render2D(this);
		r3D = new Render3D(this);
	}
	
	/** Renders the entire scene- every entity and every GUI object */
	public void renderScene(){
		// clear color and depth buffers
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		// render each layer
		for(int i = 0; i < Game.physics.numLayers(); i++)
			renderEntities(Game.physics.getLayer(i).values().iterator());
		
		// render GUI
		r2D.switchTo2DScreenCoords();
		Game.gui.render(this);
		
		
	}
	
	/**
	 * Renders every entity in an iterator, switching between 2D and 3D modes as necessary.
	 * Any renderers extending EntityRenderer3D get drawn in 3D mode.
	 */
	private void renderEntities(Iterator<Entity> it){
		while(it.hasNext()){
			try{
				Entity ent = it.next();
				
				if(ent != null && ent.renderer != null){
					// 3D renderer
					if(ent.renderer instanceof EntityRenderer3D){
						r3D.switchTo3DRender();
						r3D.prepareToRenderEntity(ent);
						ent.renderer.render(this, ent, renderDebug);
						
					// 2D renderer
					} else {
						r2D.switchTo2DWorldCoords();
						r2D.prepareToRenderEntity(ent);
						ent.renderer.render(this, ent, renderDebug);
					}
				}
			} catch(ConcurrentModificationException e){
				break;
			}
		}
	}
}
