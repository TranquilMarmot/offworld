package com.bitwaffle.offworld.gui.elements.toolbox;

import com.badlogic.gdx.graphics.Color;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.gui.elements.button.TransparentRectangleButton;
import com.bitwaffle.offworld.gui.elements.map.Map;

public class MapButton extends TransparentRectangleButton {

	public MapButton(float x, float y, float width, float height) {
		super(x, y, width, height, "mapbutton", new Color(0.5f, 0.5f, 0.5f, 0.75f), new Color(0.5f, 0.5f, 0.5f, 1.0f));
	}

	@Override
	protected void onRelease() {
		Game.gui.addObject(new Map(0.0f, 100.0f));
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
