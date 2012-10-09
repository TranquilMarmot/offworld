package com.bitwaffle.guts.gui.buttons.movement.left;

import com.bitwaffle.guts.gui.buttons.RectangleButton;
import com.bitwaffle.guts.gui.buttons.movement.MovementButtonManager;
import com.bitwaffle.offworld.Game;

/**
 * Button to move left
 */
public class MoveLeftButton extends RectangleButton{
	protected MovementButtonManager buttMan;
	
	public MoveLeftButton(MovementButtonManager buttMan, float x, float y, float width, float height) {
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
