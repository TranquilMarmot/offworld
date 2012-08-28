package com.bitwaffle.moguts.gui.buttons.pause;

import com.bitwaffle.moguts.device.TextInput;
import com.bitwaffle.moguts.graphics.render.Render2D;
import com.bitwaffle.moguts.gui.buttons.RectangleButton;
import com.bitwaffle.moguts.serialization.GameSaver;
import com.bitwaffle.offworld.Game;

public class SaveButton extends RectangleButton {
	// offset of button from center
	public static float xOffset = -150.0f, yOffset = 150.0f;

	public SaveButton(PauseButtonManager buttMan) {
		super(
				(Game.windowWidth / 2.0f) + xOffset,
				(Game.windowHeight / 2.0f) + yOffset,
				buttMan.pauseMenuButtonWidth(),
				buttMan.pauseMenuButtonHeight());
	}

	@Override
	public void update() {
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
	protected void onPress() {
		Game.vibration.vibrate(25);
	}
	
	@Override
	public void render(Render2D renderer){
		Game.resources.textures.bindTexture("blank");
		super.render(renderer);
		Game.resources.font.drawString("Save", renderer, x, y + 17.0f, 0.3f);
	}
}
