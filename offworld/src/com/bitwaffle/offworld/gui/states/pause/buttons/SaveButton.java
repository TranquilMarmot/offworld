package com.bitwaffle.offworld.gui.states.pause.buttons;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.gui.elements.button.TextButtonRenderer;
import com.bitwaffle.guts.gui.elements.button.rectangle.RectangleButton;
import com.bitwaffle.offworld.gui.states.pause.PauseState;

public class SaveButton extends RectangleButton {
	// offset of button from center
	public static float xOffset = -200.0f, yOffset = 150.0f;

	public SaveButton(PauseState state) {
		super(
			new TextButtonRenderer(
				"Save",
				25.0f,
				state.buttonRows(),
				state.buttonCols(),
				state.buttonRowWidth(),
				state.buttonColHeight()
			),
			(Game.windowWidth / 2.0f) + xOffset,
			(Game.windowHeight / 2.0f) + yOffset,
			state.buttonRows() * state.buttonRowWidth(),
			state.buttonCols() * state.buttonColHeight()
		);
	}

	@Override
	public void update(float timeStep) {
		this.x = (Game.windowWidth / 2.0f) + xOffset;
		this.y = (Game.windowHeight / 2.0f) + yOffset;
	}

	@Override
	protected void onRelease() {
		/*
		// ask the user where to save the file to
		final GameSaver saver = new GameSaver();
		TextInput input = new TextInput("Save Game", "Enter save name"){
			@Override
			public void parseInput(String input) {
				saver.saveGame(input + ".ofw", Game.physics);
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
	protected void onSelect() {
	}

	@Override
	protected void onUnselect() {
	}
}
