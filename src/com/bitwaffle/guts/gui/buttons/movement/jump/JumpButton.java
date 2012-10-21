package com.bitwaffle.guts.gui.buttons.movement.jump;

import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.gui.buttons.RectangleButton;
import com.bitwaffle.guts.gui.buttons.movement.MovementButtonManager;
import com.bitwaffle.guts.input.KeyBindings;

/**
 * Button to jump
 */
public class JumpButton extends RectangleButton{
	protected MovementButtonManager buttMan;
	
	public JumpButton(MovementButtonManager buttMan, float x, float y, float width, float height) {
		super(x, y, width, height);
		this.buttMan = buttMan;
	}

	@Override
	public void update() {}
	@Override
	protected void onRelease() {
		KeyBindings.CONTROL_JUMP.release();
	}
	@Override
	protected void onSlideRelease() {}
	@Override
	protected void onPress() {
		Game.vibration.vibrate(25);
		KeyBindings.CONTROL_JUMP.press();
	}
}
