package com.bitwaffle.guts.android.gui.states.movement.buttons;

import com.bitwaffle.guts.gui.elements.button.rectangle.RectangleButton;
import com.bitwaffle.guts.gui.elements.button.rectangle.RectangleButtonRenderer;
import com.bitwaffle.offworld.OffworldGame;

/**
 * A button that makes the player move. Automatically hides if there is no player.
 * 
 * @author TranquilMarmot
 */
public abstract class MovementButton extends RectangleButton {
	public MovementButton(RectangleButtonRenderer renderer, float x, float y, float width, float height) {
		super(renderer, x, y, width, height);
	}
	
	@Override
	public void update(float timeStep) {
		if(OffworldGame.players[0] == null && this.isVisible())
			this.hide();
		else if(OffworldGame.players[0] != null && !this.isVisible())
			this.show();
	}
}
