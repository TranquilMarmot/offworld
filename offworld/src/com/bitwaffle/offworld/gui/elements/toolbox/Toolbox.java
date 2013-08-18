package com.bitwaffle.offworld.gui.elements.toolbox;

import com.bitwaffle.guts.graphics.Renderer;
import com.bitwaffle.guts.gui.elements.button.RectangleButton;
import com.bitwaffle.offworld.entities.player.Player;

/**
 * An in-game menu for accessing maps, inventory, etc.
 * 
 * @author TranquilMarmot
 */
public class Toolbox extends RectangleButton {
	/** The player that this toolbox belongs to */
	private Player player;
	
	public Toolbox(Player player, float x, float y, float width, float height){
		super(x, y, width, height);
		this.player = player;
	}

	@Override
	public void render(Renderer renderer, boolean flipHorizontal,
			boolean flipVertical) {
		
	}

	@Override
	protected void onRelease() {
		
	}

	@Override
	protected void onSlideRelease() {
		
	}

	@Override
	protected void onPress() {
		
	}

	@Override
	protected void onSelect() {
		
	}

	@Override
	protected void onUnselect() {
		
	}

	@Override
	protected void onDrag(float dx, float dy) {
		
	}

	@Override
	public void update(float timeStep) {
		
	}
}
