package com.bitwaffle.guts.android.gui.states.movement.buttons.left;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.android.gui.states.movement.buttons.MovementButton;

/**
 * Button to move left
 */
public abstract class MoveLeftButton extends MovementButton {
	public MoveLeftButton(float x, float y, float width, float height) {
		super(x, y, width, height);
	}

	@Override
	protected void onRelease() {
		for(InputProcessor proc : Game.input.multiplexer.getProcessors())
			proc.keyUp(Input.Keys.A);
	}
	@Override
	protected void onSlideRelease() {
		for(InputProcessor proc : Game.input.multiplexer.getProcessors())
			proc.keyUp(Input.Keys.A);
	}
	@Override
	protected void onPress() {
		//Game.vibration.vibrate(25);
		for(InputProcessor proc : Game.input.multiplexer.getProcessors())
			proc.keyDown(Input.Keys.A);
	}
	
	@Override
	protected void onDrag(float dx, float dy){}
}
