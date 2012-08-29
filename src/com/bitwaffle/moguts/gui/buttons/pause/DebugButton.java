package com.bitwaffle.moguts.gui.buttons.pause;

import com.bitwaffle.moguts.graphics.render.Render2D;
import com.bitwaffle.moguts.gui.buttons.RectangleButton;
import com.bitwaffle.offworld.Game;

public class DebugButton extends RectangleButton {
	// offset of button from center
	public static float xOffset = 150.0f, yOffset = -150.0f;

	public DebugButton(PauseButtonManager buttMan) {
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
		Render2D.drawDebug = !Render2D.drawDebug;
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
		Game.resources.font.drawString("Debug", renderer, x, y + 17.0f, 0.3f);
	}
}