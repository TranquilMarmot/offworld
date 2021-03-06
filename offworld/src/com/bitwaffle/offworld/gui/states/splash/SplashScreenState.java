package com.bitwaffle.offworld.gui.states.splash;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entity.Entity;
import com.bitwaffle.guts.entity.passive.GLSLSandbox;
import com.bitwaffle.guts.graphics.Renderer;
import com.bitwaffle.guts.graphics.graphics2d.ObjectRenderer2D;
import com.bitwaffle.guts.graphics.graphics3d.ObjectRenderer3D;
import com.bitwaffle.guts.graphics.graphics3d.model.ModelRenderer;
import com.bitwaffle.guts.gui.GUI;
import com.bitwaffle.guts.gui.elements.button.Button;
import com.bitwaffle.guts.gui.states.GUIState;
import com.bitwaffle.offworld.gui.states.GUIStates;

/**
 * Super fancy splash screen
 * 
 * @author TranquilMarmot
 */
public class SplashScreenState extends GUIState {
	private Entity bitwaffle, text;
	private GLSLSandbox background;
	
	private float timer;
	/** Total time in seconds that splash screen is displayed */
	private float splashScreenTime = 5.75f;
	
	public SplashScreenState(GUI gui){
		super(gui);
		background = new GLSLSandbox("shaders/sandbox/tadah.frag");
		
		// create waffle object
		ObjectRenderer3D renderer = new ModelRenderer(Game.resources.models.getModel("bitwaffle"));
		renderer.view.rotate(0.0f, 1.0f, 1.0f, 180.0f);
		renderer.view.scale(2.0f, 2.0f, 2.0f);
		bitwaffle = new Entity(renderer, 5, new Vector2(0.0f, 0.0f)){
			//float x = Game.random.nextFloat(), y = Game.random.nextFloat(), z = Game.random.nextFloat();
			float x = 0.0f, y = 0.0f, z = -1.0f;
			@Override
			public void update(float timeStep){
				ObjectRenderer3D rend = (ObjectRenderer3D)this.renderer;
				rend.view.rotate(x, y, z, timeStep * 100.0f);
				timer += timeStep;
				if(timer > splashScreenTime){
					Game.gui.setCurrentState(GUIStates.TITLESCREEN.state);
				}
				
				
				Vector2 worldSize = Game.renderer.camera.getWorldWindowSize();
				this.location.set((worldSize.x / 2.0f), (worldSize.y / 2.0f));
				Game.renderer.camera.setLocation(this.location);
			}
		};
		
		// create text object
		ObjectRenderer2D textRenderer = new ObjectRenderer2D(){
			@Override
			public void render(Renderer renderer, Object ent){
				Gdx.gl20.glEnable(GL20.GL_BLEND);
				Game.resources.textures.bindTexture("bitwaffle-text");
				renderer.r2D.quad.render(1.0f * 30.0f, 0.125f * 30.0f, true, true);
				Gdx.gl20.glDisable(GL20.GL_BLEND);
			}
		};
		
		text = new Entity(textRenderer, 6){
			@Override
			public void update(float timeStep){
				super.update(timeStep);
				
				Vector2 worldSize = Game.renderer.camera.getWorldWindowSize();
				this.location.set((worldSize.x / 2.0f), -1.0f);
			}
		};
	}

	@Override
	protected void onGainCurrentState() {
		Game.physics.addEntity(bitwaffle, false);
		Game.physics.addEntity(background, false);
		Game.physics.addEntity(text, false);
		
		// set camera to look at waffle
		//Game.renderer.camera.setMode(CameraModes.free);
		Game.renderer.camera.setLocation(bitwaffle.getLocation());
	}

	@Override
	protected void onLoseCurrentState() {
		Game.physics.removeEntity(bitwaffle, false);
		Game.physics.removeEntity(background, false);
		Game.physics.removeEntity(text, false);
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
