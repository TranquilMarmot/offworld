package com.bitwaffle.offworld.gui.states.titlescreen.buttons;

import com.badlogic.gdx.graphics.Color;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.gui.elements.button.TextButtonRenderer;
import com.bitwaffle.guts.gui.elements.button.rectangle.RectangleButton;
import com.bitwaffle.offworld.gui.states.GUIStates;

public class OptionsButton extends RectangleButton {
	private static final String TEXT = "Options";
	
	float xOffset, yOffset;

	public OptionsButton(float xOffset, float yOffset, int rows, int columns, float rowWidth, float columnHeight) {
		super(
				new TextButtonRenderer(
					new Color(0.5f, 0.5f, 0.5f, 0.75f), 
					new Color(0.5f, 0.5f, 0.5f, 1.0f),
					new Color(0.75f, 0.75f, 0.75f, 1.0f),
					new Color(0.5f, 0.5f, 0.5f, 0.8f),
					TEXT,
					25.0f,
					rows,
					columns,
					rowWidth,
					columnHeight
				),
				0.0f,
				0.0f,
				rows * rowWidth,
				columns * columnHeight
			);
		
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
	
	@Override
	public void update(float timeStep){
		this.x = ((float)Game.windowWidth / 2.0f) + xOffset;
		this.y = ((float)Game.windowHeight / 2.0f) + yOffset;
	}

	@Override
	protected void onRelease() {
		Game.gui.setCurrentState(GUIStates.OPTIONS.state);
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
	protected void onDrag(float dx, float dy){
		this.xOffset += dx;
		this.yOffset += dy;
	}
}
