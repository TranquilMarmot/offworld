package com.bitwaffle.guts.gui.states.pause.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.Render2D;
import com.bitwaffle.guts.gui.button.TextButton;
import com.bitwaffle.guts.gui.states.pause.PauseGUIState;

public class LoadButton extends TextButton {
	// offset of button from center
	public static float xOffset = 200.0f, yOffset = 150.0f;

	public LoadButton(PauseGUIState state) {
		super(
				"Load",
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
		// ask the user where to save the file to
		/*
		final GameSaver saver = new GameSaver();
		TextInput input = new TextInput("Load Game", "Enter save to load"){
			@Override
			public void parseInput(String input) {
				saver.loadGame(input + ".ofw", Game.physics);
			}
		};
		input.askForInput();
		*/
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
	public void render(Render2D renderer, boolean flipHorizontal, boolean flipVertical){
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_SRC_COLOR);
		
		renderer.program.setUniform("vColor", 0.4f, 0.4f, 0.4f, 0.9f);
		super.renderBackground(renderer, flipHorizontal, flipVertical);
		
		super.renderText(renderer, flipHorizontal, flipVertical);
		
		Gdx.gl20.glDisable(GL20.GL_BLEND);
	}
}