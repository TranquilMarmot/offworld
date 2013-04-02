package com.bitwaffle.guts.gui.states.movement.buttons.right;

import com.badlogic.gdx.Input;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.gui.states.movement.buttons.MovementButton;

/**
 * Button to move right
 */
public abstract class MoveRightButton extends MovementButton{
	public MoveRightButton(float x, float y, float width, float height) {
		super(x, y, width, height);
	}

	@Override
	protected void onRelease() {
		Game.input.multiplexer.keyUp(Input.Keys.D);
	}
	@Override
	protected void onSlideRelease() {
		Game.input.multiplexer.keyUp(Input.Keys.D);
	}
	@Override
	protected void onPress() {
		//Game.vibration.vibrate(25);
		Game.input.multiplexer.keyDown(Input.Keys.D);
	}
	
	@Override
	protected void onDrag(float dx, float dy){}
}
