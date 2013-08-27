package com.bitwaffle.offworld.gui.elements.toolbox;

import com.badlogic.gdx.graphics.Color;
import com.bitwaffle.guts.gui.elements.button.rectangle.RectangleButton;
import com.bitwaffle.guts.gui.elements.button.rectangle.RectangleButtonRenderer;

public class InventoryButton extends RectangleButton {

	public InventoryButton(float x, float y, float width, float height) {
		super(new RectangleButtonRenderer("inventorybutton", new Color(0.5f, 0.5f, 0.5f, 0.75f), new Color(0.5f, 0.5f, 0.5f, 1.0f)), x, y, width, height);
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
