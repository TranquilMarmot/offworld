package com.bitwaffle.offworld.gui.states.pause.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.render.Renderer;
import com.bitwaffle.guts.gui.button.TextButton;
import com.bitwaffle.offworld.gui.states.GUIStates;
import com.bitwaffle.offworld.gui.states.pause.PauseState;

public class QuitButton extends TextButton {
	// offset of button from center
	public static float xOffset = -200.0f, yOffset = -150.0f;

	public QuitButton(PauseState state) {
		super(
				"Main Menu",
				25.0f,
				(Game.windowWidth / 2.0f) + xOffset,
				(Game.windowHeight / 2.0f) + yOffset,
				state.buttonRows(),
				state.buttonCols(),
				state.buttonRowWidth(),
				state.buttonColHeight());
	}

	@Override
	public void update(float timeStep) {
		this.x = (Game.windowWidth / 2.0f) + xOffset;
		this.y = (Game.windowHeight / 2.0f) + yOffset;
	}

	@Override
	protected void onRelease() {
		Game.physics.clearWorld();
		
		for(int i = 0; i < Game.players.length; i++)
			Game.players[i] = null;
		
		Game.renderer.r2D.camera.setTarget(null);
		
		Game.gui.setCurrentState(GUIStates.TITLESCREEN);
		if(Game.isPaused())
			Game.togglePause();
	}

	@Override
	protected void onSlideRelease() {}
	
	@Override
	protected void onDrag(float dx, float dy){}

	@Override
	protected void onPress() {
		//Game.vibration.vibrate(25);
	}
	
	@Override
	protected void onSelect() {
	}

	@Override
	protected void onUnselect() {
	}
	
	@Override
	public void render(Renderer renderer, boolean flipHorizontal, boolean flipVertical){
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_SRC_COLOR);
		
		float r = 0.5f;
		float g = 0.5f;
		float b = 0.5f;
		float a = 0.5f;
		if(this.isDown())
			a = 1.0f;
		else if(this.isSelected())
			a = 0.75f;
		renderer.r2D.setColor(r, g, b, a);
		
		//renderer.program.setUniform("vColor", 0.4f, 0.4f, 0.4f, 0.9f);
		super.renderBackground(renderer, flipHorizontal, flipVertical);
		
		super.renderText(renderer, flipHorizontal, flipVertical);
		
		Gdx.gl20.glDisable(GL20.GL_BLEND);
	}
}
