package com.bitwaffle.guts.gui.buttons.movement.left;

import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.gui.buttons.movement.MovementButton;
import com.bitwaffle.guts.gui.buttons.movement.MovementButtonManager;
import com.bitwaffle.guts.input.KeyBindings;

/**
 * Button to move left
 */
public abstract class MoveLeftButton extends MovementButton {
	protected MovementButtonManager buttMan;
	
	public MoveLeftButton(MovementButtonManager buttMan, float x, float y, float width, float height) {
		super(x, y, width, height);
		this.buttMan = buttMan;
	}

	@Override
	protected void onRelease() {
		KeyBindings.CONTROL_LEFT.release();
	}
	@Override
	protected void onSlideRelease() {
		KeyBindings.CONTROL_LEFT.release();
	}
	@Override
	protected void onPress() {
		Game.vibration.vibrate(25);
		KeyBindings.CONTROL_LEFT.press();
	}
}
