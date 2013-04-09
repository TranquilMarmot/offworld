package com.bitwaffle.guts.gui.states.movement.buttons.jump;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.gui.states.movement.buttons.MovementButton;

/**
 * Button to jump
 */
public abstract class JumpButton extends MovementButton {
	public JumpButton(float x, float y, float width, float height) {
		super(x, y, width, height);
	}

	@Override
	protected void onRelease() {
		//Game.input.multiplexer.keyUp(Input.Keys.SPACE);
		
		for(InputProcessor proc : Game.input.multiplexer.getProcessors()){
			proc.keyUp(Input.Keys.SPACE);
		}
	}
	@Override
	protected void onSlideRelease() {
		//Game.input.multiplexer.keyUp(Input.Keys.SPACE);
		
		for(InputProcessor proc : Game.input.multiplexer.getProcessors()){
			proc.keyUp(Input.Keys.SPACE);
		}
	}
	@Override
	protected void onPress() {
		//Game.vibration.vibrate(25);
		//Game.input.multiplexer.keyDown(Input.Keys.SPACE);
		
		for(InputProcessor proc : Game.input.multiplexer.getProcessors()){
			proc.keyDown(Input.Keys.SPACE);
		}
	}
	
	@Override
	protected void onDrag(float dx, float dy){}
}
