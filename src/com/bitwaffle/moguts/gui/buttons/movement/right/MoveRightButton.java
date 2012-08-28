package com.bitwaffle.moguts.gui.buttons.movement.right;

import com.bitwaffle.moguts.gui.buttons.RectangleButton;
import com.bitwaffle.moguts.gui.buttons.movement.MovementButtonManager;
import com.bitwaffle.offworld.Game;

/**
 * Button to move right
 */
public class MoveRightButton extends RectangleButton{
	protected MovementButtonManager buttMan;
	
	public MoveRightButton(MovementButtonManager buttMan, float x, float y, float width, float height) {
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
