package com.bitwaffle.moguts.gui.buttons.movement.left;

import com.bitwaffle.moguts.gui.buttons.ButtonManager;
import com.bitwaffle.moguts.gui.buttons.RectangleButton;
import com.bitwaffle.offworld.Game;

/**
 * Button to move left
 */
public class MoveLeftButton extends RectangleButton{
	protected ButtonManager buttMan;
	
	public MoveLeftButton(ButtonManager buttMan, float x, float y, float width, float height) {
		super(x, y, width, height);
		this.buttMan = buttMan;
	}

	@Override
	public void update() {
		if(this.isDown())
			Game.player.goLeft();
	}
	@Override
	protected void onRelease() {}
	@Override
	protected void onSlideRelease() {}
	@Override
	protected void onPress() {
		Game.vibration.vibrate(25);
	}
}
