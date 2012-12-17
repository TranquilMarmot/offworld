package com.bitwaffle.guts.gui.states.movement.buttons.right;

import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.gui.states.movement.buttons.MovementButton;
import com.bitwaffle.guts.input.KeyBindings;

/**
 * Button to move right
 */
public abstract class MoveRightButton extends MovementButton{
	public MoveRightButton(float x, float y, float width, float height) {
		super(x, y, width, height);
	}

	@Override
	protected void onRelease() {
		KeyBindings.CONTROL_RIGHT.release();
	}
	@Override
	protected void onSlideRelease() {
		KeyBindings.CONTROL_RIGHT.release();
	}
	@Override
	protected void onPress() {
		Game.vibration.vibrate(25);
		KeyBindings.CONTROL_RIGHT.press();
	}
}
