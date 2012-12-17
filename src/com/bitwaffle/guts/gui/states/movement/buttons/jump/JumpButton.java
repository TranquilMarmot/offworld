package com.bitwaffle.guts.gui.states.movement.buttons.jump;

import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.gui.states.movement.buttons.MovementButton;
import com.bitwaffle.guts.input.KeyBindings;

/**
 * Button to jump
 */
public abstract class JumpButton extends MovementButton {
	public JumpButton(float x, float y, float width, float height) {
		super(x, y, width, height);
	}

	@Override
	protected void onRelease() {
		KeyBindings.CONTROL_JUMP.release();
	}
	@Override
	protected void onSlideRelease() {
		KeyBindings.CONTROL_JUMP.release();
	}
	@Override
	protected void onPress() {
		Game.vibration.vibrate(25);
		KeyBindings.CONTROL_JUMP.press();
	}
}
