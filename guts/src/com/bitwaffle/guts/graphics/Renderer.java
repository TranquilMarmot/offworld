package com.bitwaffle.guts.graphics;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Matrix4;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entity.Entity;
import com.bitwaffle.guts.graphics.camera.Camera;
import com.bitwaffle.guts.graphics.graphics2d.Render2D;
import com.bitwaffle.guts.graphics.graphics3d.EntityRenderer3D;
import com.bitwaffle.guts.graphics.graphics3d.Render3D;

/**
 * Handles rendering the entire scene
 * 
 * @author TranquilMarmot
 */
public class Renderer {
	
	/** Used to know when to switch render modes */
	private enum RenderMode{ r2DWorld, r2DScreen, r3D }
	private RenderMode currentMode;
	
	/** Matrices for use with shaders */
	public Matrix4 modelview, projection;
	
	/** 2D Renderer */
	public Render2D r2D;
	
	/** 2D Renderer */
	public Render3D r3D;
	
	/** Camera for describing how the scene should be looked at */
	public Camera camera;
	
	/** Whether or not debug rendering mode is enabled */
	public static boolean renderDebug = false;
	
	public Renderer(){
		Gdx.gl.glViewport(0, 0, Game.windowWidth, Game.windowHeight);
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
		modelview = new Matrix4();
		projection = new Matrix4();
		
		r2D = new Render2D(this);
		r3D = new Render3D(this);
		
		camera = new Camera();
	}
	
	/**
	 * Updates the renderer - pretty much just updates cameras right now
	 * @param timeStep Time passed since last update, in seconds
	 */
	public void update(float timeStep) {
		//if(r2D.camera != null)
			//camera.update(timeStep);
		//if(r3D.camera != null)
		//	r3D.camera.update(timeStep);
	}
	
	/** Switched render mode. Does nothing if give mode is same as currentMode. */
	private void switchMode(RenderMode mode){
		if(currentMode == mode)
			return;
		else if(mode == RenderMode.r2DWorld)
			r2D.switchTo2DWorldCoords();
		else if(mode == RenderMode.r2DScreen)
			r2D.switchTo2DScreenCoords();
		else if(mode == RenderMode.r3D)
			r3D.switchTo3DRender();
		currentMode = mode;
	}
	
	/** Clears the screen and then renders the entire scene- every entity and every GUI object */
	public void renderScene(){
		// clear color and depth buffers
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		renderEntities();
		renderGUI();
	}
	
	/** Renders every entity and GUI object */
	protected void renderEntities(){
		// render each layer
		for(int i = 0; i < Game.physics.numLayers(); i++)
			renderEntities(Game.physics.getLayer(i).values().iterator());
	}
	
	protected void renderGUI(){
		// render GUI
		switchMode(RenderMode.r2DScreen);
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
						switchMode(RenderMode.r3D);
						r3D.prepareToRenderEntity(ent);
						ent.renderer.render(this, ent, renderDebug);
						
					// 2D renderer
					} else {
						switchMode(RenderMode.r2DWorld);
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
