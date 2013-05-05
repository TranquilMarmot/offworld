package com.bitwaffle.offworld.gui.states.splash;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entity.Entity;
import com.bitwaffle.guts.entity.passive.GLSLSandbox;
import com.bitwaffle.guts.graphics.render.render2d.camera.Camera2D;
import com.bitwaffle.guts.graphics.render.render3d.EntityRenderer3D;
import com.bitwaffle.guts.gui.GUI;
import com.bitwaffle.guts.gui.button.Button;
import com.bitwaffle.guts.gui.states.GUIState;
import com.bitwaffle.guts.graphics.shapes.model.ModelPolygonRenderer;

public class SplashScreenState extends GUIState {
	
	private Entity bitwaffle;
	
	private GLSLSandbox background;
	
	private float timer;
	
	private float splashScreenTime = 10.0f;
	
	public SplashScreenState(){
		EntityRenderer3D renderer = new ModelPolygonRenderer(Game.resources.models.getModel("bitwaffle"));
		renderer.rotation.rotate(0.0f, 1.0f, 1.0f, 180.0f);
		renderer.rotation.scale(0.5f, 0.5f, 0.5f);
		bitwaffle = new Entity(renderer, 5, new Vector2(0.0f, 0.0f)){
			//float x = Game.random.nextFloat(), y = Game.random.nextFloat(), z = Game.random.nextFloat();
			float x = 0.0f, y = 0.0f, z = -1.0f;
			@Override
			public void update(float timeStep){
				EntityRenderer3D rend = (EntityRenderer3D)this.renderer;
				rend.rotation.rotate(x, y, z, timeStep * 100.0f);
				timer += timeStep;
				if(timer > splashScreenTime){
					Game.gui.setCurrentState(GUI.States.TITLESCREEN);
				}
				
				
				Vector2 worldSize = Game.renderer.r2D.camera.getWorldWindowSize();
				this.location.set((worldSize.x / 2.0f), (worldSize.y / 2.0f));
				Game.renderer.r2D.camera.setLocation(this.location);
			}
		};
		
		background = new GLSLSandbox("shaders/sandbox/tadah.frag");
	}

	@Override
	protected void onGainCurrentState() {
		Game.physics.addEntity(bitwaffle, false);
		Game.physics.addEntity(background, false);
		
		Game.renderer.r2D.camera.setMode(Camera2D.Modes.FREE);
		Game.renderer.r2D.camera.setLocation(bitwaffle.getLocation());
	}

	@Override
	protected void onLoseCurrentState() {
		Game.physics.removeEntity(bitwaffle, false);
		Game.physics.removeEntity(background, false);
	}

	@Override
	public Button initialLeftButton() { return null; }

	@Override
	public Button initialRightButton() { return null; }

	@Override
	public Button initialUpButton() { return null; }

	@Override
	public Button initialDownButton() { return null; }
}
