package com.bitwaffle.guts.gui.buttons.pause;

import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.android.input.TextInput;
import com.bitwaffle.guts.graphics.render.Render2D;
import com.bitwaffle.guts.gui.buttons.RectangleButton;
import com.bitwaffle.guts.serialization.GameSaver;

public class LoadButton extends RectangleButton {
	// offset of button from center
	public static float xOffset = 150.0f, yOffset = 150.0f;

	public LoadButton(PauseButtonManager buttMan) {
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
		TextInput input = new TextInput("Load Game", "Enter save to load"){
			@Override
			public void parseInput(String input) {
				saver.loadGame(input + ".ofw", Game.physics);
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
	public void render(Render2D renderer, boolean flipHorizontal, boolean flipVertical){
		Game.resources.textures.bindTexture("blank");
		super.render(renderer, flipHorizontal, flipVertical);
		Game.resources.font.drawString("Load", renderer, x, y + 17.0f, 0.3f);
	}
}