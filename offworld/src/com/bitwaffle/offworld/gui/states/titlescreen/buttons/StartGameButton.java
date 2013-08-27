package com.bitwaffle.offworld.gui.states.titlescreen.buttons;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.gui.elements.button.TextButtonRenderer;
import com.bitwaffle.guts.gui.elements.button.rectangle.RectangleButton;
import com.bitwaffle.offworld.OffworldGame;
import com.bitwaffle.offworld.gui.states.GUIStates;

public class StartGameButton extends RectangleButton {
	private static final String TEXT = "Start\nGame";
	
	float xOffset, yOffset;

	public StartGameButton(float xOffset, float yOffset, int rows, int columns, float rowWidth, float columnHeight) {
		super(
				new TextButtonRenderer(
					TEXT,
					30.0f,
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
		OffworldGame.tempInit(Game.physics);
		Game.gui.setCurrentState(GUIStates.NONE.state);
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
	protected void onDrag(float dx, float dy){}
}
