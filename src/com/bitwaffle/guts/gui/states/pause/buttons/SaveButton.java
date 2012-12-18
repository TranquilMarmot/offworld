package com.bitwaffle.guts.gui.states.pause.buttons;

import android.opengl.GLES20;

import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.android.input.TextInput;
import com.bitwaffle.guts.graphics.render.Render2D;
import com.bitwaffle.guts.gui.button.TextButton;
import com.bitwaffle.guts.gui.states.pause.PauseGUIState;
import com.bitwaffle.guts.serialization.GameSaver;

public class SaveButton extends TextButton {
	// offset of button from center
	public static float xOffset = -150.0f, yOffset = 150.0f;

	public SaveButton(PauseGUIState buttMan) {
		super(
				"Save",
				(Game.windowWidth / 2.0f) + xOffset,
				(Game.windowHeight / 2.0f) + yOffset,
				buttMan.pauseMenuButtonWidth(),
				buttMan.pauseMenuButtonHeight());
	}

	@Override
	public void update(float timeStep) {
		this.x = (Game.windowWidth / 2.0f) + xOffset;
		this.y = (Game.windowHeight / 2.0f) + yOffset;
	}

	@Override
	protected void onRelease() {
		// ask the user where to save the file to
		final GameSaver saver = new GameSaver();
		TextInput input = new TextInput("Save Game", "Enter save name"){
			@Override
			public void parseInput(String input) {
				saver.saveGame(input + ".ofw", Game.physics);
			}
		};
		input.askForInput();
	}

	@Override
	protected void onSlideRelease() {}
	
	@Override
	protected void onDrag(float dx, float dy){}

	@Override
	protected void onPress() {
		Game.vibration.vibrate(25);
	}
	
	@Override
	public void render(Render2D renderer, boolean flipHorizontal, boolean flipVertical){
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_SRC_COLOR);
		
		renderer.program.setUniform("vColor", 0.4f, 0.4f, 0.4f, 0.9f);
		super.renderBackground(renderer, flipHorizontal, flipVertical);
		
		super.renderText(renderer, flipHorizontal, flipVertical);
		
		GLES20.glDisable(GLES20.GL_BLEND);
	}
}
