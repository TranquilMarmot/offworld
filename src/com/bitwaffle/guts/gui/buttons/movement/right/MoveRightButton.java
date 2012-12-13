package com.bitwaffle.guts.gui.buttons.movement.right;

import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.gui.buttons.RectangleButton;
import com.bitwaffle.guts.gui.buttons.movement.MovementButtonManager;
import com.bitwaffle.guts.input.KeyBindings;

/**
 * Button to move right
 */
public abstract class MoveRightButton extends RectangleButton{
	protected MovementButtonManager buttMan;
	
	public MoveRightButton(MovementButtonManager buttMan, float x, float y, float width, float height) {
		super(x, y, width, height);
		this.buttMan = buttMan;
	}

	@Override
	public void update(float timeStep) {
		if(this.isDown())
			Game.player.goRight();
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
