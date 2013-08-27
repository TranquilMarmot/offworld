package com.bitwaffle.guts.android.gui.states.movement.buttons.jump;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.android.gui.states.movement.buttons.MovementButton;
import com.bitwaffle.guts.gui.elements.button.rectangle.RectangleButtonRenderer;

/**
 * Button to jump
 */
public abstract class JumpButton extends MovementButton {
	public JumpButton(float x, float y, float width, float height) {
		super(new RectangleButtonRenderer("uparrow", true, new Color(0.5f, 0.5f, 0.5f, 0.75f), new Color(0.5f, 0.5f, 0.5f, 1.0f)), x, y, width, height);
	}

	@Override
	protected void onRelease() {
		for(InputProcessor proc : Game.input.multiplexer.getProcessors())
			proc.keyUp(Input.Keys.SPACE);
	}
	@Override
	protected void onSlideRelease() {
		for(InputProcessor proc : Game.input.multiplexer.getProcessors())
			proc.keyUp(Input.Keys.SPACE);
	}
	@Override
	protected void onPress() {
		//Game.vibration.vibrate(25);
		for(InputProcessor proc : Game.input.multiplexer.getProcessors())
			proc.keyDown(Input.Keys.SPACE);
	}
	
	@Override
	protected void onDrag(float dx, float dy){}
}
