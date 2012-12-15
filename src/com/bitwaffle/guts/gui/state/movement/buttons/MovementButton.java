package com.bitwaffle.guts.gui.state.movement.buttons;

import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.gui.button.RectangleButton;

/**
 * A button that makes the player move. Automatically hides if there is no player.
 * 
 * @author TranquilMarmot
 */
public abstract class MovementButton extends RectangleButton {
	public MovementButton(float x, float y, float width, float height) {
		super(x, y, width, height);
	}
	
	@Override
	public void update(float timeStep) {
		if(Game.player == null && this.isVisible())
			this.hide();
		else if(Game.player != null && !this.isVisible())
			this.show();
	}
}
