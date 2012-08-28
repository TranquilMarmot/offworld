package com.bitwaffle.moguts.gui.buttons.movement.jump;

import com.bitwaffle.moguts.gui.buttons.RectangleButton;
import com.bitwaffle.moguts.gui.buttons.movement.MovementButtonManager;
import com.bitwaffle.offworld.Game;

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
	protected void onRelease() {}
	@Override
	protected void onSlideRelease() {}
	@Override
	protected void onPress() {
		Game.vibration.vibrate(25);
		Game.player.jump();
	}
}
