package com.bitwaffle.moguts.gui.buttons.movement.right;

import com.bitwaffle.moguts.gui.buttons.ButtonManager;
import com.bitwaffle.moguts.gui.buttons.RectangleButton;
import com.bitwaffle.offworld.Game;

/**
 * Button to move right
 */
public class MoveRightButton extends RectangleButton{
	protected ButtonManager buttMan;
	
	public MoveRightButton(ButtonManager buttMan, float x, float y, float width, float height) {
		super(x, y, width, height);
		this.buttMan = buttMan;
	}

	@Override
	public void update() {
		if(this.isDown())
			Game.player.goRight();
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
